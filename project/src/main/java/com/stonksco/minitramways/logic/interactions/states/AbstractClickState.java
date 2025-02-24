package com.stonksco.minitramways.logic.interactions.states;

import com.stonksco.minitramways.logic.Vector2;
import com.stonksco.minitramways.logic.interactions.ClickStateMachine;
import com.stonksco.minitramways.logic.interactions.InteractionException;
import com.stonksco.minitramways.logic.map.lines.LinePart;

import java.util.HashMap;

public abstract class AbstractClickState {

    protected ClickStateMachine SM;
    protected String StateName="Undefined state";

    public AbstractClickState(ClickStateMachine machine) {
        SM = machine;
    }

    // Return the state after left click on an "empty" cell

    public AbstractClickState LeftTransition(Vector2 clicked) throws InteractionException {
        return this;
    }

    // Return the state after left click on a station
    public AbstractClickState LeftStationTransition(Vector2 clicked) throws InteractionException {
        return this;
    }

    // Return the state after Right click on an "empty" cell
    public AbstractClickState RightTransition(Vector2 clicked) throws InteractionException {
        return this;
    }

    // Return the state after right click to a station
    public AbstractClickState RightStationTransition(Vector2 clicked) throws InteractionException {
        return this;
    }

    // Return the state after right click on a part of a line
    public AbstractClickState RightPartTransition(LinePart clicked) throws InteractionException {
        return this;
    }


    // Return the state after having taken the action
    public AbstractClickState NaturalTransition() {
        return this;
    }

    // Actions to be carried out according to the interaction
    public abstract void Action() throws InteractionException;

    @Override
    public String toString() {
        return "[ ("+StateName+"):"+SM.GetData()+" ]";
    }

    public HashMap<String,Object> getData() {
        return (HashMap<String, Object>) SM.GetData().clone();
    }

}
