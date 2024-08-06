package com.supermod.base;

import net.minecraft.world.item.Item;

public abstract class ItemBase extends Item implements Registrable {
    public ItemBase(Properties properties) {
        super(properties);
    }
}
