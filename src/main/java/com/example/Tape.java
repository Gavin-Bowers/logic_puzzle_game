package com.example;

import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Tape extends Group{
    GridPane data = new GridPane();
    WireNode output1 = new WireNode(100,50,"output");
    WireNode output2 = new WireNode(100,100,"output");
    private int index = 0;
    private int width = 0;
    private int length = 0;

    Tape(){}
    
    Tape(String values[]) {
        this.width = values.length;
        this.length = values[0].length();

        for(int i=0; i<values.length; i++) {
            char[] digits = values[i].toCharArray();
            for(int j=0; j<digits.length; j++) {
                data.add(new Rectangle(30,30, ('1'== digits[j]) ? Color.GREEN : Color.BLACK), i, j); //makes a 10 by 10 square at the appropriate location in the table (data), and sets the color depending on the input
            }
        }

        data.setPadding(new Insets(10, 10, 10, 10)); 
        this.getChildren().addAll(data, output1, output2);
        //App.root.getChildren().add(this);
    }

    public Boolean hasNext() {
        return index < this.length - 1;
    }

    public Boolean[] getValues() {
        Boolean[] returnVal = new Boolean[width];
        for(int i = 0; i < width; i++) {
            returnVal[i] = (Color)((Rectangle) data.getChildren().get(this.index*width + i)).getFill() == Color.GREEN;
        }
        return returnVal;
    }

    public void next() {
        index++;
        for(Node n : data.getChildren()) {
            n.setEffect(null);
            if(GridPane.getRowIndex(n) == index) { //may need to be "data."" instead
                DropShadow shadow = new DropShadow();
                shadow.setColor(Color.LIMEGREEN);
                n.setEffect(shadow);
            }
        }
    }

    public void updateWires(WireNode node, WireNode connectedNode) { //The gate whose output(s) connect to a wire are responsible for drawing it
        node.drawWire(connectedNode.getX(),connectedNode.getY());
    }
}
