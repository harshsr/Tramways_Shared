package com.stonksco.minitramways.views.layers.cells;

import com.stonksco.minitramways.logic.Vector2;
import com.stonksco.minitramways.views.GameView;
import javafx.scene.layout.StackPane;


public class CellView extends StackPane {

    protected Vector2 GridPos;
    protected GameView Gameview;

    public CellView(GameView gw, Vector2 gridPos) {
        this.Gameview = gw;
        this.GridPos = gridPos;
    }

    public Vector2 GetGridPos() {
        return GridPos.clone();
    }
}
