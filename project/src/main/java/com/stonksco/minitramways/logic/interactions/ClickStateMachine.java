package com.stonksco.minitramways.logic.interactions;

import com.stonksco.minitramways.logic.Game;
import com.stonksco.minitramways.logic.Vector2;
import com.stonksco.minitramways.logic.interactions.states.AbstractClickState;
import com.stonksco.minitramways.logic.interactions.states.StartState;
import com.stonksco.minitramways.logic.map.buildings.Building;
import com.stonksco.minitramways.logic.map.buildings.Station;

import java.util.HashMap;

// Manages left and right click interactions
public class ClickStateMachine {

    private AbstractClickState CurrentState;
    private HashMap<String,Object> Data;

    public ClickStateMachine() {
        Data = new HashMap<>();
        CurrentState = new StartState(this);
    };

    public AbstractClickState SendLeftClick(Vector2 At) throws InteractionException {
        if(At != null) {
            Building bAt = Game.Get().GetMap().GetBuildingAt((Vector2)At);
            if(bAt instanceof Station) {
                // If we have left click on a station
                CurrentState = CurrentState.LeftStationTransition((Vector2)At);
            } else if (bAt==null) {
                // If we have left click on an empty box
                CurrentState = CurrentState.LeftTransition((Vector2)At);
            }

            NaturalTransition();

            Game.Debug(1,"Click state machine updated : "+CurrentState);

        }
        return CurrentState;
    }

    public AbstractClickState SendRightClick(Object at) throws InteractionException {
        if(at instanceof Vector2) {

            Building bAt = Game.Get().GetMap().GetBuildingAt((Vector2)at);
            if(bAt instanceof Station) {
                // If we have a right click on a station
                CurrentState = CurrentState.RightStationTransition((Vector2)at);
            } else {
                // If we have a right click on an "empty" box
                CurrentState = CurrentState.RightTransition((Vector2)at);
            }
        }

        NaturalTransition();

        Game.Debug(1,"Click state machine updated : "+CurrentState);

        return CurrentState;
    }

    private void NaturalTransition() {
        // We carry out natural transitions as long as they give another state than the current state
        boolean Exit = false;
        AbstractClickState PrecState = CurrentState;
        while(!Exit) {
            CurrentState = CurrentState.NaturalTransition();
            if(PrecState==CurrentState)
                Exit=true;
            PrecState = CurrentState;
        }
    }

    public HashMap<String,Object> GetData() {
        return Data;
    }

    public AbstractClickState GetCurrentState() {
        return CurrentState;
    }
}
