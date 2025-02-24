package com.stonksco.minitramways.logic.map.generation;

import com.stonksco.minitramways.logic.Game;
import com.stonksco.minitramways.logic.map.Area;
import com.stonksco.minitramways.logic.map.GameMap;
import com.stonksco.minitramways.views.Clock;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class PeopleGenerator {

    private GameMap GM;


    /**
     * Duration remaining before the next generation in seconds
     */
    private double RemainingTimeUntilGen = 0;


    public PeopleGenerator(GameMap gm) {
        this.GM = gm;
    }


    /**
     * Called to each frame, this function manages the generation of people
     */
    public void PeopleGeneration() {
        if(RemainingTimeUntilGen<=0) {
            // Generate a random number of people in random houses


            int nbToGen = 2+((int)(Math.random()*2d));
            //int nbToGen = 1;
            for(int i = 0; i<nbToGen; i++) {
                GM.GetRandomHouse().Spawn();
            }

            RemainingTimeUntilGen = 15 + (Math.random()*10d);
            Game.Debug(1,"New people spawned.");
        } else {
            // No generation
            RemainingTimeUntilGen -= Clock.Get().GameDeltaTime();
        }

    }

}
