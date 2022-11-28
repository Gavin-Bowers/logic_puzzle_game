package com.example;

import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.paint.Color;

public class EndWireNode extends WireNode{

    public EndWireNode(double x, double y) {
        super(x,y,"input");
        //makeWirable(this);
    }

    private void makeWirable(WireNode self) {

        self.setOnMouseEntered(event -> {
            self.setFill(Color.LIGHTGREEN);
        });

        self.setOnMouseExited(event -> {
            self.setFill(Color.GREEN);
        });

        self.setOnDragDetected(event -> { //Drag started
            //Communicates whether the initiating node is an input or output node with dragboard
            Dragboard db = self.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            content.putString(type);
            db.setContent(content);
            //Disconnects wire
            if(connectedNode != null) {
                connectedNode.clearWire();
                connectedNode.nullConnectedNode();
            }
            self.clearWire();
            event.consume();
        });

        //This performs the import task of communicating to the drag gestrure that the node can be dragged onto
        self.setOnDragOver(event -> { //Target (This executes on the target node rather than the originating node)
            if (event.getGestureSource() != self && 
                ((WireNode) event.getGestureSource()).getParent() != self.getParent() && 
                event.getDragboard().hasString() &&
                self.connectedNode == null) {

                if(event.getDragboard().getString() == "output" && self.type != "output" || event.getDragboard().getString() != "output" && self.type == "output") { //Check for heterosexuality
                    event.acceptTransferModes(TransferMode.MOVE);
                }
            }
            if (event.getDragboard().hasString()) { //Allows wire preview over node
                ((WireNode) event.getGestureSource()).drawWire(event.getSceneX(),event.getSceneY());
            }

            event.consume();
        });

        //Visual feedback
        self.setOnDragEntered(event -> { //Target
            if (event.getGestureSource() != self && event.getDragboard().hasString()) {
                if(event.getDragboard().getString() == "output" && self.type != "output" || event.getDragboard().getString() != "output" && self.type == "output") {
                    self.setFill(Color.LIGHTGREEN);
                }
            }
            event.consume();
        });

        self.setOnDragExited(event -> { //Target
            self.setFill(Color.GREEN);
            event.consume();
        });

        //Drag completion
        self.setOnDragDropped(event -> { //Target
            Dragboard db = event.getDragboard();
            boolean success = false;
            if (db.hasString()) {
                success = true;
                ((WireNode) event.getGestureSource()).setConnectedNode(self);
                self.setConnectedNode((WireNode) event.getGestureSource());
                
                //Refresh wire so it snaps to node
                if(db.getString() == "output") {
                    ((WireNode) event.getGestureSource()).setWireEndPosition(self.getX(), self.getY());
                } else {
                    //System.out.println(self.getX());
                    self.setWireStartPosition(self.getX(), self.getY());
                    self.drawWire(((WireNode) event.getGestureSource()).getX(),((WireNode) event.getGestureSource()).getY());
                }
            }
            event.setDropCompleted(success); //lets the source know whether the string was successfully transferred and used
            event.consume();
        });

        self.setOnDragDone(event -> { //Source
            if (event.getTransferMode() == TransferMode.MOVE) { //Checks if the drop was successful

            } else {
                //Remove wire preview on failure to connect
                if(connectedNode != null) {
                    connectedNode.clearWire();
                    connectedNode.nullConnectedNode();
                }
                self.clearWire();
            }
            if(self.type == "input") {
                App.root.getChildren().remove(self.wire);
                self.wireIsVisible = false;
            }
            event.consume();
        });
    }
    
}
