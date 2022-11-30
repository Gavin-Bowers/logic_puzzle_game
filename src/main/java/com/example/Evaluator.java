package com.example;

import java.util.ArrayList;

import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Evaluator extends Group{
    private VBox organizerVBox = new VBox(10);

    //Having any buttons in the program cause default css to be applied, so the styles.css file is used to overide it
    private HBox buttonHBox = new HBox();
    private Button addInputButton = new Button("+");
    private Button removeInputButton = new Button("-");
    private Button playButton = new Button("PLAY");

    private ArrayList<WireNode> inputs = new ArrayList<WireNode>();
    private GridPane inputBox = new GridPane();
    private GridPane data = new GridPane();

    Evaluator() {
        setupInputAdder(addInputButton);
        setupInputRemover(removeInputButton);
        setupPlayButton(playButton);
        buttonHBox.setAlignment(Pos.TOP_RIGHT);

        inputBox.setHgap(10);
        inputBox.setVgap(10);
        organizerVBox.setAlignment(Pos.TOP_RIGHT);

        //data.setPadding(new Insets(10, 10, 10, 10)); 

        
        buttonHBox.getChildren().addAll(addInputButton, removeInputButton, playButton);
        organizerVBox.getChildren().addAll(buttonHBox, inputBox, data);
        this.getChildren().add(organizerVBox);

        setupWirePreviewOverEvaluator(this);
    }

    //Button Handling

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

    private void setupPlayButton(Button self) {
        self.setOnAction(event -> {
            this.evaluate();
        });
    }

    //Other Stuff

    public void evaluate() {
        App.tape.reset();
        this.organizerVBox.getChildren().remove(this.data);
        this.data = new GridPane();
        this.organizerVBox.getChildren().add(this.data);

        for(int i = 0; i < App.tape.getLength(); i++) {
            for(int j = 0; j < inputs.size(); j++) {
                data.add(new Rectangle(30,30, (inputs.get(j).evaluate()) ? Color.GREEN : Color.BLACK), j, i); //Column, row
            }
            App.tape.next();
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
