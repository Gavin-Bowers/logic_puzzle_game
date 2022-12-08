package com.example;

import java.util.ArrayList;

import com.example.App.GateType;

import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class LogicGate extends Group{

    private GateType type;    
    private ImageView image;
    private ArrayList<WireNode> inputs = new ArrayList<WireNode>();
    private ArrayList<WireNode> outputs = new ArrayList<WireNode>();
    private HBox hbox;
    private VBox nodeContainer;
    
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
                //Splitters function pretty differently to other logic gates, so I wrote my own code for making them - Lily
                inputs.add(new WireNode(6, 25, "input"));
                outputs.add(new WireNode(130, 25, "output"));
                splitterSupport();
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

            //Trashing check
            if(App.trash.isInTrash(self)) {
                App.trash.setClosed();
                for(WireNode wirenode : inputs) {
                    if(wirenode.getConnectedNode() != null) {
                        wirenode.getConnectedNode().drawWire(wirenode.getConnectedNode().getAbsoluteX(),wirenode.getConnectedNode().getAbsoluteY());
                        wirenode.getConnectedNode().nullConnectedNode();
                    }
                }
                for(WireNode wirenode : outputs) {
                    if(wirenode.getConnectedNode() != null) {
                        wirenode.getConnectedNode().drawWire(wirenode.getConnectedNode().getAbsoluteX(),wirenode.getConnectedNode().getAbsoluteY());
                        wirenode.getConnectedNode().nullConnectedNode();
                        wirenode.clearWire();
                        this.getChildren().remove(wirenode);
                    }
                }
                App.root.getChildren().remove(self);
                App.deleteGate(self);
            }
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

            //Trash preview
            if(App.trash.isInTrash(self)) {
                App.trash.setOpen();
            } else {
                App.trash.setClosed();
            }
        });
    }

    //Connected wires are drawn entirely by the output node which connects to the wire.
    //Preview wires meanwhile are drawn by whichever node the drag gesture begins at

    public void updateWires(WireNode node, WireNode connectedNode) {
        node.drawWire(connectedNode.getAbsoluteX(),connectedNode.getAbsoluteY());
    }

    public Boolean evaluate() {
        Boolean returnVal;

        //checks for any null sources, and if one is found, it returns false and changes red, to indicate an incorrect node.
        for(int i = 0; i < inputs.size(); i++) {
        	if(inputs.get(i).getConnectedNode() == null) {
        		inputs.get(i).setFill(Color.web("0xFF0000"));
        		return false;
        	}
        }

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
    public void addNodeSplitter() {
    	System.out.println(outputs.size());
    	//nodeContainer.getChildren().add(new WireNode());	
    	if(this.outputs.size() == 1) {
    		outputs.add(new WireNode(130, 55, "output"));
    		this.getChildren().add(outputs.get(outputs.size()-1));
    	} else if(this.outputs.size() % 2 == 1) {
            outputs.add(new WireNode(130, 25 + (15 *(outputs.size() + 1)), "output"));
            this.getChildren().add(outputs.get(outputs.size()-1));
        } else {
            outputs.add(new WireNode(130, 25 - (15 * (outputs.size())), "output"));
            nodeContainer.setTranslateY(nodeContainer.getTranslateY() - 30);
            this.getChildren().add(outputs.get(outputs.size()-1));
        }
    }

    //Lily code for splitters v
	private void splitterSupport() {
		//create a button for adding nodes
		Button add = new Button("+");
		add.setOnAction(Event -> {
			addNodeSplitter();
		});
		
		//create a button for removing nodes
		Button remove = new Button("-");
		remove.setOnAction(Event -> {
			WireNode temp = outputs.get(outputs.size()-1);
			if(outputs.size() > 1) {
				if(temp.getConnectedNode() != null) {
					temp.getConnectedNode().clearWire();
					temp.clearWire();
					
				}
				if(outputs.size() %2 == 1) {
					nodeContainer.setTranslateY(nodeContainer.getTranslateY() + 30);
				}
				this.getChildren().remove(temp);
				outputs.remove(outputs.size()-1);
				nodeContainer.getChildren().remove(nodeContainer.getChildren().size()-1);
			}
		});
		hbox = new HBox(10);
		hbox.setTranslateX(35);
		hbox.getChildren().addAll(add, remove);
		nodeContainer = new VBox(30);
		nodeContainer.setStyle("-fx-border-color: green");
		nodeContainer.setTranslateX(130);
		nodeContainer.setTranslateY(25);
		//nodeContainer.getChildren().add(new WireNode());
		this.getChildren().addAll(hbox, nodeContainer);
		
	}
	//Lily code for splitters ^
	//Lily code for splitters ^
}
