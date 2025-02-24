package com.stonksco.minitramways.views.layers;

import com.stonksco.minitramways.logic.Game;
import com.stonksco.minitramways.logic.map.Area;
import com.stonksco.minitramways.views.GameView;
import com.stonksco.minitramways.views.items.areas.AreaView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class AreasLayer extends Pane {

    private final GameView GameView;

    private final ArrayList<AreaView> Areas;

    private AreaView Area;

    public AreasLayer(GameView gw) {
        super();
        this.GameView = gw;
        Areas = new ArrayList<AreaView>();

        for(Area a : Game.Get().GetAreas().values()) {
            AddArea(a);
        }

        if(Game.Get().GetDebug()>2)
            this.setBorder(new Border(new BorderStroke(Color.PURPLE, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));


    }

    public void AddArea(Area a){
        Area = new AreaView(GameView,this,a);
        this.getChildren().add(Area);
        Areas.add(Area);
    }

}
