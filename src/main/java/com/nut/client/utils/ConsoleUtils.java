package com.nut.client.utils;

import java.awt.Color;

public class ConsoleUtils {
    public static void printC(String text, Color color) {
        String colorCode = getANSIFromColor(color);
        System.out.println(colorCode + text + "\u001B[0m");
    }

    private static String getANSIFromColor(Color color) {
        if (Color.RED.equals(color)) {
            return "\u001B[31m";
        } else if (Color.GREEN.equals(color)) {
            return "\u001B[32m";
        } else if (Color.YELLOW.equals(color)) {
            return "\u001B[33m";
        } else if (Color.BLUE.equals(color)) {
            return "\u001B[34m";
        } else if (Color.MAGENTA.equals(color)) {
            return "\u001B[35m";
        } else if (Color.CYAN.equals(color)) {
            return "\u001B[36m";
        } else if (Color.WHITE.equals(color)) {
            return "\u001B[37m";
        }
        return "\u001B[0m";
    }
}
