package com.example;

import com.example.App.GateType;
import com.example.MainMenuSpace.Select;

import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

//Holds and Loads Free Design Space and levels mode Space - mika

public class WorkSpace{
	
	static String LevelOutputEval;	//holds Level Eval
	static Boolean IsItLeveMode;	//
	
//-----------------------------FREE DESIGN-----------------------------------------------------
	public static void LoadFreeDesignSpace() { //Loads Free Design Space
		
		IsItLeveMode = false;
		
    	
    	ClearScreen.All();					//clears screen
    	
        String[] test = {"0011", "0101"};	//Input
        App.tape = new Tape(test);
        App.root.setLeft(App.tape);

        
        Evaluator eval = new Evaluator();	//Output Evaluator
        App.root.setRight(eval);
        


        HBox cards = new HBox(20);			//Gate Cards
        App.root.setBottom(cards);
        
        for (GateType type : GateType.values()) {
            cards.getChildren().add(new GateCard(type));
        }

        
        App.trash = new TrashCard();		//trash function
        cards.getChildren().add(App.trash);	
    }
	
//-----------------------------LEVEL MODE-----------------------------------------------------
	public static void LoadLevelModeSpace(String Level) { //Loads levels mode Space
    	
		IsItLeveMode = true;
		
		
	 	ClearScreen.All();					//clears screen

	 	
        String[] OutputToMatch = {Level};			//Level Output to match
        App.tape = new Tape(OutputToMatch, 3);
        App.root.setCenter( App.tape);
        
	 	
        String[] input = {"0011", "0101"};	//Input
        App.tape = new Tape(input);
        App.root.setLeft( App.tape);
        
        
        Evaluator eval = new Evaluator(3);	//Output Evaluator
        App.root.setRight(eval);

        
        HBox cards = new HBox(20);			//Gate Cards
        App.root.setBottom(cards);
        
        //Spawns a gatecard for each type
        for (GateType type : GateType.values()) {
            cards.getChildren().add(new GateCard(type));
        }

        
        App.trash = new TrashCard();		//trash function
        cards.getChildren().add(App.trash);
        
    }
	
	public static void LevelValueMatch(String Level, String eval) {
		
		if(eval.equals(Level)) {		//check if match == Output
        	
        	
        	VBox VBoxPaneLevels = new VBox(30);		//Main Menu 'buttons'
            App.root.setCenter(VBoxPaneLevels);
            VBoxPaneLevels.setAlignment(Pos.CENTER);
        	
        	VBoxPaneLevels.getChildren().add(new  MainMenuSpace(Select.LevelSolvedImg));
        }
		
	}
	

}
