package com.example;

import java.util.ArrayList;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Evaluator extends Group{
    private VBox organizerBox = new VBox(10);

    //Having any buttons in the program cause default css to be applied, so the styles.css file is used to overide it
    private HBox buttonBox = new HBox();
    private Button addInputButton = new Button("+");
    private Button removeInputButton = new Button("-");

    private ArrayList<WireNode> inputs = new ArrayList<WireNode>();
    private GridPane inputBox = new GridPane();
    private GridPane data = new GridPane();

    Evaluator() {
        

        setupInputAdder(addInputButton);
        setupInputRemover(removeInputButton);
        buttonBox.setAlignment(Pos.TOP_RIGHT);

        inputBox.setHgap(10);
        inputBox.setVgap(10);
        organizerBox.setAlignment(Pos.TOP_RIGHT);

        data.setPadding(new Insets(40, 10, 10, 10)); 

        
        buttonBox.getChildren().addAll(addInputButton, removeInputButton);
        organizerBox.getChildren().addAll(buttonBox, inputBox, data);
        this.getChildren().add(organizerBox);

        setupWirePreviewOverEvaluator(this);
    }

    private void setupInputAdder(Button self) {
        self.setOnAction(event -> {
            WireNode wirenode = new WireNode(0,0,"input");
            inputs.add(wirenode);
            inputBox.add(wirenode, inputs.size(), inputs.size());

            for(WireNode node : inputs) {
                if(node.getConnectedNode() != null) {
                    node.getConnectedNode().drawWire(node.getAbsoluteX()-20,node.getAbsoluteY()); //The position changes after this executes, so the position change must be added (-20)
                }
            }
        });
    }

    private void setupInputRemover(Button self) {
        self.setOnAction(event -> {
            WireNode wirenode = inputs.get(inputs.size()-1);
            inputBox.getChildren().remove(wirenode);
            inputs.remove(wirenode);

            if(wirenode.getConnectedNode() != null) {
                wirenode.getConnectedNode().nullConnectedNode();
            }

            for(WireNode node : inputs) {
                if(node.getConnectedNode() != null) {
                    node.getConnectedNode().drawWire(node.getAbsoluteX()+20,node.getAbsoluteY());
                }
            }
        });
    }

    public void evaluate() {
        int i = 0;
        while (App.tape.hasNext()) {
            for(int j = 0; j < inputs.size(); j++) {
                data.add(new Rectangle(30,30, (inputs.get(j).evaluate()) ? Color.GREEN : Color.BLACK), i, j);
            }
            App.tape.next();
            i++;
        }
    }

    private void setupWirePreviewOverEvaluator(Evaluator self) { //Allows wire previews to render over this node
        self.setOnDragOver(event -> { //Target
            if (event.getDragboard().hasString()) {
                ((WireNode) event.getGestureSource()).drawWire(event.getSceneX(),event.getSceneY());
            }
        });
    }
}
