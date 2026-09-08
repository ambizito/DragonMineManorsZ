package com.dragonminez.client.gui.hud;

import com.dragonminez.Reference;
import com.dragonminez.common.network.S2C.PartyHudS2C.Member;
import com.dragonminez.client.util.KeyBinds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import java.util.*;

@Mod.EventBusSubscriber(modid = Reference.MOD_ID, value = Dist.CLIENT)
public final class PartyHUD {
    private static List<Member> members = List.of();
    private static long updated;
    public static void update(List<Member> snapshot) { members = List.copyOf(snapshot); updated = System.currentTimeMillis(); }
    @SubscribeEvent public static void logout(ClientPlayerNetworkEvent.LoggingOut event) { members = List.of(); }
    public static final IGuiOverlay HUD = (gui, graphics, partialTick, width, height) -> {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.options.hideGui || mc.options.renderDebug || mc.screen instanceof ChatScreen
                || members.isEmpty() || System.currentTimeMillis() - updated > 5000) return;
        // Compact bottom-left cards; reserve the bottom strip for the hotbar/techniques.
        float scale = Math.min(0.85f, width / 600f);
        int available = Math.max(1, (int) ((height - 135) / (36 * scale)));
        int count = Math.min(members.size(), available);
        int panelHeight = 21 + count * 36;
        int x = 10, y = (int) ((height - 58) / scale) - panelHeight;
        graphics.pose().pushPose();
        graphics.pose().scale(scale, scale, 1);
        graphics.fill(x, y, x + 184, y + panelHeight, 0xD9101725);
        graphics.fill(x, y, x + 184, y + 2, 0xFFFFB744);
        graphics.drawString(mc.font, Component.translatable("gui.dragonminez.party.hud.title", members.size()), x + 7, y + 7, 0xFFFFC96A, false);
        String key = KeyBinds.STATS_TAB_PARTY.getTranslatedKeyMessage().getString();
        graphics.drawString(mc.font, key, x + 177 - mc.font.width(key), y + 7, 0xFFADBED4, false);
        int first = members.size() <= count ? 0 : (int) ((mc.player.tickCount / 100L) % ((members.size() + count - 1) / count)) * count;
        for (int i = 0; i < count && first + i < members.size(); i++) draw(graphics, members.get(first + i), x, y + 21 + i * 36);
        graphics.pose().popPose();
    };
    private static void draw(GuiGraphics g, Member member, int x, int y) {
        var mc = Minecraft.getInstance();
        int accent = !member.online() ? 0xFF66758A : member.health() <= 0 ? 0xFFE66666 : 0xFF58DAB1;
        g.fill(x + 5, y, x + 179, y + 32, 0xCC243247);
        g.fill(x + 5, y, x + 7, y + 32, accent);
        String name = (member.leader() ? "★ " : "") + member.name();
        g.drawString(mc.font, mc.font.plainSubstrByWidth(name, 160), x + 12, y + 3, 0xFFF2F6FC, false);
        float fraction = member.online() ? Mth.clamp(member.health() / Math.max(1, member.maxHealth()), 0, 1) : 0;
        g.fill(x + 12, y + 15, x + 172, y + 19, 0xFF121A29);
        g.fill(x + 12, y + 15, x + 12 + (int) (160 * fraction), y + 19, fraction < 0.25f ? 0xFFEE7373 : accent);
        String health = member.online() ? compact(member.health()) + " / " + compact(member.maxHealth()) : "--";
        String state = !member.online() ? "offline" : member.health() <= 0 ? "down" : member.arena() ? "arena"
                : !member.sameDimension() ? "dimension" : "near";
        String label = state.equals("near") ? member.distance() + "m" : Component.translatable("gui.dragonminez.party.hud." + state).getString();
        g.drawString(mc.font, health, x + 12, y + 22, 0xFFCAD8EA, false);
        g.drawString(mc.font, label, x + 172 - mc.font.width(label), y + 22, 0xFFADBED4, false);
    }
    private static String compact(float value) {
        if (value >= 1_000_000) return String.format(Locale.ROOT, "%.1fM", value / 1_000_000);
        if (value >= 10_000) return String.format(Locale.ROOT, "%.1fk", value / 1000);
        return Long.toString(Math.round(value));
    }
}
