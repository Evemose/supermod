package com.supermod.register;

import static com.supermod.ExampleMod.*;

import com.supermod.base.BlockBase;
import java.util.Objects;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;

@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class BlockRegistrationManager extends AbstractRegistrationManager<BlockBase> {

    public static final BlockRegistrationManager INSTANCE = new BlockRegistrationManager();

    private BlockRegistrationManager() {
        super(BlockBase.class, ForgeRegistries.BLOCKS);
    }

    @SubscribeEvent
    public static void onLoad(final RegisterEvent event) {
        if (Objects.equals(event.getForgeRegistry(), ForgeRegistries.BLOCKS)) {
            INSTANCE.registerAllOnClasspath();
        }
    }

    @SubscribeEvent
    public static void onConstructMod(final FMLConstructModEvent event) {
        INSTANCE.registry.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    @Override
    public long priority() {
        return 0;
    }
}
