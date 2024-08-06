package com.supermod.register;

import static com.supermod.ExampleMod.*;

import lombok.experimental.UtilityClass;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegisterEvent;

@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
@UtilityClass
public class CreativeTabsHolder {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    private static boolean registered = false;

    @SubscribeEvent
    public static void finalizeRegistration(final RegisterEvent event) {
        if (registered) {
            return;
        }
        CREATIVE_MODE_TABS.register(FMLJavaModLoadingContext.get().getModEventBus());
        registered = true;
    }
}
