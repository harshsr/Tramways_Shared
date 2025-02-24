package com.stonksco.minitramways.logic.interactions.states;

import com.stonksco.minitramways.logic.Game;
import com.stonksco.minitramways.logic.Vector2;
import com.stonksco.minitramways.logic.interactions.ClickStateMachine;

public class StartState extends AbstractClickState {

    public StartState(ClickStateMachine machine) {
        super(machine);
        StateName="Initial state";
    }


    @Override
    public AbstractClickState LeftTransition(Vector2 clicked) {
        SM.GetData().put("firstcell",clicked);
        return new LineCreationState(SM);
    }

    @Override
    public AbstractClickState LeftStationTransition(Vector2 clicked) {
        SM.GetData().put("firstcell",clicked);
        if(Game.Get().IsAtExtremity(clicked))
            return new LineExtensionState(SM);
        else
            return new LineCreationState(SM);
    }

    @Override
    public AbstractClickState RightStationTransition(Vector2 clicked) {
        SM.GetData().put("stationtodestroy",clicked);
        return new StationDestroyState(SM);
    }

    public AbstractClickState RightPartTransition(Vector2 clicked) {
        return this;
    }


    @Override
    public void Action() {}

}
