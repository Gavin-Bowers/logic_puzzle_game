package com.example;

import java.util.ArrayList;

import com.example.App.GateType;

import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;

public class LogicGate extends Group{
    
    private ImageView image;
    private ArrayList<WireNode> inputs = new ArrayList<WireNode>();
    private ArrayList<WireNode> outputs = new ArrayList<WireNode>();
    
    LogicGate(){} //Don't use, this is just here so Java doesn't complain

    LogicGate(GateType type) {
        try {
            String fileName = "";
            switch(type) { //This will be extended to include the logic in the future
                case OR:
                    fileName = "orgate2.png";
                    break;
                case AND:
                    fileName = "andgate2.png";
                    break;
                case NOT:
                    fileName = "notgate2.png";
                    break;
                case SPLITTER:
                    fileName = "splitter2.png";
                    break;
                case NOR:
                    fileName = "norgate2.png";
                    break;
                case NAND:
                    fileName = "nandgate2.png";
                    break;
                case XOR:
                    fileName = "xorgate2.png";
                    break;
                case XNOR:
                    fileName = "xnorgate2.png";
                    break;
                default:
                    fileName = "andgate2.png";
                    System.out.println("How did you get here?");
                    break;
            }
            Image image = new Image(getClass().getResourceAsStream(fileName));
            this.image = new ImageView(image);
            this.image.setX(0);
            this.image.setY(0);
            this.image.setFitHeight(50);
            this.image.setFitWidth(135);
            setupDrag(this.image, this); //Allows clicking and dragging to translate (change the tranlsation x and y, which apply after other positioning) to the group
            this.getChildren().add(this.image);
            
            //input wire select amount
            //New code from Mika v -------------------------------------------------------
            if(type == GateType.NOT) {	//for only one input, (probably only going to be used by NOTgate)
                inputs.add(new WireNode(6, 25, "input"));
                outputs.add(new WireNode(130, 25, "output"));
            }
            else if(type == GateType.SPLITTER) {
                inputs.add(new WireNode(6, 25, "input"));
                outputs.add(new WireNode(90, 25, "output"));
                outputs.add(new WireNode(110, 25, "output"));
                outputs.add(new WireNode(130, 25, "output"));
            } else {
            	inputs.add(new WireNode(6, 14, "input"));
                inputs.add(new WireNode(6, 37, "input"));
                outputs.add(new WireNode(130, 25, "output"));
            }
            //New code from Mika ^ -------------------------------------------------------

            //The wirenodes are both owned by this object, and are children of it in the javafx hierarchy
            inputs.forEach(node -> this.getChildren().add(node));
            inputs.forEach(node -> node.setupWire());

            outputs.forEach(node -> this.getChildren().add(node));
            outputs.forEach(node -> node.setupWire());

            App.root.getChildren().add(this);
            setupWirePreviewOverGate(this);

        } catch(Exception e) {
            System.out.println("Error: Invalid type for gate (or another error in this code block): " + type);
        }
    }

    private void setupWirePreviewOverGate(LogicGate self) { //Allows wire previews to render over this node
        self.image.setOnDragOver(new EventHandler<DragEvent>() { public void handle(DragEvent event) { //Target
            if (event.getDragboard().hasString()) {
                ((WireNode) event.getGestureSource()).drawWire(event.getSceneX(),event.getSceneY());
            }
        }});
    }

    private void setupDrag(ImageView image, LogicGate self) {
        final Delta dragDelta = new Delta(); //This sticks around as long as the gate does despite being declared in this function

        image.setOnMousePressed(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent event) {
            image.setMouseTransparent(true);
            image.getParent().toFront();
            dragDelta.x = image.getParent().getTranslateX() - event.getSceneX();
            dragDelta.y = image.getParent().getTranslateY() - event.getSceneY();
            event.setDragDetect(true);
        }});
        image.setOnMouseReleased(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent event) {
            image.setMouseTransparent(false);
            //image.getParent().setTranslateX(20 * Math.round(image.getParent().getTranslateX()/20));
            //image.getParent().setTranslateY(20 * Math.round(image.getParent().getTranslateY()/20));
        }});
        image.setOnMouseEntered(new EventHandler<MouseEvent>() { @Override public void handle(MouseEvent event) {
        }});
        image.setOnMouseDragged(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent event) {
            image.getParent().setTranslateX(event.getSceneX() + dragDelta.x);
            image.getParent().setTranslateY(event.getSceneY() + dragDelta.y);
            event.setDragDetect(false);

            //Updates wires when moving gates
            outputs.forEach(node -> {
                if(node.getConnectedNode() != null) {
                    WireNode connectedNode = node.getConnectedNode();
                    self.updateWires(node, connectedNode);
                }
            });
            inputs.forEach(node -> {
                if(node.getConnectedNode() != null) {
                    WireNode connectedNode = node.getConnectedNode();
                    ((LogicGate) connectedNode.getParent()).updateWires(connectedNode, node);
                }
            });
        }});
    }

    public void updateWires(WireNode node, WireNode connectedNode) { //The gate whose output(s) connect to a wire are responsible for drawing it
        node.drawWire(connectedNode.getX(),connectedNode.getY());
    }
}
