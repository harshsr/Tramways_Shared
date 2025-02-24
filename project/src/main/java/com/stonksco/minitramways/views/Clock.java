package com.stonksco.minitramways.views;

import com.stonksco.minitramways.logic.Game;
import javafx.animation.AnimationTimer;
import javafx.beans.property.ReadOnlyLongProperty;
import javafx.beans.property.SimpleLongProperty;


public class Clock extends AnimationTimer {

    private long startedAt = -1;
    private final float gameTimeSpeedFactor = 1;
    private long lastUpdate = 0;
    private long now = 0;
    private long elapsed = 0;
    private long gameElapsed;


    private static final Clock instance = new Clock();

    public static Clock Get() {
        return instance;
    }

    @Override
    public void handle(long l) {
        if(startedAt==-1)
            startedAt=l;
        now = l-startedAt;
        Game.Get().Update();
        GameView.FrameUpdate();
        elapsed += DeltaTime();
        gameElapsed += GameDeltaTimeNs();

        lastUpdate = now;
    }

    public long DeltaTime() {
        return now-lastUpdate;
    }

 
    public double GameDeltaTime() {
        return (double)GameDeltaTimeNs()/Math.pow(10,9);
    }

  
    public long GameDeltaTimeNs() {
        return (long)((now-lastUpdate)*gameTimeSpeedFactor);
    }

  
    public long GameElapsed() {
        return gameElapsed;
    }


}
