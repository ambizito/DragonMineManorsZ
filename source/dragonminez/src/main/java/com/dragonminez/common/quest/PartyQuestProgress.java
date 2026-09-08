package com.dragonminez.common.quest;

import com.dragonminez.common.events.DMZEvent;
import com.dragonminez.common.network.NetworkHandler;
import com.dragonminez.common.network.S2C.ProgressionSyncS2C;
import com.dragonminez.common.network.S2C.StoryToastS2C;
import com.dragonminez.common.stats.*;
import com.dragonminez.server.events.ArenaManager;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.MinecraftForge;
import java.util.*;

/** Shares only the current quest. History, unlocks and reward receipts remain personal. */
public final class PartyQuestProgress {
    private PartyQuestProgress() {}

    public static PlayerQuestData data(ServerPlayer player) {
        return StatsProvider.get(StatsCapability.INSTANCE, player).resolve()
                .map(StatsData::getPlayerQuestData).orElse(null);
    }

    public static boolean eligible(ServerPlayer player, QuestService.ResolvedQuest resolved) {
        StatsData stats = StatsProvider.get(StatsCapability.INSTANCE, player).resolve().orElse(null);
        if (stats == null || !player.isAlive() || player.isSpectator()) return false;
        PlayerQuestData own = stats.getPlayerQuestData();
        if (own.isQuestCompleted(resolved.questKey())) return false;
        if (own.isQuestAccepted(resolved.questKey())) return true;
        Quest quest = resolved.quest();
        if (resolved.saga() != null && !QuestAvailabilityChecker.isSagaQuestAvailable(
                quest, resolved.saga(), resolved.saga().getQuests().indexOf(quest), stats)) return false;
        return QuestAvailabilityChecker.describeNonPositionalStartBlocker(
                quest, resolved.questKey(), player, stats) == null;
    }

    public static List<ServerPlayer> accept(List<ServerPlayer> members, QuestService.ResolvedQuest resolved,
                                           Difficulty difficulty) {
        // Snapshot eligibility before any progress changes.
        List<ServerPlayer> eligible = members.stream().filter(p -> eligible(p, resolved)).toList();
        for (ServerPlayer member : eligible) {
            PlayerQuestData own = data(member);
            own.acceptQuest(resolved.questKey());
            resolved.quest().initializeObjectiveRequirements(own, resolved.questKey(), members.size());
            own.setQuestDifficulty(resolved.questKey(), difficulty);
            own.setTrackedQuestId(resolved.questKey());
        }
        return eligible;
    }

    public static void complete(ServerPlayer source, String questKey) {
        QuestService.ResolvedQuest resolved = QuestService.resolveQuest(questKey);
        PlayerQuestData progress = data(source);
        if (resolved == null || progress == null || !progress.isQuestAccepted(questKey)) return;
        List<ServerPlayer> members = ArenaManager.questMembers(source);
        for (ServerPlayer member : members) {
            PlayerQuestData own = data(member);
            if (own == null || !own.isQuestAccepted(questKey)
                    || !ArenaManager.canProgress(member, questKey)) continue;
            if (MinecraftForge.EVENT_BUS.post(new DMZEvent.QuestCompletedEvent(member, questKey,
                    resolved.saga(), resolved.quest(), members))) continue;
            for (int i = 0; i < resolved.quest().getObjectives().size(); i++) {
                own.setObjectiveProgress(questKey, i, resolved.quest().getObjectiveRequired(own, questKey, i));
            }
            own.completeQuest(questKey);
            if (questKey.equals(own.getTrackedQuestId())) own.setTrackedQuestId(null);
            NetworkHandler.sendToPlayer(StoryToastS2C.questComplete(questKey), member);
            // Existing reward service retains NPC-only rules and individual, persisted receipts.
            if (resolved.quest().getClaimMode() != Quest.ClaimMode.NPC_ONLY) QuestService.claimRewards(member, questKey);
            NetworkHandler.sendToPlayer(new ProgressionSyncS2C(member), member);
        }
        ArenaManager.onQuestFinished(source, questKey);
    }
}
