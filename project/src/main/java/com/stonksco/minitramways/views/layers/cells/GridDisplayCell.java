package com.stonksco.minitramways.views.layers.cells;

import com.stonksco.minitramways.logic.Game;
import com.stonksco.minitramways.logic.Vector2;
import com.stonksco.minitramways.views.ColorEnum;
import com.stonksco.minitramways.views.GameView;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;

public class GridDisplayCell extends CellView{

    private Ellipse E;

    public GridDisplayCell(GameView gw, Vector2 gridPos) {
        super(gw,gridPos);

        int i = (int)gridPos.GetY();
        int j = (int)gridPos.GetX();

        Color c = gw.GetColor(ColorEnum.GRID_DOT);

        SimpleDoubleProperty sizeU = new SimpleDoubleProperty();
        sizeU.bind(gw.GetCellSizeX().multiply(0.1));

        if(i==0 || j==0) {
            E = new Ellipse(0,0,3,3);
            E.radiusYProperty().bind(sizeU);
            E.radiusXProperty().bind(sizeU);
            E.translateXProperty().bind(sizeU.multiply(-1));
            E.translateYProperty().bind(sizeU.multiply(-1));
            E.setFill(c);
            StackPane.setAlignment(E, Pos.TOP_LEFT);
            this.getChildren().add(E);
        }

        if(j==Game.Get().GetMapSize().GetX()-1 && i==0) {
            E = new Ellipse(0,0,3,3);
            E.radiusYProperty().bind(sizeU);
            E.radiusXProperty().bind(sizeU);
            E.translateXProperty().bind(sizeU);
            E.translateYProperty().bind(sizeU.multiply(-1));
            E.setFill(c);
            StackPane.setAlignment(E, Pos.TOP_RIGHT);
            this.getChildren().add(E);
        }

        if(j==0 && i==Game.Get().GetMapSize().GetY()-1) {
            E = new Ellipse(0,0,3,3);
            E.radiusYProperty().bind(sizeU);
            E.radiusXProperty().bind(sizeU);
            E.translateXProperty().bind(sizeU.multiply(-1));
            E.translateYProperty().bind(sizeU);
            E.setFill(c);
            StackPane.setAlignment(E, Pos.BOTTOM_LEFT);
            this.getChildren().add(E);
        }

        E = new Ellipse(0,0,3,3);
        E.radiusYProperty().bind(sizeU);
        E.radiusXProperty().bind(sizeU);
        E.translateXProperty().bind(sizeU);
        E.translateYProperty().bind(sizeU);
        E.setFill(c);
        StackPane.setAlignment(E, Pos.BOTTOM_RIGHT);
        this.getChildren().add(E);
    }

    public ReadOnlyDoubleProperty GetPixelsX() {
        SimpleDoubleProperty res = new SimpleDoubleProperty(0);
        res.bind(this.layoutXProperty().add(Gameview.GetCellSizeX().divide(2)));

        return res;

    }
    public ReadOnlyDoubleProperty GetPixelsY() {
        SimpleDoubleProperty res = new SimpleDoubleProperty(0);
        res.bind(this.layoutYProperty().add(Gameview.GetCellSizeY().divide(2)));

        return res;

    }
}
