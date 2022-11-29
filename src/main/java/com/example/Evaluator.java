package com.example;

import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Evaluator extends Group{
    GridPane data = new GridPane();
    WireNode input1 = new WireNode(-30,50,"input");
    WireNode input2 = new WireNode(-30,100,"input");

    Evaluator() {
        for(int i=0; i<2; i++) {
            for(int j=0; j<8; j++) {
                data.add(new Rectangle(30,30, Color.BLACK), i, j); //makes a 10 by 10 square at the appropriate location in the table (data), and sets the color depending on the input
            }
        }

        data.setPadding(new Insets(10, 10, 10, 10)); 
        this.getChildren().addAll(data, input1, input2);
    }

    Evaluator(String values[]) {
        for(int i=0; i<values.length; i++) {
            char[] digits = values[i].toCharArray();
            for(int j=0; j<digits.length; j++) {
                data.add(new Rectangle(30,30, ('1'== digits[j]) ? Color.GREEN : Color.BLACK), i, j); //makes a 10 by 10 square at the appropriate location in the table (data), and sets the color depending on the input
            }
        }

        data.setPadding(new Insets(10, 10, 10, 10)); 
        this.getChildren().addAll(data, input1, input2);
        //App.root.getChildren().add(this);
    }

    public void evaluate() {
        int i = 0;
        while (App.tape.hasNext()) {
            Boolean result1 = input1.evaluate();
            Boolean result2 = input2.evaluate();
            data.add(new Rectangle(30,30, (result1) ? Color.GREEN : Color.BLACK), i, 0);
            data.add(new Rectangle(30,30, (result2) ? Color.GREEN : Color.BLACK), i, 1);
            App.tape.next();
            i++;
        }
    }
}
