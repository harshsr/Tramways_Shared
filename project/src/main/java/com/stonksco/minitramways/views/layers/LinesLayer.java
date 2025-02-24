package com.stonksco.minitramways.views.layers;

import com.stonksco.minitramways.logic.Game;
import com.stonksco.minitramways.logic.Vector2;
import com.stonksco.minitramways.views.ColorEnum;
import com.stonksco.minitramways.views.GameView;
import com.stonksco.minitramways.views.items.lines.LineView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LinesLayer extends Pane {

    private final GameView GameView;

    private final HashMap<Integer,LineView> Lines;

    public LinesLayer(GameView gw) {
        super();
        this.GameView = gw;
        Lines = new HashMap<>();
    }


    public Color GetColorFor(int id){
        Color c = GameView.GetColor(ColorEnum.values()[GetColorId(id)]);
        return c;
    }
    public int GetColorId(int id) {
        int res = id%8;
        Game.Debug(3,"Found color "+res+" for line "+id);
        return res;
    }

    public boolean AddLine(int lineID) {
        boolean res = false;
        List<Map.Entry<Vector2,Vector2>> parts = Game.Get().GetPartsVectorsOf(lineID);
        if(parts.size()>0) {
            this.Lines.put(lineID,new LineView(GameView,this,lineID));
            res=true;
        } else {
            Game.Debug(1,"VIEW : Error when retrieving line parts of line "+lineID+" from Game !");
        }
        return res;
    }

    public void RemoveLine(int lineID) {
        if(Lines.get(lineID)!=null) {
            Lines.get(lineID).Remove();
            Lines.remove(lineID);
        }
    }

  
    public void Update() {
        for(LineView l : Lines.values()) {
            l.UpdateTrams();
        }
    }

}
