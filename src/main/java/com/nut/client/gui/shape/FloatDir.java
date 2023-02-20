package com.nut.client.gui.shape;

import java.util.ArrayList;
import java.util.List;

public enum FloatDir {
    LEFT,
    RIGHT,
    TOP,
    BOTTOM,
    TOP_LEFT,
    TOP_RIGHT,
    BOTTOM_LEFT,
    BOTTOM_RIGHT,
    NONE;

    public static List<FloatDir> getLeft() {
        List<FloatDir> list = new ArrayList<>();
        list.add(LEFT);
        list.add(TOP_LEFT);
        list.add(BOTTOM_LEFT);
        return list;
    }

    public static List<FloatDir> getRight() {
        List<FloatDir> list = new ArrayList<>();
        list.add(RIGHT);
        list.add(TOP_RIGHT);
        list.add(BOTTOM_RIGHT);
        return list;
    }

    public static List<FloatDir> getTop() {
        List<FloatDir> list = new ArrayList<>();
        list.add(TOP);
        list.add(TOP_LEFT);
        list.add(TOP_RIGHT);
        return list;
    }

    public static List<FloatDir> getBottom() {
        List<FloatDir> list = new ArrayList<>();
        list.add(BOTTOM);
        list.add(BOTTOM_LEFT);
        list.add(BOTTOM_RIGHT);
        return list;
    }
}
