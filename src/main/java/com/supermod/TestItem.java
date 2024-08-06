package com.supermod;

import com.supermod.base.ItemBase;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class TestItem extends ItemBase {

    public TestItem(Properties properties) {
        super(properties);
    }

    public static RegistryObject<Item> register(DeferredRegister<Item> registry) {
        return registry.register("test_block", () -> new TestItem(new Item.Properties()));
    }
}
