package com.nut.client.gui.shape;

import com.nut.client.utils.Vector2i;

public class Positioner {

    public static void position(int x, int y, int rows, int columns, int gap, Shape... shapes) {
        Shape lastShape = null;
        Vector2i vector = new Vector2i(x, y);
        int index = 0;

        for (int i = 0; i < rows; i++) {
            if (lastShape != null)
                vector.set(x, lastShape.y + lastShape.height + lastShape.margin.getBottom() + gap);

            for (int j = 0; j < columns; j++) {
                if (index == shapes.length) break;
                lastShape = shapes[index];

                lastShape.setPosition(vector.x + lastShape.margin.getLeft(), vector.y + lastShape.margin.getTop());
                vector.set(lastShape.x + lastShape.width + lastShape.margin.getRight() + gap, vector.y);
                index++;
            }
        }
    }
}
