package com.example;

import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Tape extends Group{
    GridPane dataView = new GridPane();
    Boolean[][] data;
    WireNode output1 = new WireNode(25,40,"output");
    WireNode output2 = new WireNode(55,20,"output");
    private int index = 0;
    private int width = 0;
    private int length = 0;

    Tape(){}
    
    Tape(String values[]) {
        this.width = values.length;
        this.length = values[0].length();
        data = new Boolean[width][length];

        for(int i=0; i<values.length; i++) {
            char[] digits = values[i].toCharArray();
            for(int j=0; j<digits.length; j++) {
                data[i][j] = '1'== digits[j];
                dataView.add(new Rectangle(30,30, ('1'== digits[j]) ? Color.GREEN : Color.BLACK), i, j); //makes a 10 by 10 square at the appropriate location in the table (data), and sets the color depending on the input
            }
        }

        dataView.setPadding(new Insets(60, 10, 10, 10));
        this.getChildren().addAll(dataView, output1, output2);
        
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
