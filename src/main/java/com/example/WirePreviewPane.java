package com.example;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class WirePreviewPane extends Rectangle{

    WirePreviewPane(double width, double height) {
        this.setWidth(width);
        this.setHeight(height);
        //this.setFill(Color.TRANSPARENT);
        this.setFill(new Color(1,1,1,0));
        setupDrawing(this);
    }

    private void setupDrawing(WirePreviewPane self) { //Allows wire previews to render over the background

        self.setOnDragOver(event -> { //Target
            if (event.getDragboard().hasString()) {
                ((WireNode) event.getGestureSource()).drawWire(event.getSceneX(),event.getSceneY());
            }
        });
    }
}
