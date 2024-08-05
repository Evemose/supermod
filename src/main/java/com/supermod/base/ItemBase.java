package com.supermod.base;

import net.minecraft.world.item.Item;

public abstract class ItemBase extends Item implements Creatable {
    public ItemBase(Properties properties) {
        super(properties);
    }
}

class Test extends ItemBase {
    public Test() {
        super(new Item.Properties());
    }
}
