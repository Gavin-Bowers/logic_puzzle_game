package com.example;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.CubicCurve;

public class WireNode extends Circle{

    public static final int APPWIDTH = 1920;
    public static final int APPHEIGHT = 1080; //Not sure why these don't work from App

    protected String type; //Either "input" or "output"
    protected CubicCurve wire = new CubicCurve();
    protected Boolean wireIsVisible = false;
    protected WireNode connectedNode = null;

    WireNode(){}
    
    WireNode(double x, double y, String type) {

        this.type = type;

        this.setLayoutX(x);
        this.setLayoutY(y);
        this.setRadius(6);
        this.setFill(Color.GREEN);

        //wire drawing line
        this.wire.setFill(Color.TRANSPARENT);
        this.wire.setStroke(Color.GREEN);
        //this.wire.setStroke(Color.GRAY);
        this.wire.setStrokeWidth(2);
        this.wire.getStrokeDashArray().addAll(5d, 5d);
        
    }

    public void setupWire() { //Seperate function so it can run after it becomes a child, so the parent's translation can be inherited
        double startX = this.getLayoutX() + this.getParent().getTranslateX();
        double startY = this.getLayoutY() + this.getParent().getTranslateY();
        this.wire.setStartX(startX);
        this.wire.setStartY(startY);
        this.wire.setControlX1(startX);
        this.wire.setControlY1(startY);
        this.wire.setControlX2(startX);
        this.wire.setControlY2(startY);
        this.wire.setEndX(this.getLayoutX() + this.getParent().getTranslateX());
        this.wire.setEndY(this.getLayoutY() + this.getParent().getTranslateY());
    }

    public void drawWire(double x, double y) {
        if(!wireIsVisible) {
            App.root.getChildren().add(wire);
            wire.toBack();
            wireIsVisible = true;
        }

        double startX = this.getLayoutX() + this.getParent().getTranslateX();
        double startY = this.getLayoutY() + this.getParent().getTranslateY();
        double endX = x;
        double endY = y;

        wire.setStartX(startX);
        wire.setStartY(startY);

        wire.setEndX(endX);
        wire.setEndY(endY);

        //Java handles dividing by zero with no complaints
        //It literally ouputs "Infinity", and diving by Infinity gives zero
        //So the following code does in fact work
        wire.setControlX1(endX + ((startX - endX) / (APPWIDTH / Math.abs(startX - endX)))); 
        wire.setControlY1(startY);
        
        wire.setControlX2(startX - ((startX - endX) / (APPWIDTH / Math.abs(startX - endX))));
        wire.setControlY2(endY);
    }

    

    public void clearWire() {
        App.root.getChildren().remove(this.wire);
        this.wireIsVisible = false;
        //Remove reference in LogicGate
        this.nullConnectedNode();
    }

    public double getX() {
        return(this.getLayoutX() + this.getParent().getTranslateX());
    }

    public double getY() {
        return(this.getLayoutY() + this.getParent().getTranslateY());
    }

    public void setWireEndPosition(double x, double y) {
        this.wire.setEndX(x);
        this.wire.setEndY(y);
    }

    public void setWireStartPosition(double x, double y) {
        this.wire.setStartX(x);
        this.wire.setStartY(y);
        App.forceRefresh();
    }

    //Each node points to the node it's wired to, these connections run both ways
    public WireNode getConnectedNode() {
        return connectedNode;
    }

    public void setConnectedNode(WireNode node) {
        this.connectedNode = node;
    }
    public void nullConnectedNode() {
        this.connectedNode = null;
    }

    public Boolean evaluate() {
        return true;
    }
}
