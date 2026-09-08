package com.dragonminez.server.events;

import com.dragonminez.Reference;
import com.dragonminez.common.quest.*;
import com.dragonminez.common.quest.objectives.KillObjective;
import com.dragonminez.common.network.NetworkHandler;
import com.dragonminez.common.network.S2C.ProgressionSyncS2C;
import com.dragonminez.server.world.data.ArenaSavedData;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.templatesystem.*;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.server.ServerStoppedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import java.util.*;

@Mod.EventBusSubscriber(modid = Reference.MOD_ID)
public final class ArenaManager {
    public static final ResourceKey<Level> DIMENSION = ResourceKey.create(Registries.DIMENSION,
            ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "quest_arena"));
    private static final Map<UUID, Session> sessions = new HashMap<>();
    private static final int RADIUS = 192;
    private static final long TIMEOUT = 20L * 60 * 30;
    private ArenaManager() {}

    private static final class Session {
        final String quest;
        final List<UUID> members;
        final Set<UUID> eligible;
        final BlockPos center;
        final long started;
        boolean finished;
        Session(String quest, List<ServerPlayer> members, List<ServerPlayer> eligible, BlockPos center, long started) {
            this.quest = quest;
            this.members = members.stream().map(Entity::getUUID).toList();
            this.eligible = new HashSet<>(eligible.stream().map(Entity::getUUID).toList());
            this.center = center;
            this.started = started;
        }
    }

    public static boolean needsArena(Quest quest) {
        return quest.getObjectives().stream().anyMatch(o -> o instanceof KillObjective k
                && k.getSpawnMode() == KillObjective.SpawnMode.QUEST);
    }

    public static Component checkEntry(List<ServerPlayer> members, Quest quest) {
        if (!needsArena(quest)) return null;
        if (members.get(0).getServer().getLevel(DIMENSION) == null)
            return Component.translatable("message.dragonminez.arena.unavailable");
        for (ServerPlayer member : members) {
            if (sessions.containsKey(member.getUUID()) || ArenaSavedData.get(member.getServer()).returns.containsKey(member.getUUID()))
                return Component.translatable("message.dragonminez.arena.busy", member.getGameProfile().getName());
            if (!member.isAlive() || member.isSpectator())
                return Component.translatable("message.dragonminez.arena.not_ready", member.getGameProfile().getName());
        }
        return null;
    }

    public static void enter(ServerPlayer source, QuestService.ResolvedQuest resolved,
                             List<ServerPlayer> members, List<ServerPlayer> eligible) {
        if (!needsArena(resolved.quest())) return;
        MinecraftServer server = source.getServer();
        ServerLevel arena = Objects.requireNonNull(server.getLevel(DIMENSION));
        ArenaSavedData saved = ArenaSavedData.get(server);
        int slot = saved.allocateSlot();
        BlockPos center = new BlockPos((slot % 1000) * 1024, 64, (slot / 1000) * 1024);
        // Use the original Cell structure, without the NPCs/structure markers from world generation.
        Optional<StructureTemplate> cell = arena.getStructureManager().get(
                ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "cell_arena"));
        cell.ifPresent(template -> {
            BlockPos origin = center.offset(-template.getSize().getX() / 2, 0, -template.getSize().getZ() / 2);
            template.placeInWorld(arena, origin, origin, new StructurePlaceSettings().setIgnoreEntities(true)
                    .addProcessor(BlockIgnoreProcessor.STRUCTURE_BLOCK), arena.random, 2);
        });
        Session session = new Session(resolved.questKey(), members, eligible, center, server.overworld().getGameTime());
        for (ServerPlayer member : members) {
            CompoundTag ticket = new CompoundTag();
            ticket.putUUID("Player", member.getUUID());
            ticket.putString("Dimension", member.level().dimension().location().toString());
            ticket.putDouble("X", member.getX()); ticket.putDouble("Y", member.getY()); ticket.putDouble("Z", member.getZ());
            ticket.putFloat("Yaw", member.getYRot()); ticket.putFloat("Pitch", member.getXRot());
            ticket.putString("Quest", resolved.questKey());
            ticket.putBoolean("Eligible", session.eligible.contains(member.getUUID()));
            saved.returns.put(member.getUUID(), ticket);
            sessions.put(member.getUUID(), session);
        }
        saved.setDirty();
        server.overworld().getDataStorage().save(); // Write return tickets before moving anyone.
        int index = 0;
        for (ServerPlayer member : members) {
            int x = center.getX() + (index++ % 8) * 2 - 6;
            int z = center.getZ() + 6 + (index / 8) * 2;
            int y = arena.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x, z);
            member.stopRiding();
            member.teleportTo(arena, x + 0.5, y + 1, z + 0.5, 180, 0);
            member.fallDistance = 0;
            member.sendSystemMessage(Component.translatable(session.eligible.contains(member.getUUID())
                    ? "message.dragonminez.arena.enter" : "message.dragonminez.arena.support"));
        }
    }

    public static List<ServerPlayer> questMembers(ServerPlayer source) {
        Session session = sessions.get(source.getUUID());
        if (session == null) return PartyManager.getAllPartyMembers(source).stream()
                .filter(p -> !sessions.containsKey(p.getUUID()) && p.isAlive() && !p.isSpectator()).toList();
        return session.members.stream().map(id -> source.getServer().getPlayerList().getPlayer(id))
                .filter(Objects::nonNull).filter(p -> present(p, session)).toList();
    }

    private static boolean present(ServerPlayer player, Session session) {
        return sessions.get(player.getUUID()) == session && player.isAlive() && !player.isSpectator()
                && player.level().dimension().equals(DIMENSION)
                && Math.abs(player.getX() - session.center.getX()) <= RADIUS
                && Math.abs(player.getZ() - session.center.getZ()) <= RADIUS;
    }

    public static boolean canProgress(ServerPlayer player, String quest) {
        Session session = sessions.get(player.getUUID());
        return session == null ? !player.level().dimension().equals(DIMENSION)
                : session.quest.equals(quest) && session.eligible.contains(player.getUUID()) && present(player, session);
    }

    public static boolean inArena(ServerPlayer player) { return sessions.containsKey(player.getUUID()); }

    public static boolean ownsQuestEnemy(List<ServerPlayer> members, String owner, String quest) {
        for (ServerPlayer member : members) {
            Session session = sessions.get(member.getUUID());
            if (session != null && session.quest.equals(quest)
                    && session.members.stream().anyMatch(id -> id.toString().equals(owner))) return true;
        }
        return false;
    }

    public static void onQuestFinished(ServerPlayer player, String quest) {
        Session session = sessions.get(player.getUUID());
        if (session != null && session.quest.equals(quest)) session.finished = true;
    }

    public static void abort(ServerPlayer player) {
        Session session = sessions.remove(player.getUUID());
        CompoundTag ticket = ArenaSavedData.get(player.getServer()).returns.get(player.getUUID());
        if (ticket != null && ticket.getBoolean("Eligible")) {
            PlayerQuestData own = PartyQuestProgress.data(player);
            if (own != null && own.isQuestAccepted(ticket.getString("Quest"))) own.failQuest(ticket.getString("Quest"));
        }
        if (player.isAlive()) returnPlayer(player);
        if (session != null && session.members.stream().noneMatch(id -> sessions.get(id) == session)) cleanup(player.getServer(), session);
    }

    private static void returnPlayer(ServerPlayer player) {
        ArenaSavedData saved = ArenaSavedData.get(player.getServer());
        CompoundTag ticket = saved.returns.get(player.getUUID());
        if (ticket == null || !player.isAlive()) return;
        ServerLevel target = player.getServer().getLevel(ResourceKey.create(Registries.DIMENSION,
                ResourceLocation.parse(ticket.getString("Dimension"))));
        double x = ticket.getDouble("X"), y = ticket.getDouble("Y"), z = ticket.getDouble("Z");
        if (target == null || target.dimension().equals(DIMENSION)) {
            target = player.getServer().overworld();
            BlockPos spawn = target.getSharedSpawnPos();
            x = spawn.getX() + 0.5; y = spawn.getY() + 1; z = spawn.getZ() + 0.5;
        }
        player.stopRiding();
        player.teleportTo(target, x, y, z, ticket.getFloat("Yaw"), ticket.getFloat("Pitch"));
        player.fallDistance = 0;
        saved.returns.remove(player.getUUID()); saved.setDirty();
        NetworkHandler.sendToPlayer(new ProgressionSyncS2C(player), player);
        player.sendSystemMessage(Component.translatable("message.dragonminez.arena.return"));
    }

    private static void cleanup(MinecraftServer server, Session session) {
        ServerLevel arena = server.getLevel(DIMENSION);
        if (arena == null) return;
        AABB bounds = new AABB(session.center).inflate(RADIUS, 256, RADIUS);
        for (Entity entity : arena.getEntities((Entity) null, bounds,
                e -> !(e instanceof ServerPlayer) && e.getPersistentData().contains(QuestService.QUEST_KEY_TAG))) entity.discard();
    }

    @SubscribeEvent public static void tick(TickEvent.ServerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        MinecraftServer server = event.getServer();
        for (Session session : new HashSet<>(sessions.values())) {
            List<ServerPlayer> active = session.members.stream().map(id -> server.getPlayerList().getPlayer(id))
                    .filter(Objects::nonNull).filter(p -> sessions.get(p.getUUID()) == session).toList();
            // Return after the battle even when an NPC turn-in or another world objective remains.
            boolean battleDone = active.stream().filter(p -> session.eligible.contains(p.getUUID())).anyMatch(p -> {
                Quest quest = QuestRegistry.getQuest(session.quest);
                PlayerQuestData own = PartyQuestProgress.data(p);
                if (quest == null || own == null) return false;
                if (own.isQuestCompleted(session.quest)) return true;
                for (int i = 0; i < quest.getObjectives().size(); i++) {
                    if (quest.getObjectives().get(i) instanceof KillObjective k && k.getSpawnMode() == KillObjective.SpawnMode.QUEST
                            && own.getObjectiveProgress(session.quest, i) < quest.getObjectiveRequired(own, session.quest, i)) return false;
                }
                return true;
            });
            boolean timedOut = server.overworld().getGameTime() - session.started >= TIMEOUT
                    || active.stream().noneMatch(p -> session.eligible.contains(p.getUUID()) && present(p, session));
            for (ServerPlayer player : active) {
                if (!present(player, session) || timedOut) abort(player);
                else if (session.finished || battleDone) {
                    sessions.remove(player.getUUID());
                    returnPlayer(player);
                }
            }
            if (session.members.stream().noneMatch(id -> sessions.get(id) == session)) cleanup(server, session);
        }
        if (server.getTickCount() % 20 == 0) {
            for (ServerPlayer player : server.getPlayerList().getPlayers()) {
                if (player.level().dimension().equals(DIMENSION) && !sessions.containsKey(player.getUUID()) && player.isAlive()) {
                    if (ArenaSavedData.get(server).returns.containsKey(player.getUUID())) returnPlayer(player);
                    else {
                        BlockPos spawn = server.overworld().getSharedSpawnPos();
                        player.teleportTo(server.overworld(), spawn.getX() + 0.5, spawn.getY() + 1, spawn.getZ() + 0.5, 0, 0);
                    }
                }
            }
        }
    }

    @SubscribeEvent public static void logout(PlayerEvent.PlayerLoggedOutEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) abort(player);
    }
    @SubscribeEvent public static void login(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer player && ArenaSavedData.get(player.getServer()).returns.containsKey(player.getUUID())) abort(player);
    }
    @SubscribeEvent public static void respawn(PlayerEvent.PlayerRespawnEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) abort(player);
    }
    @SubscribeEvent public static void stopped(ServerStoppedEvent event) { sessions.clear(); }
    @SubscribeEvent public static void commands(RegisterCommandsEvent event) {
        event.getDispatcher().register(Commands.literal("dmzarena").then(Commands.literal("sair").executes(ctx -> {
            abort(ctx.getSource().getPlayerOrException()); return 1;
        })));
    }
}
