package com.dragonminez.common.network.S2C;

import com.dragonminez.client.gui.hud.PartyHUD;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import java.util.*;
import java.util.function.Supplier;

public record PartyHudS2C(List<Member> members) {
    public record Member(UUID id, String name, float health, float maxHealth, boolean online,
                         boolean leader, boolean arena, boolean sameDimension, int distance) {}
    public PartyHudS2C(FriendlyByteBuf buf) {
        this(buf.readList(b -> new Member(b.readUUID(), b.readUtf(64), b.readFloat(), b.readFloat(),
                b.readBoolean(), b.readBoolean(), b.readBoolean(), b.readBoolean(), b.readVarInt())));
    }
    public void encode(FriendlyByteBuf buf) {
        buf.writeCollection(members, (b, m) -> {
            b.writeUUID(m.id()); b.writeUtf(m.name(), 64); b.writeFloat(m.health()); b.writeFloat(m.maxHealth());
            b.writeBoolean(m.online()); b.writeBoolean(m.leader()); b.writeBoolean(m.arena());
            b.writeBoolean(m.sameDimension()); b.writeVarInt(m.distance());
        });
    }
    public void handle(Supplier<NetworkEvent.Context> supplier) {
        var context = supplier.get();
        context.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> PartyHUD.update(members)));
        context.setPacketHandled(true);
    }
}
