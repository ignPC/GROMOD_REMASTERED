package com.nut.client.utils;

import net.minecraft.entity.Entity;

public class MathUtils {

    public static Vector3d interpolateCoords(Entity entity, float partialTicks) {
        return new Vector3d(
                interpolate(entity.posX, entity.lastTickPosX, partialTicks),
                interpolate(entity.posY, entity.lastTickPosY, partialTicks),
                interpolate(entity.posZ, entity.lastTickPosZ, partialTicks)
        );
    }

    public static double interpolate(double currentPos, double lastTickPos, float partialTicks) {
        return lastTickPos + (currentPos - lastTickPos) * partialTicks;
    }
}
