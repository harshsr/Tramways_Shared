package com.stonksco.minitramways.views.layers;

import com.stonksco.minitramways.logic.Game;
import com.stonksco.minitramways.logic.Vector2;
import com.stonksco.minitramways.logic.map.buildings.BuildingEnum;
import com.stonksco.minitramways.views.GameView;
import com.stonksco.minitramways.views.layers.cells.BuildingView;
import com.stonksco.minitramways.views.layers.cells.CellView;

import java.util.ArrayList;
import java.util.HashMap;

public class BuildingsLayer extends GridView {

    private final HashMap<Vector2, BuildingView> Buildings;


    public BuildingsLayer(GameView gw, Vector2 size) {
        super(gw, size);
        Fill(CellView.class);
        Buildings = new HashMap<>();
    }

    public void AddBuildingAt(Vector2 at, BuildingEnum type) {
        BuildingView b  = new BuildingView(gw,at,type);
        this.getChildren().remove(this.GetCellAt(at));
        Buildings.put(at,b);
        this.add(b,(int)at.GetX(),(int)at.GetY());
    }

    
    public void UpdateBuildings(){
        HashMap<BuildingEnum, ArrayList<Vector2>> buildings = Game.Get().GetBuildings();

     
        int buildingsCount = 0;
        for(ArrayList<Vector2> list : buildings.values()) {
            buildingsCount += list.size();
        }

        if(buildingsCount > this.Buildings.size()) {

            Game.Debug(2,"VIEW : Buildings refreshed.");

            ArrayList<Vector2> pos;
            pos = buildings.get(BuildingEnum.HOUSE);
            if (pos != null) {
                for (int i = 0; i < pos.size(); i++) {
                    AddBuildingAt(pos.get(i), BuildingEnum.HOUSE);
                }
            }
            pos = buildings.get(BuildingEnum.SHOP);
            if (pos != null) {
                for (int i = 0; i < pos.size(); i++) {
                    AddBuildingAt(pos.get(i), BuildingEnum.SHOP);
                }
            }
            pos = buildings.get(BuildingEnum.OFFICE);
            if (pos != null) {
                for (int i = 0; i < pos.size(); i++) {
                    AddBuildingAt(pos.get(i), BuildingEnum.OFFICE);
                }
            }
        }

    }


    public void UpdateBuildingsPins() {
        for(Vector2 v : Buildings.keySet()) {
            int nb = Game.Get().GetAmountOfPeople(v);
            Buildings.get(v).SetAmount(nb);
        }
    }
}
