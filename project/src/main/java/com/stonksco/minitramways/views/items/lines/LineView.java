package com.stonksco.minitramways.views.items.lines;

import com.stonksco.minitramways.logic.Game;
import com.stonksco.minitramways.logic.Vector2;
import com.stonksco.minitramways.logic.map.lines.Tramway;
import com.stonksco.minitramways.views.Clock;
import com.stonksco.minitramways.views.GameView;
import com.stonksco.minitramways.views.layers.LinesLayer;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.scene.Group;
import javafx.scene.paint.Color;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class LineView extends Group {

    private final GameView GameView;
    private final LinesLayer Layer;
 
    private HashMap<Integer,TramView> Trams;

    private final Color Color;
    private final int ColorId;
    private final int LineID;

   
    private final HashMap<Vector2, LinePartView> Parts;

    public LineView(GameView gw, LinesLayer layer, int lineID) {
        super();
        this.GameView = gw;
        this.Layer = layer;
        Parts = new HashMap<>();
        this.LineID = lineID;

        this.Color = layer.GetColorFor(lineID);
        this.ColorId = layer.GetColorId(lineID);

        List<Map.Entry<Vector2,Vector2>> partsVectors = Game.Get().GetPartsVectorsOf(lineID);


        int pos = Game.Get().GetFirstIndexOf(lineID);
        for(Map.Entry v : partsVectors) {

            Vector2 posV = new Vector2(pos,pos+100);
            LinePartView lp = AddPart(posV,(Vector2)v.getKey(),(Vector2)v.getValue());
            Game.Debug(2,"VIEW : Drawn line part "+ v.getKey() +"-----"+ v.getValue() +"  at indexes "+posV);
            pos+=100;
        }

        layer.getChildren().add(this);

    }

    private void AddTram(double at,int peopleAmount) {
        if(Trams==null)
            Trams = new HashMap<>();

        TramView tv = new TramView(this,at,GameView,ColorId,peopleAmount);
        Trams.put(Trams.size(),tv);
        this.getChildren().add(tv);

    }

    private LinePartView AddPart(Vector2 position, Vector2 from, Vector2 to) {
        LinePartView lp = new LinePartView(GameView,this,from,to,(int)position.GetX(),(int)position.GetY(),Color);
        this.Parts.put(position,lp);
        return lp;
    }

    public void Remove() {
        for(LinePartView lp : Parts.values()) {
            lp.getChildren().clear();
        }
        this.getChildren().clear();
        this.Parts.clear();
    }

    public ReadOnlyDoubleProperty GetPosXAt(double at) {
        return GetPartAt(at).GetPosXAt(at);
    }

    public ReadOnlyDoubleProperty GetPoxYAt(double at) {
        return GetPartAt(at).GetPosYAt(at);
    }

    public LinePartView GetPartAt(double at) {
        LinePartView res = null;
        for(Map.Entry e : Parts.entrySet()) {
            if(((Vector2)e.getKey()).GetX() <=at && ((Vector2)e.getKey()).GetY()>at) {
                res=(LinePartView)e.getValue();
                break;
            }
        }
        return res;
    }

    private double timeSinceLastTramUpdate = 0;

    public void UpdateTrams() {
        if(timeSinceLastTramUpdate > 0.03) {
            if (Trams == null)
                Trams = new HashMap<>();

            for (TramView tv : Trams.values()) {
                this.getChildren().remove(tv);
            }
            this.Trams.clear();

            for (Tramway t : Game.Get().GetTramsOf(this.LineID)) {
                AddTram(t.GetLinePos(),t.Amount());
            }
            timeSinceLastTramUpdate = 0;
        } else {
            timeSinceLastTramUpdate+= Clock.Get().GameDeltaTime();
        }



    }




}
