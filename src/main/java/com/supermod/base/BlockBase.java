package com.supermod.base;

import net.minecraft.world.level.block.Block;

public abstract class BlockBase extends Block implements Registrable {
    public BlockBase(Properties properties) {
        super(properties);
    }
}
