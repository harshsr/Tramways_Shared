package com.stonksco.minitramways.views.layers;

import com.stonksco.minitramways.logic.Vector2;
import com.stonksco.minitramways.views.ColorEnum;
import com.stonksco.minitramways.views.GameView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RadiusLayer extends Pane {

    private final GameView GameView;

    private final HashMap<Vector2,Circle> Circles;

    public RadiusLayer(GameView gw) {
        super();
        this.GameView = gw;
        Circles = new HashMap<>();
    }

    public void ShowRadiusAt(Vector2 at) {
        if(Circles.get(at) == null) {
            Circle c = new Circle();
            c.setRadius(3.5d*GameView.GetCellSizeX().get());
            c.translateXProperty().bind(GameView.CellToPixelsX(at));
            c.translateYProperty().bind(GameView.CellToPixelsY(at));

            c.setFill(GameView.GetColor(ColorEnum.GRID_DOT));
            this.getChildren().add(c);
            Circles.put(at,c);
        }
    }

    public void HideRadiusAt(Vector2 at) {
        Circle c = Circles.get(at);
        if(c!=null) {
            this.getChildren().remove(c);
            Circles.remove(at,c);
        }
    }

    
    public void UpdateStationsSet(ArrayList<Vector2> stationsSet) {
        for(Map.Entry<Vector2,Circle> e : Circles.entrySet()) {
            if(!stationsSet.contains(e.getKey()))
                HideRadiusAt(e.getKey());
        }
    }





}
