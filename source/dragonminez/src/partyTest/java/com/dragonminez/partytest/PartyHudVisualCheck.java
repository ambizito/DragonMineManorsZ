package com.dragonminez.partytest;

import com.dragonminez.client.gui.hud.PartyHUD;
import com.dragonminez.common.network.S2C.PartyHudS2C.Member;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Screenshot;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import java.util.List;
import java.util.UUID;

/** Opt-in render check in an isolated development world, using fixture HUD data. */
@Mod.EventBusSubscriber(modid = "dragonminez", value = Dist.CLIENT)
public final class PartyHudVisualCheck {
    private static int ticks;
    private static boolean saved;
    @SubscribeEvent public static void tick(TickEvent.ClientTickEvent event) {
        if (!Boolean.getBoolean("dmz.partyHudPreview") || event.phase != TickEvent.Phase.END) return;
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.level == null) return;
        ticks++;
        mc.setScreen(null);
        mc.options.hideGui = false;
        PartyHUD.update(List.of(
                new Member(mc.player.getUUID(), "Goku", 12400, 15000, true, true, true, true, 0),
                new Member(UUID.fromString("00000000-0000-0000-0000-000000000002"), "Vegeta", 1850, 12000, true, false, true, true, 12),
                new Member(UUID.fromString("00000000-0000-0000-0000-000000000003"), "Piccolo", 7000, 9000, true, false, false, false, 0),
                new Member(UUID.fromString("00000000-0000-0000-0000-000000000004"), "Gohan", 0, 9000, false, false, false, false, 0)));
        if (ticks >= 100 && !saved) {
            saved = true;
            Screenshot.grab(mc.gameDirectory, "party-hud-verification.png", mc.getMainRenderTarget(), message -> {});
        }
        if (ticks > 140) mc.stop();
    }
}
