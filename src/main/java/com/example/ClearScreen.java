package com.example;

import javafx.scene.layout.HBox;

//Use to clear things for Screen used for changing through menus - Mika

public class ClearScreen {
	
	private static HBox EmptyHBox = new HBox();
	
	
	public static void All() { //clear all (call all clear methods)
		
		ClearScreen.Center();
        ClearScreen.Cards();
        ClearScreen.Buttons();
        ClearScreen.Input();
        ClearScreen.Gates();
	}
	
	public static void Center() {	//clear center
		HBox Empty = new HBox();			
		App.root.setCenter(Empty);	
    
		Empty.setOnDragOver(event -> {  //makes wire able to draw over with wire
			if (event.getDragboard().hasString()) {
				((WireNode) event.getGestureSource()).drawWire(event.getSceneX(),event.getSceneY());
			}
		});
	}
	
	public static void Cards() {		//clear cards
	     App.root.setBottom(EmptyHBox);			
	}
	
	public static void Buttons() {		//clear Buttons
		Evaluator eval = new Evaluator(true);
        App.root.setRight(eval);
	}
	
	public static void Input() { 		//clear (Tape) Input 
		
		String[] Empty = {""};	//Input
        App.tape = new Tape(Empty, 1);
        App.root.setLeft(App.tape);
        
	}
	
	
	//need to make a clear all gates and wires method
	public static void Gates() {
		
	}
	
}
