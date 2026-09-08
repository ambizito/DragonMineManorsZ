package com.dragonminez.server.events;

import com.dragonminez.Reference;
import com.dragonminez.common.network.NetworkHandler;
import com.dragonminez.common.network.S2C.PartyHudS2C;
import com.dragonminez.server.world.data.PartySavedData;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import java.util.*;

@Mod.EventBusSubscriber(modid = Reference.MOD_ID)
public final class PartyHudEvents {
    @SubscribeEvent public static void tick(TickEvent.ServerTickEvent event) {
        if (event.phase != TickEvent.Phase.END || event.getServer().getTickCount() % 10 != 0) return;
        var server = event.getServer();
        for (var viewer : server.getPlayerList().getPlayers()) {
            var party = PartySavedData.get(server).getPartyOf(viewer.getUUID());
            List<PartyHudS2C.Member> members = new ArrayList<>();
            if (party != null) for (UUID id : party.getMembers()) {
                var player = server.getPlayerList().getPlayer(id);
                String name = player != null ? player.getGameProfile().getName()
                        : server.getProfileCache().get(id).map(p -> p.getName()).orElse(id.toString().substring(0, 8));
                boolean same = player != null && player.level() == viewer.level();
                members.add(new PartyHudS2C.Member(id, name, player == null ? 0 : player.getHealth(),
                        player == null ? 1 : player.getMaxHealth(), player != null, id.equals(party.getLeaderId()),
                        player != null && ArenaManager.inArena(player), same,
                        same ? (int) viewer.distanceTo(player) : 0));
            }
            members.sort(Comparator.comparing(PartyHudS2C.Member::leader).reversed().thenComparing(PartyHudS2C.Member::name));
            NetworkHandler.sendToPlayer(new PartyHudS2C(members), viewer);
        }
    }
}
