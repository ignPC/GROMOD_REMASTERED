package com.gromod.client.utils;

import lombok.Getter;

@Getter
public class Scaled {

    private float xScale = 1;
    private float yScale = 1;

    public void set(float xScale, float yScale) {
        this.xScale = xScale;
        this.yScale = yScale;
    }
}
