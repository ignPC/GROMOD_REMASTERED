package com.nut.client.gui.shape;

public class Margin {

    private final int[] margins = new int[4];

    public Margin(int left, int top, int right, int bottom) {
        margins[0] = left;
        margins[1] = top;
        margins[2] = right;
        margins[3] = bottom;
    }

    public void set(int left, int top, int right, int bottom) {
        margins[0] = left;
        margins[1] = top;
        margins[2] = right;
        margins[3] = bottom;
    }

    public int getLeft() {
        return margins[0];
    }

    public int getTop() {
        return margins[1];
    }

    public int getRight() {
        return margins[2];
    }

    public int getBottom() {
        return margins[3];
    }
}