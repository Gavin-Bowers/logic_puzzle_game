package com.example;

import java.util.ArrayList;

import com.example.App.GateType;

import javafx.scene.Group;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

public class LogicGate extends Group{

    private GateType type;    
    private ImageView image;
    private ArrayList<WireNode> inputs = new ArrayList<WireNode>();
    private ArrayList<WireNode> outputs = new ArrayList<WireNode>();
    
    LogicGate(){} //Don't use, this is just here so Java doesn't complain

    LogicGate(GateType type) {
        this.type = type;
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

            this.setEffect(new Glow(0.8));

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
        self.image.setOnDragOver(event ->{
            if (event.getDragboard().hasString()) {
                ((WireNode) event.getGestureSource()).drawWire(event.getSceneX(),event.getSceneY());
            }
        });
    }

    private void setupDrag(ImageView image, LogicGate self) {
        final Delta dragDelta = new Delta(); //This sticks around as long as the gate does despite being declared in this function

        image.setOnMousePressed(event -> {
            image.setMouseTransparent(true);
            image.getParent().toFront();
            dragDelta.x = image.getParent().getTranslateX() - event.getSceneX();
            dragDelta.y = image.getParent().getTranslateY() - event.getSceneY();
            event.setDragDetect(true);

            //Shadow when dragged
            DropShadow shadow = new DropShadow();
            shadow.setColor(Color.LIMEGREEN);
            image.setEffect(shadow);
        });

        image.setOnMouseReleased(event -> {
            image.setMouseTransparent(false);
            image.setEffect(null); //Turns off the drop shadow
        });

        image.setOnMouseEntered(event -> {
        });

        image.setOnMouseDragged(event -> {
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
                    updateWires(connectedNode, node);
                }
            });
        });
    }

    //Connected wires are drawn entirely by the output node which connects to the wire.
    //Preview wires meanwhile are drawn by whichever node the drag gesture begins at

    public void updateWires(WireNode node, WireNode connectedNode) {
        node.drawWire(connectedNode.getAbsoluteX(),connectedNode.getAbsoluteY());
    }

    public Boolean evaluate() {
        Boolean returnVal;
        switch(this.type) { //This will be extended to include the logic in the future
            case OR:
                returnVal = inputs.get(0).evaluate() || inputs.get(1).evaluate();
                break;
            case AND:
                returnVal = inputs.get(0).evaluate() && inputs.get(1).evaluate();
                break;
            case NOT:
                returnVal = !inputs.get(0).evaluate();
                break;
            case SPLITTER:
                returnVal = inputs.get(0).evaluate();
                break;
            case NOR:
                returnVal = !(inputs.get(0).evaluate() || inputs.get(1).evaluate());
                break;
            case NAND:
                returnVal = !(inputs.get(0).evaluate() && inputs.get(1).evaluate());
                break;
            case XOR:
                returnVal = inputs.get(0).evaluate() ^ inputs.get(1).evaluate();
                break;
            case XNOR:
                returnVal = !(inputs.get(0).evaluate() ^ inputs.get(1).evaluate());
                break;
            default:
                returnVal = true;
                System.out.println("Enum Error");
                break;
        }
        return returnVal;
    }
}
