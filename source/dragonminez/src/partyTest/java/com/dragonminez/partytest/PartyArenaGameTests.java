package com.dragonminez.partytest;

import com.dragonminez.common.quest.*;
import com.dragonminez.common.network.S2C.PartyHudS2C;
import com.dragonminez.server.events.ArenaManager;
import com.dragonminez.server.events.QuestEvents;
import com.dragonminez.server.world.data.ArenaSavedData;
import com.google.gson.JsonParser;
import com.mojang.authlib.GameProfile;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.gametest.GameTestHolder;
import net.minecraftforge.gametest.PrefixGameTestTemplate;
import io.netty.buffer.Unpooled;
import java.lang.reflect.Field;
import java.util.*;

/** Runs only with -PpartyGameTests=true, never shipped in the production JAR. */
@GameTestHolder("dragonminez")
@PrefixGameTestTemplate(false)
public final class PartyArenaGameTests {
    @SuppressWarnings("unchecked")
    private static Map<UUID, ServerPlayer> online(GameTestHelper helper) {
        try {
            Field field = PlayerList.class.getDeclaredField("playersByUUID");
            field.setAccessible(true);
            return (Map<UUID, ServerPlayer>) field.get(helper.getLevel().getServer().getPlayerList());
        } catch (Exception e) { throw new RuntimeException(e); }
    }
    private static ServerPlayer player(GameTestHelper helper, String name) {
        var player = new FakePlayer(helper.getLevel(), new GameProfile(UUID.randomUUID(), name));
        player.setPos(helper.absolutePos(new BlockPos(1, 2, 1)).getCenter());
        online(helper).put(player.getUUID(), player);
        PartyQuestProgress.data(player).setDifficultyChosen(true);
        return player;
    }
    @SuppressWarnings("unchecked")
    private static QuestService.ResolvedQuest quest(String key) {
        String json = """
            {"id":"%s","title":"Test battle","type":"SIDEQUEST","party_scaling":false,
            "prerequisites":{"operator":"AND","conditions":[{"type":"QUEST","questId":"test_gate"}]},
            "requirements":{"operator":"AND","conditions":[{"type":"DIMENSION","dimension":"minecraft:overworld"}]},
            "objectives":[{"type":"KILL","entity":"minecraft:zombie","count":2,"health":20}],
            "rewards":[{"type":"ITEM","item":"minecraft:apple","count":1}]}
            """.formatted(key);
        Quest quest = Objects.requireNonNull(QuestParser.parseQuest(JsonParser.parseString(json).getAsJsonObject()));
        try {
            Field field = QuestRegistry.class.getDeclaredField("LOADED_QUESTS");
            field.setAccessible(true);
            ((Map<String, Quest>) field.get(null)).put(key, quest);
        } catch (Exception e) { throw new RuntimeException(e); }
        return new QuestService.ResolvedQuest(key, quest, null);
    }
    private static void join(GameTestHelper helper, ServerPlayer leader, ServerPlayer member) {
        PartyManager.sendInvite(leader, member);
        helper.assertTrue(PartyManager.acceptInvite(member, true) == PartyManager.InviteAcceptResult.SUCCESS, "Party invitation failed");
    }
    private static List<LivingEntity> enemies(ServerPlayer player, String key) {
        return player.serverLevel().getEntitiesOfClass(LivingEntity.class, player.getBoundingBox().inflate(80),
                e -> key.equals(e.getPersistentData().getString(QuestService.QUEST_KEY_TAG)));
    }

    @GameTest(template = "cell_arena", timeoutTicks = 300)
    public static void sharedBattleRewardsAndReturn(GameTestHelper helper) {
        ServerPlayer a = player(helper, "ArenaLeader"), b = player(helper, "ArenaMember"), c = player(helper, "ArenaSupport");
        var qa = PartyQuestProgress.data(a); var qb = PartyQuestProgress.data(b); var qc = PartyQuestProgress.data(c);
        qa.completeQuest("test_gate"); qb.completeQuest("test_gate"); qa.completeQuest("leader_history");
        join(helper, a, b); join(helper, a, c);
        helper.assertFalse(qb.isQuestCompleted("leader_history"), "Joining copied leader history");
        helper.assertFalse(qc.isQuestCompleted("test_gate"), "Joining unlocked a locked quest");
        String key = "party_test_shared"; var resolved = quest(key);
        helper.assertTrue(PartyQuestProgress.eligible(b, resolved), "Eligible member rejected");
        helper.assertFalse(PartyQuestProgress.eligible(c, resolved), "Locked helper eligible for rewards");
        var originA = a.position(); var originB = b.position(); var originC = c.position();
        var failure = QuestService.startQuest(b, key); // A non-leader starts the battle.
        helper.assertTrue(failure == null, "Start failed: " + (failure == null ? "" : failure.getString()));
        helper.assertTrue(a.level().dimension().equals(ArenaManager.DIMENSION) && c.level() == a.level(), "Entire party was not transported");
        helper.assertTrue(qa.isQuestAccepted(key) && qb.isQuestAccepted(key) && !qc.isQuestAccepted(key), "Wrong reward eligibility snapshot");
        helper.assertTrue(QuestService.resummonQuest(a, key) != null, "Duplicate wave allowed");
        List<LivingEntity> enemies = enemies(a, key);
        helper.assertTrue(enemies.size() == 2, "Expected exactly two enemies, got " + enemies.size());
        QuestEvents.creditQuestKill(c, enemies.get(0));
        QuestEvents.creditQuestKill(c, enemies.get(0));
        helper.assertTrue(qa.getObjectiveProgress(key, 0) == 1 && qb.getObjectiveProgress(key, 0) == 1, "Helper progress not shared or duplicate kill counted");
        QuestEvents.creditQuestKill(c, enemies.get(1));
        helper.assertTrue(qa.isQuestCompleted(key) && qb.isQuestCompleted(key), "Party completion missing");
        helper.assertFalse(qc.isQuestCompleted(key), "Locked helper received completion");
        helper.assertTrue(a.getInventory().countItem(Items.APPLE) == 1 && b.getInventory().countItem(Items.APPLE) == 1
                && c.getInventory().countItem(Items.APPLE) == 0, "Wrong reward distribution");
        QuestService.claimRewards(a, key); QuestService.claimAllRewards(b);
        helper.assertTrue(a.getInventory().countItem(Items.APPLE) == 1 && b.getInventory().countItem(Items.APPLE) == 1, "Duplicate reward issued");
        PlayerQuestData restored = new PlayerQuestData(); restored.deserializeNBT(qa.serializeNBT());
        helper.assertTrue(restored.isRewardClaimed(key, 0), "Reward receipt did not survive NBT roundtrip");
        helper.runAfterDelay(3, () -> {
            helper.assertTrue(a.level().dimension().equals(Level.OVERWORLD) && b.level() == a.level() && c.level() == a.level(), "Party not returned");
            helper.assertTrue(a.position().distanceTo(originA) < 0.1 && b.position().distanceTo(originB) < 0.1 && c.position().distanceTo(originC) < 0.1, "Wrong return coordinates");
            helper.assertFalse(ArenaSavedData.get(a.getServer()).returns.containsKey(a.getUUID()), "Return ticket not consumed");
            for (var p : List.of(a,b,c)) { PartyManager.leaveParty(p); online(helper).remove(p.getUUID()); p.discard(); }
            helper.succeed();
        });
    }

    @GameTest(template = "cell_arena", timeoutTicks = 300)
    public static void departureDoesNotStrandRemainingMembers(GameTestHelper helper) {
        ServerPlayer a = player(helper, "DepartingOwner"), b = player(helper, "RemainingMember");
        PartyQuestProgress.data(a).completeQuest("test_gate"); PartyQuestProgress.data(b).completeQuest("test_gate");
        join(helper,a,b);
        String key = "party_test_departure"; quest(key);
        helper.assertTrue(QuestService.startQuest(a,key) == null,"Arena start failed");
        List<LivingEntity> enemies = enemies(a,key);
        var saved = ArenaSavedData.get(a.getServer());
        ArenaSavedData reloaded = ArenaSavedData.load(saved.save(new CompoundTag()));
        helper.assertTrue(reloaded.returns.get(a.getUUID()).getString("Quest").equals(key),"Restart ticket missing");
        PartyManager.leaveParty(a);
        helper.assertTrue(PartyQuestProgress.data(a).getQuestStatus(key) == PlayerQuestData.QuestStatus.FAILED,"Leaving did not fail own quest");
        for (LivingEntity enemy : enemies) QuestEvents.creditQuestKill(b,enemy);
        helper.assertTrue(PartyQuestProgress.data(b).isQuestCompleted(key),"Owner departure blocked kill credit");
        helper.assertTrue(a.getInventory().countItem(Items.APPLE) == 0 && b.getInventory().countItem(Items.APPLE) == 1,"Departed owner rewarded");
        helper.runAfterDelay(3, () -> {
            helper.assertTrue(b.level().dimension().equals(Level.OVERWORLD),"Remaining member stranded");
            for (var p : List.of(a,b)) { PartyManager.leaveParty(p); online(helper).remove(p.getUUID()); p.discard(); }
            helper.succeed();
        });
    }

    @GameTest(template = "cell_arena")
    public static void hudPacketAndArenaPersistence(GameTestHelper helper) {
        var member = new PartyHudS2C.Member(UUID.randomUUID(), "Vegeta", 2500, 9000, true, true, true, false, 0);
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        try {
            new PartyHudS2C(List.of(member)).encode(buf);
            helper.assertTrue(new PartyHudS2C(buf).members().equals(List.of(member)),"HUD packet roundtrip lost state");
        } finally { buf.release(); }
        ArenaSavedData data = new ArenaSavedData();
        int first = data.allocateSlot();
        ArenaSavedData loaded = ArenaSavedData.load(data.save(new CompoundTag()));
        helper.assertTrue(loaded.allocateSlot() > first,"Restart reused an old destroyed arena");
        var arena = helper.getLevel().getServer().getLevel(ArenaManager.DIMENSION);
        helper.assertTrue(arena != null,"Arena dimension JSON not loaded");
        helper.assertTrue(arena.getStructureManager().get(net.minecraft.resources.ResourceLocation.parse("dragonminez:cell_arena")).isPresent(),"Cell template missing");
        helper.succeed();
    }

    @GameTest(template = "cell_arena", batch = "recovery", timeoutTicks = 300)
    public static void requirementsDeathAndLogout(GameTestHelper helper) {
        ServerPlayer player = player(helper,"RecoveryPlayer");
        var own = PartyQuestProgress.data(player); own.completeQuest("test_gate");
        String key = "party_test_recovery"; quest(key);
        var origin = player.position();
        player.teleportTo(player.getServer().getLevel(Level.NETHER),0,90,0,0,0);
        helper.assertTrue(QuestService.startQuest(player,key) != null,"Original dimension requirement bypassed");
        helper.assertFalse(own.isQuestAccepted(key),"Rejected quest was accepted");
        helper.assertFalse(ArenaSavedData.get(player.getServer()).returns.containsKey(player.getUUID()),"Rejected start created a ticket");
        player.teleportTo(helper.getLevel(),origin.x,origin.y,origin.z,0,0);
        helper.assertTrue(QuestService.startQuest(player,key) == null,"Valid solo quest failed");
        player.setHealth(0);
        ArenaManager.abort(player);
        helper.assertTrue(own.getQuestStatus(key) == PlayerQuestData.QuestStatus.FAILED,"Death did not fail quest");
        helper.assertTrue(ArenaSavedData.get(player.getServer()).returns.containsKey(player.getUUID()),"Death lost return ticket");
        player.setHealth(20);
        ArenaManager.respawn(new net.minecraftforge.event.entity.player.PlayerEvent.PlayerRespawnEvent(player,false));
        helper.assertTrue(player.level().dimension().equals(Level.OVERWORLD),"Respawn stranded player");
        helper.assertTrue(QuestService.startQuest(player,key) == null,"Failed quest could not restart");
        ArenaManager.logout(new net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedOutEvent(player));
        helper.assertTrue(player.level().dimension().equals(Level.OVERWORLD) && own.getQuestStatus(key) == PlayerQuestData.QuestStatus.FAILED,"Logout did not return and fail player");
        helper.assertTrue(player.getInventory().countItem(Items.APPLE) == 0,"Failed run rewarded player");
        online(helper).remove(player.getUUID()); player.discard(); helper.succeed();
    }

    @GameTest(template = "cell_arena", batch = "collection")
    public static void combinedInventoryCompletesForBoth(GameTestHelper helper) {
        ServerPlayer a = player(helper,"GatherLeader"), b = player(helper,"GatherMember");
        PartyQuestProgress.data(a).completeQuest("test_gate"); PartyQuestProgress.data(b).completeQuest("test_gate");
        join(helper,a,b);
        String key = "party_test_items"; var resolved = quest(key);
        resolved.quest().getObjectives().clear();
        resolved.quest().getObjectives().add(new com.dragonminez.common.quest.objectives.ItemObjective(Items.APPLE,2));
        helper.assertTrue(QuestService.startQuest(b,key) == null,"Collection quest failed");
        helper.assertTrue(a.level().dimension().equals(Level.OVERWORLD),"Collection quest was incorrectly sent to arena");
        a.getInventory().add(new net.minecraft.world.item.ItemStack(Items.APPLE));
        b.getInventory().add(new net.minecraft.world.item.ItemStack(Items.APPLE));
        b.tickCount = 20;
        QuestEvents.onPlayerTick(new net.minecraftforge.event.TickEvent.PlayerTickEvent(net.minecraftforge.event.TickEvent.Phase.END,b));
        helper.assertTrue(PartyQuestProgress.data(a).isQuestCompleted(key) && PartyQuestProgress.data(b).isQuestCompleted(key),"Combined inventory did not complete for both");
        for (var p : List.of(a,b)) { PartyManager.leaveParty(p); online(helper).remove(p.getUUID()); p.discard(); }
        helper.succeed();
    }
}
