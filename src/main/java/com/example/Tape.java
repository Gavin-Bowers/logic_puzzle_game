package com.example;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Tape extends Group{
    GridPane dataView = new GridPane();
    Boolean[][] data;
    WireNode output1 = new WireNode(0,0,"output");
    WireNode output2 = new WireNode(0,0,"output");
    private int index = 0;
    private int width = 0;
    private int length = 0;

    Tape(){}
    
    Tape(String values[]) {
        this.width = values.length;
        this.length = values[0].length();
        data = new Boolean[width][length];

        //This lays out the nodes, and their connections the the data columns
        VBox inputBox1 = new VBox(output1,new Rectangle(2,30,Color.LIGHTGREEN));
        VBox inputBox2 = new VBox(output2, new Rectangle(2,60,Color.LIGHTGREEN));
        inputBox1.setAlignment(Pos.BOTTOM_CENTER);
        inputBox2.setAlignment(Pos.BOTTOM_CENTER);
        GridPane.setMargin(inputBox1,new Insets(10,5,0,5));
        GridPane.setMargin(inputBox2,new Insets(10,5,0,5));

        dataView.add(inputBox1, 0, 0);
        dataView.add(inputBox2, 1, 0);
        dataView.setPadding(new Insets(40,0,0,0));

        Insets dataSpacing = new Insets(0,5,0,5);

        for(int i=0; i<values.length; i++) {
            char[] digits = values[i].toCharArray();
            for(int j=0; j<digits.length; j++) {

                data[i][j] = '1'== digits[j];
                Rectangle rect = new Rectangle(30,30, ('1'== digits[j]) ? Color.GREEN : Color.BLACK);
                GridPane.setMargin(rect, dataSpacing);
                rect.setStroke(Color.LIGHTGREEN);
                dataView.add(rect, i, 1+j); //makes a 10 by 10 square at the appropriate location in the table (data), and sets the color depending on the input
            }
        }

        this.getChildren().addAll(dataView);
        
        setupWirePreviewOverTape(this);
    }

    private void setupWirePreviewOverTape(Tape self) { //Allows wire previews to render over this node
        self.setOnDragOver(event -> { //Target
            if (event.getDragboard().hasString()) {
                ((WireNode) event.getGestureSource()).drawWire(event.getSceneX(),event.getSceneY());
            }
        });
    }

    public void next() {
        this.index++;
        /*
        for(Node n : data.getChildren()) {
            n.setEffect(null);
            if(GridPane.getRowIndex(n) == this.index) { //may need to be "data."" instead
                DropShadow shadow = new DropShadow();
                shadow.setColor(Color.LIMEGREEN);
                n.setEffect(shadow);
            }
        }
        */
    }

    public void reset() {
        this.index = 0;
    }

    public int getLength() {
        return this.length;
    }

    public Boolean getValue(int lane) {
        return data[lane][this.index];
    }

    public Boolean evaluate(WireNode node) {
        if(node.equals(output1)) {
            return getValue(0);
        } else {
            return getValue(1);
        }

    }
}
