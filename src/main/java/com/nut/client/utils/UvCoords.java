package com.nut.client.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;

public class UvCoords {

    private static final TextureMap textureMap = Minecraft.getMinecraft().getTextureMapBlocks();
    public static final TextureAtlasSprite tntTop = textureMap.getAtlasSprite("minecraft:blocks/tnt_top");
    public static final TextureAtlasSprite tntSide = textureMap.getAtlasSprite("minecraft:blocks/tnt_side");
    public static final TextureAtlasSprite tntBottom = textureMap.getAtlasSprite("minecraft:blocks/tnt_bottom");
    public static final TextureAtlasSprite sandTexture = textureMap.getAtlasSprite("minecraft:blocks/sand");
    public static final TextureAtlasSprite gravelTexture = textureMap.getAtlasSprite("minecraft:blocks/gravel");
    public static final TextureAtlasSprite redSandTexture = textureMap.getAtlasSprite("minecraft:blocks/red_sand");
}
