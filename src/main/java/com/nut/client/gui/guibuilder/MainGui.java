package com.nut.client.gui.guibuilder;

import com.nut.client.annotation.AutoInit;
import com.nut.client.annotation.Component;
import com.nut.client.gui.shape.FloatDir;
import com.nut.client.gui.shape.shapes.RRectangle;

import static com.nut.client.gui.shape.AbstractShape.*;

@Component
public class MainGui {
    @AutoInit
    public MainGui() {
        RRectangle main = new RRectangle(0, 0, 20, 20, 10);
        main.add(new RRectangle(10, 10, 10, 10, 10));

        RRectangle rRectangle = (RRectangle) new ShapeBuilder(new RRectangle(0, 0, 20, 20, 10))
                .withPadding(10)
                .withMargin(20)
                .withFloat(FloatDir.LEFT)
                .build();

        rRectangle.add(main);

        System.out.println(rRectangle.getInformation());

    }
}
