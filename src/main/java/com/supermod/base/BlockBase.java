package com.supermod.base;

import net.minecraft.world.level.block.Block;

public abstract class BlockBase extends Block implements Creatable, Base {
    public BlockBase(Properties properties) {
        super(properties);
    }
}
