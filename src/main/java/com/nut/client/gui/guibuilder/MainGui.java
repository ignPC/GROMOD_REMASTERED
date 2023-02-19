package com.nut.client.gui.guibuilder;

import com.nut.client.gui.shapes.RRectangle;

import static com.nut.client.gui.shapes.AbstractShape.*;

public class MainGui {
    public MainGui() throws IllegalAccessException, InstantiationException {
        RRectangle main = new RRectangle(0, 0, 20, 20, 10);
        main.add(new RRectangle(10, 10, 10, 10, 10));

        RRectangle rRectangle = (RRectangle) new ShapeBuilder(RRectangle.class)
                .withPadding(10)
                .withMargin(20)
                .build(0, 0, 100, 50);
    }
}
