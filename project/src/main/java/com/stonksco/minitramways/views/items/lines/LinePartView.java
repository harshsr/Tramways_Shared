package com.stonksco.minitramways.views.items.lines;

import com.stonksco.minitramways.logic.Vector2;
import com.stonksco.minitramways.views.GameView;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeLineCap;


public class LinePartView extends Group {

    private final GameView GameView;
    private final LineView LineView;


    private final ReadOnlyDoubleProperty  pxStartX;
    private final ReadOnlyDoubleProperty  pxStartY;
    private final ReadOnlyDoubleProperty  pxEndX;
    private final ReadOnlyDoubleProperty  pxEndY;

  
    private final Vector2 StartPos;
    private final Vector2 EndPos;

    public int getStart() {
        return Start;
    }

    public int getEnd() {
        return End;
    }


    private final int Start;
    private final int End;

    
    private Line LineShape;
    private final Color Color;


    public LinePartView(GameView gw, LineView line, Vector2 startPos, Vector2 endPos, int start, int end, Color color) {
        super();
        this.GameView = gw;
        this.LineView = line;
        this.StartPos = startPos;
        this.EndPos = endPos;
        this.Start = start;
        this.End = end;
        this.Color = color;

        pxStartX = gw.CellToPixelsX(startPos);
        pxStartY = gw.CellToPixelsY(startPos);
        pxEndX = gw.CellToPixelsX(endPos);
        pxEndY = gw.CellToPixelsY(endPos);

        DrawLine();
    }


   
    private void DrawLine()
    {
        LineShape = new Line();

        LineShape.startXProperty().bind(pxStartX);
        LineShape.startYProperty().bind(pxStartY);
        LineShape.endXProperty().bind(pxEndX);
        LineShape.endYProperty().bind(pxEndY);
        LineShape.strokeWidthProperty().bind(GameView.GetCellSizeX().multiply(0.2d));
        LineShape.setStroke(Color);
        LineShape.setStrokeLineCap(StrokeLineCap.ROUND);
        LineView.getChildren().add(this);
        this.getChildren().add(LineShape);
    }

 
    public double GetOrientation() {
        Vector2 startPos = this.StartPos;
        Vector2 endPos = this.EndPos;
        Vector2 lineVector = endPos.Sub(startPos);

        Vector2 normalizedLineVector = lineVector.Normalize();
        Vector2 horizontalVector = new Vector2(1,0);

        double scalar = normalizedLineVector.Scalar(horizontalVector);

        double angle = Math.acos(scalar);
        angle = Math.toDegrees(angle);

       
        if(startPos.GetY()>endPos.GetY())
            angle = -angle;

        return angle;
    }

    
    public ReadOnlyDoubleProperty GetPosXAt(double at) {

        at = at%100;
        if(at<0)
            at +=100;

        SimpleDoubleProperty res = new SimpleDoubleProperty(0);
        if(at==0)
            res.bind(pxStartX);
        else if(at==100)
            res.bind(pxEndX);
        else
        {
            res.bind(pxEndX.subtract(pxStartX).multiply((at/100d)).add(pxStartX));
        }

        return res;
    }

    public ReadOnlyDoubleProperty GetPosYAt(double at) {

        at = at%100;
        if(at<0)
            at +=100;

        SimpleDoubleProperty res = new SimpleDoubleProperty(0);
        if(at==0)
            res.bind(pxStartY);
        else if(at==100)
            res.bind(pxEndY);
        else
        {
            res.bind(pxEndY.subtract(pxStartY).multiply((at/100d)).add(pxStartY));
        }

        return res;
    }

}
