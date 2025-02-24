package com.stonksco.minitramways.logic.interactions.states;

import com.stonksco.minitramways.logic.Vector2;
import com.stonksco.minitramways.logic.interactions.ClickStateMachine;

/**
 * Final state, erases the data and returns to the initial state
 */
public class ResetClickState extends AbstractClickState{

    public ResetClickState(ClickStateMachine machine) {
        super(machine);
    }

    @Override
    public AbstractClickState NaturalTransition() {
        Action();
        return new StartState(SM);
    }

    @Override
    public void Action() {
        SM.GetData().clear();
    }
}
