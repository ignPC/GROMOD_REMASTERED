package com.nut.client;

import com.nut.client.annotation.AutoInit;
import com.nut.client.annotation.BModule;
import com.nut.client.annotation.Component;
import net.minecraft.client.Minecraft;
import org.reflections.Reflections;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Set;

public class Loader {

    private final Reflections reflections = new Reflections("com.nut.client");
    private final HashMap<Class<?>, Object> clazz2ObjectMap = new HashMap<>();

    public Loader() throws InstantiationException, IllegalAccessException, InvocationTargetException {
        fillClazz2ObjectMap();

        Set<Class<?>> classes = reflections.getTypesAnnotatedWith(Component.class);
        for (Class<?> clazz : classes)
            initClass(clazz);
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

    public void initModule(Class<?> module){
        BModule annotation = module.getAnnotation(BModule.class);
        String moduleName = annotation.name();
        addModuleToGui(moduleName);
    }

    private void addModuleToGui(String moduleName) {

    }
}
