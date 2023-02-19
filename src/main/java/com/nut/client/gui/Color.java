package com.nut.client.gui;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Color {

    private float r;
    private float g;
    private float b;
    private float a;

    public Color(float r, float g, float b, float a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }
}
