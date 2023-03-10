package com.nut.client.utils;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;

public enum TextureType {

    TNT(UvCoords.tntTop, UvCoords.tntSide, UvCoords.tntBottom),
    SAND(UvCoords.sandTexture, UvCoords.sandTexture, UvCoords.sandTexture),
    REDSAND(UvCoords.redSandTexture, UvCoords.redSandTexture, UvCoords.redSandTexture),
    GRAVEL(UvCoords.gravelTexture, UvCoords.gravelTexture, UvCoords.gravelTexture),
    NONE(null);

    public final TextureAtlasSprite[] texture;

    TextureType(TextureAtlasSprite... texture) {
        this.texture = texture;
    }
}
