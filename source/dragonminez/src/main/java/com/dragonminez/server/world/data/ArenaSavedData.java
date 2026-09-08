package com.dragonminez.server.world.data;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.saveddata.SavedData;
import java.util.*;

/** Durable return tickets survive death, logout and server restarts. Slots are never reused. */
public final class ArenaSavedData extends SavedData {
    public final Map<UUID, CompoundTag> returns = new HashMap<>();
    private int nextSlot;
    public static ArenaSavedData get(MinecraftServer server) {
        return server.overworld().getDataStorage().computeIfAbsent(ArenaSavedData::load,
                ArenaSavedData::new, "dragonminez_arena_returns");
    }
    public int allocateSlot() {
        if (nextSlot >= 1_000_000) throw new IllegalStateException("Arena capacity exhausted");
        setDirty();
        return nextSlot++;
    }
    public static ArenaSavedData load(CompoundTag tag) {
        ArenaSavedData data = new ArenaSavedData();
        data.nextSlot = tag.getInt("NextSlot");
        ListTag list = tag.getList("Returns", Tag.TAG_COMPOUND);
        for (int i = 0; i < list.size(); i++) {
            CompoundTag entry = list.getCompound(i);
            data.returns.put(entry.getUUID("Player"), entry);
        }
        return data;
    }
    @Override public CompoundTag save(CompoundTag tag) {
        tag.putInt("NextSlot", nextSlot);
        ListTag list = new ListTag();
        returns.values().forEach(entry -> list.add(entry.copy()));
        tag.put("Returns", list);
        return tag;
    }
}
