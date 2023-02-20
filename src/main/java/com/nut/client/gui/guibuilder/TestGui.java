package com.nut.client.gui.guibuilder;

import com.nut.client.annotation.AutoInit;
import com.nut.client.annotation.Component;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

@Component
public class TestGui extends GuiScreen {

    @AutoInit
    public TestGui() {
    }

    @Override
    public void setWorldAndResolution(Minecraft mc, int width, int height) {
        super.setWorldAndResolution(mc, width, height);
    }
}
