package com.stonksco.minitramways.logic.map.generation;

import com.stonksco.minitramways.logic.Game;
import com.stonksco.minitramways.logic.map.Area;
import com.stonksco.minitramways.logic.map.GameMap;
import com.stonksco.minitramways.views.Clock;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.SortedMap;

public class BuildingsGenerator {

    private GameMap GM;


    /**
     * Duration remaining before the next generation in seconds
     * Duration is shorter each round
     */
    private double RemainingTimeUntilGen = 0;


    public BuildingsGenerator(GameMap gm) {
        this.GM = gm;
    }


    // Called to each frame, this function manages the progressive generation of buildings
    public void BuildingsGeneration() {
        if(RemainingTimeUntilGen<=0) {
            // Generate in less dense areas first

            // Classify areas by density
            ArrayList<Area> Areas = new ArrayList<>(GM.GetAreas().values());


            Collections.sort(Areas, new Comparator<Area>() {
                @Override
                public int compare(Area O1, Area O2) {
                    return Double.compare(O1.GetDensity(),O2.GetDensity());
                }
            });

            boolean Res = false;
            for(Area A : Areas) {
                // Firts iteration = less dense areas
                // Ask to generate a building
                Res = A.GenerateBuilding();
                // If the generation succeeds, stop
                if(Res)
                    break;
                // If the generation fails, we go to the next area and we try again
            }

            // If we have failed to generate a building in any area, then we consider that the areas are full
            // We therefore block generation to avoid unnecessary calculations
            if(!Res) {
                RemainingTimeUntilGen = Double.POSITIVE_INFINITY;
                Game.Debug(1,"Cannot generate more buildings. Generation ended.");
            }
            else {
                // Calculate the new Countdown
                RemainingTimeUntilGen = 20 + (Math.random()*10d);
                Game.Debug(1,"New buildings generated.");
            }
        } else {
            // No generation
            RemainingTimeUntilGen -= Clock.Get().GameDeltaTime();
        }

    }

    // Initial buildings generation
    public void InitBuildings() {
        for(Area A : GM.GetAreas().values()) {
            A.GenerateBuilding();
        }
    }

}
