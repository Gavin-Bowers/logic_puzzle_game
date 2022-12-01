package com.example;

import java.util.ArrayList;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class App extends Application {
    public static final int APPWIDTH = 1920;
    public static final int APPHEIGHT = 1080;

    private static Scene scene;
    private static ArrayList<LogicGate> gates = new ArrayList<LogicGate>(); //Each gate is a group containting the image, as well as wire terminals for connecting gates
    
    public static BorderPane root = new BorderPane();
    public static Tape tape;
    
    private static WirePreviewPane previewPane = new WirePreviewPane(APPWIDTH, APPHEIGHT);

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
        
        //gates.get(0).setTranslateX(200.0); 
        HBox cards = new HBox(20);
        root.setBottom(cards);

        String[] test = {"0011", "0110"};
        App.tape = new Tape(test);
        root.setLeft(tape);

        Evaluator eval = new Evaluator();
        root.setRight(eval);

        //Spawns a gatecard for each type
        for (GateType type : GateType.values()) {
            cards.getChildren().add(new GateCard(type));
        }
    }
    
    public static void SpawnGate(GateType type, double x, double y) {
    	gates.add(new LogicGate(type));
        gates.get(gates.size()-1).setTranslateX(x);
        gates.get(gates.size()-1).setTranslateY(y);
    }

    public static void main(String[] args) {
        launch();
    }

}