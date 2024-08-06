package com.supermod.base;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;

public abstract class MobBase extends Mob implements Registrable {
    public MobBase(EntityType<? extends Mob> type, Level level) {
        super(type, level);
    }
}
