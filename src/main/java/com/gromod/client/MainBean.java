package com.gromod.client;

import com.gromod.client.fpstest.FpsTest;
import com.gromod.client.gui.TestGui;
import com.gromod.client.renderer.entity.CustomEntityLoader;
import com.gromod.client.renderer.font.CustomFont;
import com.gromod.client.renderer.font.FontAtlasBuilder;
import com.gromod.client.renderer.image.ImageLoader;
import com.gromod.client.settings.LoadSettings;
import com.gromod.client.settings.SaveSettings;
import com.gromod.client.utils.MessageUtils;
import com.gromod.client.utils.RenderUtils;
import lombok.SneakyThrows;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;

@Mod(modid = "Gromod", version = "Pre-Alpha")
public class MainBean {

    private CustomEntityLoader customEntityLoader;
    private static MainBean instance;
    private boolean loadSettings;

    @SneakyThrows
    @Mod.EventHandler
    public void init(FMLPostInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
        FontAtlasBuilder.instance
                .addFont("Sweets Smile.ttf", 60, CustomFont.ALL, 0)
                .addFont("Inter-Bold.ttf", 30, CustomFont.ALL, 0)
                .addFont("Purple Smile.ttf", 60, CustomFont.ALL, 0)
                .buildAtlas();

        instance = this;
        customEntityLoader = new CustomEntityLoader();
        new Loader();

        ImageLoader.createTexture("D:\\DesktopStorage\\Intellij_Projects\\Minecraft_Projects\\GROMOD_REMASTERED\\src\\main\\resources\\assets\\bean\\logo\\logo.png");
        //new FpsTest();

        loadSettings = true;
    }

    @SubscribeEvent
    public void onPlayerLeave(PlayerEvent.PlayerLoggedOutEvent event) {
        new SaveSettings();
    }

    @Mod.EventHandler
    public void onServerStopping(FMLServerStoppingEvent event) {
        new SaveSettings();
    }

    @SubscribeEvent
    public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event){
        new LoadSettings();
    }

    public CustomEntityLoader getCustomEntityRenderer() {
        return customEntityLoader;
    }

    public static MainBean getInstance() {
        return instance;
    }

    /*
    TODO:
        - Modules:
            - cannoning
                - autotick
                - debugblock that works
                - inside of gametick tnt visualiser
            - fps
                - entity limit
            - schematica mods
                - faster printer somehow
                - replay printer
                - auto fit schematic
            - other
                - basebuilder
     */
}
