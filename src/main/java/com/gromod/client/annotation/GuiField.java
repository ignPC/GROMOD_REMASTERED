package com.gromod.client.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface GuiField {
    public enum Type {
        BUTTON,
        SLIDER,
        TEXT_INPUT
    }

    Type type();
    String label() default "";
    double min() default 0;
    double max() default 1;
}
