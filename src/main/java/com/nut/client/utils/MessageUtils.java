package com.nut.client.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;

public class MessageUtils {
    private static final Minecraft mc = Minecraft.getMinecraft();

    public static void addClientMessage(String message) {
        mc.thePlayer.addChatMessage(new ChatComponentText("[Bean Client] " + message));
    }
    public static void addMessage(String message) {
        mc.thePlayer.addChatMessage(new ChatComponentText(message));
    }
    public static String getClientMessage(String message) {
        return "[Bean Client] " + message;
    }
}
