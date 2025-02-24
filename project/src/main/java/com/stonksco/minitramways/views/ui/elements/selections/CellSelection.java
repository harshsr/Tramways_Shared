package com.stonksco.minitramways.views.ui.elements.selections;

import com.stonksco.minitramways.logic.Vector2;
import com.stonksco.minitramways.views.ColorEnum;
import com.stonksco.minitramways.views.GameView;
import eu.hansolo.tilesfx.tools.GradientLookup;
import javafx.scene.Group;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class CellSelection extends Group {

    private final GameView GameView;
    private final Vector2 CellPos;

    public CellSelection(GameView gw, Vector2 pos) {
        this.GameView = gw;
        CellPos=pos;

        Show();
    }

    public void Show() {
        Circle c = new Circle();
        c.radiusProperty().bind(GameView.GetCellSizeX().multiply(0.6d));
        c.setFill(GameView.GetColor(ColorEnum.CELL_SELECTION));
        c.centerYProperty().bind(GameView.CellToPixelsY(CellPos).add(GameView.GridPosY()));
        c.centerXProperty().bind(GameView.CellToPixelsX(CellPos).add(GameView.GridPosX()));
        this.getChildren().add(c);
    }

    public void Hide() {
        this.getChildren().clear();
    }





}
