package com.stonksco.minitramways.views.layers;

import com.stonksco.minitramways.logic.Game;
import com.stonksco.minitramways.logic.Vector2;
import com.stonksco.minitramways.views.GameView;
import com.stonksco.minitramways.views.layers.cells.CellView;
import com.stonksco.minitramways.views.layers.cells.StationView;

import java.util.ArrayList;
import java.util.HashMap;

public class StationsLayer extends GridView{

    private final HashMap<Vector2, StationView> stations;

    public StationsLayer(GameView gw, Vector2 size) {
        super(gw, size);
        stations = new HashMap<>();
        Fill(CellView.class);

        if(Game.Get().GetDebug()>2)
            this.setGridLinesVisible(true);

    }

   
    public void AddStationAt(Vector2 at) {
        StationView s = new StationView(gw,at);
        s.Enable();
        stations.remove(this.GetCellAt(at));
        stations.put(at,s);
        this.add(s,(int)at.GetX(),(int)at.GetY());
    }

    public ArrayList<Vector2> UpdateStations() {
        ArrayList<Vector2> res = new ArrayList<>();
        this.stations.clear();
        this.getChildren().clear();
        this.getColumnConstraints().clear();
        this.getRowConstraints().clear();
        this.Fill(CellView.class);
        for(Vector2 v : Game.Get().GetStations()) {
            AddStationAt(v);
            res.add(v);
        }
        return res;
    }

    public void ShowRadiusOf(Vector2 cell) {
        StationView sv = stations.get(cell);
        if(sv != null)
            sv.ShowRadius();
    }

    public void HideRadiusOf(Vector2 cell) {
        StationView sv = stations.get(cell);
        if(sv != null)
            sv.HideRadius();
    }

   
    public void UpdateStationsPins() {
        for(Vector2 v : stations.keySet()) {
            int nb = Game.Get().GetAmountOfPeople(v);
            stations.get(v).SetAmount(nb);
        }
    }

}
