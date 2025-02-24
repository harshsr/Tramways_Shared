package com.stonksco.minitramways;

import com.stonksco.minitramways.control.MapController;
import com.stonksco.minitramways.logic.Game;
import com.stonksco.minitramways.views.GameView;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.stage.Stage;

public class MiniTramways extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        System.out.println("Launching Mini Tramways...");


        if(getParameters().getNamed().containsKey("debug")) {
            int nb = Integer.valueOf(getParameters().getNamed().get("debug"));
            if (nb > 0)
                Game.Get().SetDebug(nb);
        }

        primaryStage.setResizable(true);
        primaryStage.setTitle("Mini Tramways");

        Group root = new Group();

        MapController controller = new MapController();

        GameView gw = new GameView(root,primaryStage,controller);
        primaryStage.setScene(gw);
        primaryStage.show();
        gw.Enable();







    }
}
