package com.stonksco.minitramways.views.items.areas;

import com.stonksco.minitramways.logic.Vector2;
import com.stonksco.minitramways.logic.map.Area;
import com.stonksco.minitramways.logic.map.AreaTypes;
import com.stonksco.minitramways.views.ColorEnum;
import com.stonksco.minitramways.views.GameView;
import com.stonksco.minitramways.views.layers.AreasLayer;
import javafx.scene.layout.Pane;
import javafx.scene.shape.*;

import java.util.ArrayList;

public class AreaView extends Pane {

    private final GameView GameView;
    private final AreaTypes Type;

    private final Path AreaShape;

    public AreaView(GameView gw, AreasLayer layer, Area a) {

        super();
        this.GameView = gw;
        this.Type = a.GetType();

        AreaShape = new Path();


        ArrayList<Vector2> edgeCells = new ArrayList<Vector2>();

        switch(Type) {
            case office:
                AreaShape.setFill(gw.GetColor(ColorEnum.OFFICE_BACKGROUND));
                AreaShape.setStroke(gw.GetColor(ColorEnum.OFFICE_BORDER));

                edgeCells.add(new Vector2(35,6));
                edgeCells.add(new Vector2(34,6));
                edgeCells.add(new Vector2(33,6));
                edgeCells.add(new Vector2(32,6));
                edgeCells.add(new Vector2(31,6));
                edgeCells.add(new Vector2(30,6));
                edgeCells.add(new Vector2(29,7));
                edgeCells.add(new Vector2(28,7));
                edgeCells.add(new Vector2(28,8));
                edgeCells.add(new Vector2(28,9));
                edgeCells.add(new Vector2(29,10));
                edgeCells.add(new Vector2(29,11));
                edgeCells.add(new Vector2(28,12));
                edgeCells.add(new Vector2(28,13));
                edgeCells.add(new Vector2(28,14));
                edgeCells.add(new Vector2(29,15));
                edgeCells.add(new Vector2(30,15));
                edgeCells.add(new Vector2(31,16));
                edgeCells.add(new Vector2(32,16));
                edgeCells.add(new Vector2(33,16));
                edgeCells.add(new Vector2(34,16));
                edgeCells.add(new Vector2(35,16));



                break;
            case residential:
                AreaShape.setFill(gw.GetColor(ColorEnum.RESIDENTIAL_BACKGROUND));
                AreaShape.setStroke(gw.GetColor(ColorEnum.RESIDENTIAL_BORDER));

                edgeCells.add(new Vector2(0,5));
                edgeCells.add(new Vector2(1,5));
                edgeCells.add(new Vector2(3,5));
                edgeCells.add(new Vector2(4,6));
                edgeCells.add(new Vector2(5,6));
                edgeCells.add(new Vector2(6,6));
                edgeCells.add(new Vector2(7,7));
                edgeCells.add(new Vector2(7,8));
                edgeCells.add(new Vector2(7,9));
                edgeCells.add(new Vector2(7,10));
                edgeCells.add(new Vector2(6,11));
                edgeCells.add(new Vector2(6,12));
                edgeCells.add(new Vector2(6,13));
                edgeCells.add(new Vector2(6,14));
                edgeCells.add(new Vector2(6,15));
                edgeCells.add(new Vector2(6,17));
                edgeCells.add(new Vector2(7,18));
                edgeCells.add(new Vector2(7,19));
                edgeCells.add(new Vector2(7,20));
                edgeCells.add(new Vector2(8,21));
                edgeCells.add(new Vector2(8,22));
                edgeCells.add(new Vector2(0,22));

                break;
            case shopping:
                AreaShape.setFill(gw.GetColor(ColorEnum.COMMERCIAL_BACKGROUND));
                AreaShape.setStroke(gw.GetColor(ColorEnum.COMMERCIAL_BORDER));


                edgeCells.add(new Vector2(13,0));
                edgeCells.add(new Vector2(13,1));
                edgeCells.add(new Vector2(13,2));
                edgeCells.add(new Vector2(14,3));
                edgeCells.add(new Vector2(14,4));
                edgeCells.add(new Vector2(14,5));
                edgeCells.add(new Vector2(15,6));
                edgeCells.add(new Vector2(16,5));
                edgeCells.add(new Vector2(17,5));
                edgeCells.add(new Vector2(18,4));
                edgeCells.add(new Vector2(19,4));
                edgeCells.add(new Vector2(20,4));
                edgeCells.add(new Vector2(21,5));
                edgeCells.add(new Vector2(22,5));
                edgeCells.add(new Vector2(23,6));
                edgeCells.add(new Vector2(24,5));
                edgeCells.add(new Vector2(24,4));
                edgeCells.add(new Vector2(24,3));
                edgeCells.add(new Vector2(24,2));
                edgeCells.add(new Vector2(23,1));
                edgeCells.add(new Vector2(23,0));


                break;
        }


        Vector2 firstV = edgeCells.get(0);
        int minX = (int)firstV.GetX();
        int maxX = (int)firstV.GetX();
        int minY = (int)firstV.GetY();
        int maxY = (int)firstV.GetY();

        for(int i = 1; i<edgeCells.size(); i++) {
            if(i==1) {
                MoveTo from = new MoveTo();
                from.xProperty().bind(gw.CellToPixelsX(edgeCells.get(i-1)));
                from.yProperty().bind(gw.CellToPixelsY(edgeCells.get(i-1)));
                AreaShape.getElements().add(from);
            }

            Vector2 v = edgeCells.get(i);
            if(v.GetX()<minX)
                minX=(int)v.GetX();
            if(v.GetX()>maxX)
                maxX=(int)v.GetX();
            if(v.GetY()<minY)
                minY=(int)v.GetY();
            if(v.GetY()>maxY)
                maxY=(int)v.GetY();

            QuadCurveTo to = new QuadCurveTo();
            to.xProperty().bind(gw.CellToPixelsX(edgeCells.get(i)));
            to.yProperty().bind(gw.CellToPixelsY(edgeCells.get(i)));
            to.controlXProperty().bind(Vector2.GetMidPointProperty(gw.CellToPixelsX(edgeCells.get(i-1)), gw.CellToPixelsX(edgeCells.get(i))));
            to.controlYProperty().bind(Vector2.GetMidPointProperty(gw.CellToPixelsY(edgeCells.get(i-1)), gw.CellToPixelsY(edgeCells.get(i))));
            AreaShape.getElements().add(to);
        }

        int sizeX = maxX-minX;
        int sizeY = maxY-minY;

        AreaShape.getElements().add(new ClosePath());
        AreaShape.fillRuleProperty().setValue(FillRule.NON_ZERO);
        AreaShape.setScaleX(1+(1d/sizeX));
        AreaShape.setScaleY(1+(1d/sizeY));
        AreaShape.strokeWidthProperty().bind(gw.GetCellSizeX().multiply(0.25d));
        AreaShape.strokeTypeProperty().setValue(StrokeType.OUTSIDE);
        AreaShape.strokeLineCapProperty().setValue(StrokeLineCap.ROUND);

        this.getChildren().add(AreaShape);


    }
}
