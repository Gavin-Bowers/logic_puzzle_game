package com.example;



//controls the first menu (Free Design or Level Mode) - Mika


import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

public class MainMenuSpace extends ImageView { //ImageView
	
	public Select type;   
	
	public static void MainMenuScene() {
		
		
		ClearScreen.All();
		
		VBox VBoxPaneLevels = new VBox(30);		//Main Menu 'buttons'
        App.root.setCenter(VBoxPaneLevels);
        VBoxPaneLevels.setAlignment(Pos.CENTER);
        
        VBoxPaneLevels.getChildren().add(new  MainMenuSpace(Select.FreeDesignMENU));
        VBoxPaneLevels.getChildren().add(new  MainMenuSpace(Select.LevelModeMENU));
        
	}
	
	//Image load-----------------------------------------------------
	
	public enum Select {
		 FreeDesignMENU,
		 LevelModeMENU,
		 LevelSolvedImg
		
	    }
	
	MainMenuSpace(){}
	
	MainMenuSpace(Select type){
		 this.type = type;
	        try {
	        	String filename = "";
	            switch(type) { //This will be extended to include more levels in the future
	                case FreeDesignMENU:
	                	filename = "FreeDesignMENU.jfif";
	                    break;
	                    
	                case LevelModeMENU:
	                	filename = "LevelModeMENU.jfif";
	                    break;
	                    
	                case LevelSolvedImg:
	                	filename = "LevelSolvedImg.jfif";
	                    break;
	                
	       
	                default:
	                	filename = "LevelModeMENU.jfif";
	                    System.out.println("How did you get here?");
	                    break;
	            }
		
		
		
	this.setImage(new Image(getClass().getResourceAsStream(filename)));
    this.setX(0);
    this.setY(0);
    this.setFitHeight(200);	
    this.setFitWidth(900);
    ClickedOnMENU(this, type);	
    
	        } catch(Exception e) {
	            System.out.println("Error: Invalid filename for MainModeMENU (or another error in this code block): " + type);
	        }
    
	}
	      
	
	//----------------------- Menu Spawn stuff-------------------------
    
	        //main menu clickables
	        private void ClickedOnMENU(MainMenuSpace self, Select type) {
	            this.setOnMousePressed(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent event) {
	            	
	            	
	            	if(type == Select.FreeDesignMENU) {
	            		WorkSpace.LoadFreeDesignSpace(); 	//load free mode
		            }
	            	
	            	if(type ==  Select.LevelModeMENU) {
	            		LevelMenu.LevelMenuSpawn();			//loads level menu
	            	}
	            }});
	        }
	        
}
