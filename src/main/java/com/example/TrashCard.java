package com.example;

import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class TrashCard extends ImageView{
    private static boolean isOpen = false;

    TrashCard() {
        this.setImage(new Image(getClass().getResourceAsStream("DeleteClosedcard.png")));
        this.setX(0);
        this.setY(0);
        this.setFitHeight(225);
        this.setFitWidth(150);
        setupWirePreviewOverCard(this);
    }

    private void setupWirePreviewOverCard(TrashCard self) { //Allows wire previews to render over this node
        self.setOnDragOver(event -> { //Target
            if (event.getDragboard().hasString()) {
                ((WireNode) event.getGestureSource()).drawWire(event.getSceneX(),event.getSceneY());
            }
        });
    }

    public boolean isInTrash(Node node) {
        Bounds trashBounds = this.localToScene(this.getBoundsInLocal());
        Bounds nodeBounds = node.localToScene(node.getBoundsInLocal());
        return trashBounds.intersects(nodeBounds);
    }

    public void setOpen() {
        if(!isOpen) {
            this.setImage(new Image(getClass().getResourceAsStream("DeleteOpencard.png")));
            isOpen = true;
        }
    }

    public void setClosed() {
        if(isOpen) {
            this.setImage(new Image(getClass().getResourceAsStream("DeleteClosedcard.png")));
            isOpen = false;
        }
    }
}
