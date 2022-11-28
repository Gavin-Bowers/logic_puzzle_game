package com.example;

import java.util.ArrayList;

import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class App extends Application {
    public static final int APPWIDTH = 1920;
    public static final int APPHEIGHT = 1080;

    private static Scene scene;
    private static ArrayList<LogicGate> gates = new ArrayList<LogicGate>(); //Each gate is a group containting the image, as well as wire terminals for connecting gates
    
    public static BorderPane root = new BorderPane();
    public static Tape tape;
    
    private static WirePreviewPane previewPane = new WirePreviewPane(APPWIDTH, APPHEIGHT);
    private static Rectangle forceRefresher = new Rectangle(0,0,0,0);
    //private static BorderPane borderPane = new BorderPane();

    public enum GateType {
        OR,
        AND,
        NOT,
        SPLITTER,
        NOR,
        NAND,
        XOR,
        XNOR
    }

    @Override
    public void start(Stage stage) {

        scene = new Scene(root,APPWIDTH,APPHEIGHT,Color.rgb(6,6,6));
        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
        stage.setScene(scene);
        stage.setMaximized(true); //Windowed fullscreen. If the computer is not 1080p, game assets will be the wrong size
        stage.setTitle("Logician's Folly");
        stage.getIcons().add(new Image(getClass().getResourceAsStream("icon.png")));
        stage.setOpacity(0.0); //The opacity toggling is to prevent the window for flashbanging white for a few frames while loading in
        stage.show();
        stage.setOpacity(1.0);

        root.getChildren().add(previewPane);

        forceRefresher.setFill(Color.TRANSPARENT); //Used to force refresh
        root.getChildren().add(forceRefresher);
        forceRefresher.toBack();
        
        //gates.get(0).setTranslateX(200.0); 
        HBox cards = new HBox(20);
        root.setBottom(cards);

        String[] test = {"000110", "101000"};
        App.tape = new Tape(test);
        root.setLeft(tape);

        Evaluator eval = new Evaluator();
        root.setRight(eval);

        //Spawns a gatecard for each type
        for (GateType type : GateType.values()) {
            cards.getChildren().add(new GateCard(type));
        }
        
        //Spawns a gate of each type
        /*for (GateType type : GateType.values()) {
            SpawnGate(type, APPWIDTH / 2, APPHEIGHT / 2);
        }*/

        //Post processing
        Glow glow = new Glow(0.8);

        for(Node n : root.getChildren()) {
            n.setEffect(glow);
        }

        
        
    }
    
    public static void SpawnGate(GateType type, double x, double y) {
    	gates.add(new LogicGate(type));
        gates.get(gates.size()-1).setTranslateX(x);
        gates.get(gates.size()-1).setTranslateY(y);
        gates.get(gates.size()-1).setEffect(new Glow(0.8));
    }

    public static void forceRefresh() {
        forceRefresher.toFront();
        forceRefresher.toBack();
    }

    public static void main(String[] args) {
        launch();
    }

}