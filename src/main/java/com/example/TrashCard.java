package com.example;

import javafx.scene.image.ImageView;

import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;

public class TrashCard extends ImageView{

    TrashCard() {
        this.setImage(new Image(getClass().getResourceAsStream("DeleteClosedcard.png")));
        this.setX(0);
        this.setY(0);
        this.setFitHeight(225);	//will fix + standardize latter, this is for testing
        this.setFitWidth(150);
        setupWirePreviewOverCard(this);	//Works now
    }

    private void setupWirePreviewOverCard(TrashCard self) { //Allows wire previews to render over this node
        self.setOnDragOver(event -> { //Target
            if (event.getDragboard().hasString()) {
                ((WireNode) event.getGestureSource()).drawWire(event.getSceneX(),event.getSceneY());
            }
        });
    }

     private void setupClickSpawn(GateCard self) {
         this.setOnMousePressed(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent event) {
            Bounds boundsInScene = self.localToScene(self.getBoundsInLocal()); //Used to get absolute position of card, so the gate can be spawned above it
            App.SpawnGate(self.type, boundsInScene.getMinX() + 8, boundsInScene.getMinY() - 60);
         }});
     }
    
}
