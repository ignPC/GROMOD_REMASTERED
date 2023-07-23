package com.gromod.client;

import com.gromod.client.annotation.AutoInit;
import com.gromod.client.annotation.Component;
import com.gromod.client.annotation.GuiModule;
import com.gromod.client.gui.TestGui;
import com.gromod.client.settings.LoadSettings;
import net.minecraft.client.Minecraft;
import org.reflections.Reflections;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Set;

public class Loader {

    private final Reflections reflections = new Reflections("com.gromod.client");
    private final HashMap<Class<?>, Object> clazz2ObjectMap = new HashMap<>();

    public Loader() throws InstantiationException, IllegalAccessException, InvocationTargetException {
        fillClazz2ObjectMap();

        // Initialize classes annotated with GuiModule first
        Set<Class<?>> guiModuleClasses = reflections.getTypesAnnotatedWith(GuiModule.class);
        for (Class<?> clazz : guiModuleClasses) {
            initClass(clazz);
        }

        // Initialize the remaining classes
        Set<Class<?>> componentClasses = reflections.getTypesAnnotatedWith(Component.class);
        for (Class<?> clazz : componentClasses) {
            if (!guiModuleClasses.contains(clazz)) {
                initClass(clazz);
            }
        }
    }

    public Object initClass(Class<?> clazz) throws InvocationTargetException, InstantiationException, IllegalAccessException {
        if (clazz.getAnnotation(Component.class) == null) return null;
        Constructor<?>[] constructors = clazz.getConstructors();
        for (Constructor<?> constructor : constructors) {
            if (constructor.getAnnotation(AutoInit.class) == null) continue;
            return initConstructor(constructor);
        }
        return null;
    }

    public Object initConstructor(Constructor<?> constructor) throws InvocationTargetException, InstantiationException, IllegalAccessException {
        Parameter[] parameters = constructor.getParameters();
        if (parameters.length == 0)
            return constructor.newInstance();
        Object[] instantiatedParams = new Object[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            Class<?> paramClazz = parameters[i].getType();
            clazz2ObjectMap.computeIfAbsent(paramClazz, k -> {
                try {
                    Object object = initClass(paramClazz);
                    if (object == null)
                        throw new RuntimeException("Could not instantiate: " + paramClazz);
                    return object;
                } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            });
            instantiatedParams[i] = clazz2ObjectMap.get(paramClazz);
        }
        return constructor.newInstance(instantiatedParams);
    }

    public void fillClazz2ObjectMap() {
        clazz2ObjectMap.put(Minecraft.class, Minecraft.getMinecraft());
    }
}
