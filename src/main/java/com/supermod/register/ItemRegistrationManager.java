package com.supermod.register;

import static com.supermod.ExampleMod.*;
import static com.supermod.register.CreativeTabsHolder.CREATIVE_MODE_TABS;

import com.supermod.base.ItemBase;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ItemRegistrationManager extends AbstractRegistrationManager<ItemBase> {

    public static final ItemRegistrationManager INSTANCE = new ItemRegistrationManager();

    private final List<RegistryObject<? extends ItemBase>> registeredItems = new ArrayList<>();

    private ItemRegistrationManager() {
        super(ItemBase.class, ForgeRegistries.ITEMS);
    }

    @SubscribeEvent
    public static void onLoad(final RegisterEvent event) {
        if (Objects.equals(event.getForgeRegistry(), ForgeRegistries.ITEMS)) {
            INSTANCE.registerAllOnClasspath();
        }
    }

    @SubscribeEvent
    public static void onConstructMod(final FMLConstructModEvent event) {
        INSTANCE.registry.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    @Override
    protected <R extends ItemBase> void performOnRegistered(RegistryObject<R> registered) {
        registeredItems.add(registered);
    }

    @Override
    protected void finalizeRegistration() {
        super.finalizeRegistration();
        CREATIVE_MODE_TABS.register("items", () -> CreativeModeTab.builder()
            .icon(() -> registeredItems.getFirst().get().getDefaultInstance())
            .displayItems((parameters, output) -> registeredItems.forEach(item -> output.accept(item.get())))
            .build()
        );
    }

    @Override
    public long priority() {
        return 1;
    }
}
