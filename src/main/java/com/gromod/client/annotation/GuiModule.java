package com.gromod.client.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface GuiModule {
    public enum Category {
        Fps,
        Patching,
        Schematica,
        Other
    }
    Category category();
    String name();
    int index();
}
