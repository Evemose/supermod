package com.supermod.register;

import static com.supermod.ExampleMod.*;

import com.supermod.base.Registrable;
import com.supermod.util.Reflection;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Modifier;
import lombok.SneakyThrows;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryObject;

public abstract class AbstractRegistrationManager<T extends Registrable> implements RegistrationManager {

    private final Class<T> baseType;

    protected final DeferredRegister<? super T> registry;

    protected AbstractRegistrationManager(Class<T> baseType, IForgeRegistry<? super T> registries) {
        this.baseType = baseType;
        this.registry = DeferredRegister.create(registries, MODID);
    }

    @Override
    public void registerAllOnClasspath() {
        Reflection.findSubtypesOf(baseType).stream()
            .filter(clazz -> !Modifier.isAbstract(clazz.getModifiers()))
            .map(this::register)
            .forEach(this::performOnRegistered);
        finalizeRegistration();
    }
    
    @SneakyThrows
    @SuppressWarnings("unchecked")
    private RegistryObject<? extends T> register(Class<? extends T> clazz) {
        var handle = MethodHandles.lookup().findStatic(clazz, "register", MethodType.methodType(RegistryObject.class, DeferredRegister.class));
        return (RegistryObject<? extends T>) handle.invoke(registry);
    }

    protected <R extends T> void performOnRegistered(RegistryObject<R> registered) {
    }

    protected void finalizeRegistration() {
        registry.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}
