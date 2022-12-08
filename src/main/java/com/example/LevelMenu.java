package com.example;

//controls the Level Menu - Mika

import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;


public class LevelMenu extends ImageView{
	public Levels type;   
	public static String levelSelected;
	    
    LevelMenu(){} //Don't use, this is just here so Java doesn't complain
    
    LevelMenu(Levels type) {
        this.type = type;
        try {
        	String name = "";
            switch(type) { //This will be extended to include more levels in the future
                case OOOO:
                	name = "0000";
                    break;
                case OOOI:
                	name = "0001";
                    break;
                case OOIO:
                	name = "0010";
                    break;
                    
                case OOII:
                	name = "0011";
                    break;
                 
                case OIOO:
                	name = "0100";
                    break;
                    
                case OIOI:
                	name = "0101";
                    break;
                    
                case OIIO:
                	name = "0110";
                    break;
                    
                case OIII:
                	name = "0111";
                    break;
                    
                case IOOO:
                	name = "1000";
                    break;
                    
                case IOOI:
                	name = "1001";
                    break;
                    
                case IOIO:
                	name = "1010";
                    break;
                    
                case IOII:
                	name = "1011";
                    break;
                 
                case IIOO:
                	name = "1100";
                    break;
                    
                case IIOI:
                	name = "1101";
                    break;
                    
                case IIIO:
                	name = "1110";
                    break;
                    
                case IIII:
                    name = "1111";
                    break;
       
                default:
                    name = "0000";
                    System.out.println("How did you get here?");
                    break;
            }
            
            String fileName = name + ".jfif";
            this.setImage(new Image(getClass().getResourceAsStream(fileName)));
            this.setX(0);
            this.setY(0);
            this.setFitHeight(100);	//will fix + standardize latter, this is for testing
            this.setFitWidth(250);
            LevelClicked(this, name);	//Allows click to load level
            //LevelClicked(this, name);
            
            
        } catch(Exception e) {
            System.out.println("Error: Invalid type for LevelSelect (or another error in this code block): " + type);
        }
    }
    
    private void LevelClicked(LevelMenu self, String level) {
        this.setOnMousePressed(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent event) {
        	
        	levelSelected = level;
        	WorkSpace.LoadLevelModeSpace(level);
        }});
    }
    
    
  //level stuff (does not work with 0 or 1, so I used O and I)--------------------------Mika
    public enum Levels {
        OOOO,
        OOOI,
        OOIO,
        OOII,
        OIOO,
        OIOI,
        OIIO,
        OIII,
        IOOO,
        IOOI,
        IOIO,
        IOII,
        IIOO,
        IIOI,
        IIIO,
        IIII
    }
    
    //-----------------------Level Menu Spawn stuff-------------------------
    public static void LevelMenuSpawn() {
    	//Spawns Level Menu------------------------- Mika
    	
    	ClearScreen.All();					//clears screen
        
	    GridPane GridPaneLevels = new GridPane();
        App.root.setCenter(GridPaneLevels);
        
        int gap = 50;	//gap between level cards
        GridPaneLevels.setHgap(gap);
        GridPaneLevels.setVgap(gap);
        	
        	int num = 0;
        	int row = 0;
        	int col = 0;
        	
        	for (Levels type : Levels.values()) {	//spawns level cards in a 4 by 4 grid
        		
        		GridPaneLevels.add(new LevelMenu(type), row, col, 1, 1);
        		
        		num++;
        		row = num%4;
        		col = num/4;
        	}
        	
    }
    
  
}
