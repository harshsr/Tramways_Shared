package com.stonksco.minitramways.logic.interactions.states;

import com.stonksco.minitramways.logic.Game;
import com.stonksco.minitramways.logic.Vector2;
import com.stonksco.minitramways.logic.interactions.ClickStateMachine;

public class StationDestroyState extends AbstractClickState{

    public StationDestroyState(ClickStateMachine machine) {
        super(machine);
        StateName="Station Destroy";
    }

    @Override
    public AbstractClickState NaturalTransition() {
        Action();
        return new ResetClickState(SM);
    }

    @Override
    public void Action() {
        Game.Get().DestroyStation((Vector2)SM.GetData().get("stationtodestroy"));
    }
}
