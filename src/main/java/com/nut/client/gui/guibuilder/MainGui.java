package com.nut.client.gui.guibuilder;

import com.nut.client.gui.shapes.RRectangle;

public class MainGui {
    public MainGui(){
        RRectangle main = new RRectangle(0, 0, 20, 20, 10);
        main.add(new RRectangle(10, 10, 10, 10, 10));
    }
}
