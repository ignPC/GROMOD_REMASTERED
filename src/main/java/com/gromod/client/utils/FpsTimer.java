package com.gromod.client.utils;

import com.gromod.client.annotation.AutoInit;
import com.gromod.client.annotation.Component;

import java.awt.*;

@Component
public class FpsTimer {

    public static boolean skipRender = true;
    private static long lastFrameTime;
    private static long TIME_PER_FRAME;

    @AutoInit
    public FpsTimer() {
        getHighestMonitorRefreshRate();
    }

    private void getHighestMonitorRefreshRate() {
        GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] devices = env.getScreenDevices();

        int rate = 0;
        for (GraphicsDevice device : devices) {
            DisplayMode displayMode = device.getDisplayMode();
            if (displayMode.getRefreshRate() > rate)
                rate = displayMode.getRefreshRate();
        }
        TIME_PER_FRAME = 1000000000 / rate;
    }

    public static boolean pollTimer() {
        long currentFrameTime = System.nanoTime();
        if (lastFrameTime == 0) {
            lastFrameTime = currentFrameTime;
            skipRender = false;
            return false;
        }
        long deltaFrameTime = currentFrameTime - lastFrameTime;
        if (deltaFrameTime >= TIME_PER_FRAME) {
            lastFrameTime = currentFrameTime;
            skipRender = false;
            return false;
        }
        skipRender = true;
        return true;
    }
}
