package com.stonksco.minitramways.views.layers;

import com.stonksco.minitramways.logic.Game;
import com.stonksco.minitramways.logic.Vector2;
import com.stonksco.minitramways.logic.Vector2Couple;
import com.stonksco.minitramways.logic.people.People;
import com.stonksco.minitramways.views.ColorEnum;
import com.stonksco.minitramways.views.GameView;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TargetsLayer extends Pane {

    private GameView GameView;

    public TargetsLayer(GameView gw) {
        super();
        this.GameView = gw;
    }

    public void Enter(Vector2 cell) {
        HashMap<Vector2,Integer> nbTargets = new HashMap<>();

        ArrayList<People> ppl = Game.Get().GetPeopleAt(cell);
        for(People p : ppl) {
            if(nbTargets.get(p.GetTargetPos())==null)
                nbTargets.put(p.GetTargetPos(),0);
            nbTargets.put(p.GetTargetPos(),nbTargets.get(p.GetTargetPos())+1);
        }

        for(Map.Entry<Vector2,Integer> e : nbTargets.entrySet()) {
            StackPane n = CreateTargetDisplay(e.getValue());
            n.translateXProperty().bind(GameView.CellToPixelsX(e.getKey()).add(n.widthProperty().divide(-2)));
            n.translateYProperty().bind(GameView.CellToPixelsY(e.getKey()).add(n.heightProperty().divide(-2)));
            this.getChildren().add(n);
        }
    }

    public void Exit(Vector2 cell) {
        this.getChildren().clear();
    }

    private StackPane CreateTargetDisplay(int nbOfPeople) {
        StackPane res = new StackPane();
        Circle c1 = new Circle();
        c1.radiusProperty().bind(GameView.GetCellSizeX().multiply(1.2d+(0.2d*nbOfPeople)));
        c1.setFill(GameView.GetColor(ColorEnum.TARGET_COLOR));
        c1.setStroke(GameView.GetColor(ColorEnum.TARGET_OUTLINE_COLOR));
        c1.strokeWidthProperty().bind(GameView.GetCellSizeX().multiply(0.15d+(0.15d*nbOfPeople)));
        c1.strokeTypeProperty().setValue(StrokeType.INSIDE);


        res.getChildren().add(c1);
        return res;
    }
}
