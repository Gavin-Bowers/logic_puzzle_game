package com.example;

import com.example.App.GateType;

import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class GateCard extends ImageView{
    public GateType type;
    
     GateCard(GateType gateType) {
        try {
            this.type = gateType;
            String fileName = "";
            switch(gateType) {
                case OR:
                    fileName = "orcard.png";
                    break;
                case AND:
                    fileName = "andcard.png";
                    break;
                case NOT:
                    fileName = "notcard.png";
                    break;
                case SPLITTER:
                    fileName = "splittercard.png";
                    break;
                case NOR:
                    fileName = "norcard.png";
                    break;
                case NAND:
                    fileName = "nandcard.png";
                    break;
                case XOR:
                    fileName = "xorcard.png";
                    break;
                case XNOR:
                    fileName = "xnorcard.png";
                    break;
                default:
                    fileName = "andcard.png";
                    System.out.println("How did you get here?");
                    break;
            }

            //I guess filenames aren't case sensitive
            this.setImage(new Image(getClass().getResourceAsStream(fileName)));
            this.setX(0);
            this.setY(0);
            this.setFitHeight(225);	//will fix + standardize latter, this is for testing
            this.setFitWidth(150);
            setupClickSpawn(this);	//Allows click to spawn Gates from cards
            setupWirePreviewOverCard(this);	//Works now
            
        } catch(Exception e) {
            System.out.println("Error: Invalid filename for GateCard (or another error in this code block): " + type);
        }
    }

    private void setupWirePreviewOverCard(GateCard self) { //Allows wire previews to render over this node
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
