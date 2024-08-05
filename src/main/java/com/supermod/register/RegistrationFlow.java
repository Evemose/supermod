package com.supermod.register;

import static com.supermod.ExampleMod.*;

import com.supermod.util.Reflection;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.NavigableSet;
import java.util.TreeSet;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
@SuppressWarnings("unused")
public class RegistrationFlow {

    private static final NavigableSet<? extends RegistrationManager> registrationManagers = registerManagers();

    private static NavigableSet<? extends RegistrationManager> registerManagers() {
        var managerSubtypes = Reflection.findSubtypesOf(RegistrationManager.class);

        var managers = new TreeSet<>(managerSubtypes.stream()
            .filter(clazz -> !Modifier.isAbstract(clazz.getModifiers()))
            .map(Reflection::createInstance)
            .toList());

        return Collections.unmodifiableNavigableSet(managers);
    }

    @SubscribeEvent
    public static void onLoad() {
        registrationManagers.forEach(RegistrationManager::registerAllOnClasspath);
    }
}
