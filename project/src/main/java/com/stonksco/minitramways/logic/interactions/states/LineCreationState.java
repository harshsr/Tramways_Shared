package com.stonksco.minitramways.logic.interactions.states;

import com.stonksco.minitramways.logic.Game;
import com.stonksco.minitramways.logic.Vector2;
import com.stonksco.minitramways.logic.interactions.ClickStateMachine;
import com.stonksco.minitramways.logic.interactions.InteractionException;
import com.stonksco.minitramways.logic.map.lines.LinePart;

// Current line creation
public class LineCreationState extends AbstractClickState {


    public LineCreationState(ClickStateMachine machine) {
        super(machine);
        StateName = "Line creation";
    }

    @Override
    public AbstractClickState LeftTransition(Vector2 clicked) throws InteractionException {
        SM.GetData().put("secondcell",clicked);
        Action();
        return new LineExtensionState(SM);
    }

    @Override
    public AbstractClickState LeftStationTransition(Vector2 clicked) throws InteractionException {
        SM.GetData().put("secondcell",clicked);
        Action();
        return new LineExtensionState(SM);
    }

    @Override
    public AbstractClickState RightTransition(Vector2 clicked) {
        return new ResetClickState(SM);
    }

    @Override
    public AbstractClickState RightStationTransition(Vector2 clicked) {
        return new ResetClickState(SM);
    }

    @Override
    public AbstractClickState RightPartTransition(LinePart clicked) {
        return new ResetClickState(SM);
    }

    @Override
    public void Action() throws InteractionException {
        Vector2 V1 = (Vector2) SM.GetData().get("firstcell");
        Vector2 V2 = (Vector2) SM.GetData().get("secondcell");
        Integer CreationCost = (2*35)+(int)(Vector2.AbstractDistance(V1,V2)/7);
        if(Game.Get().GetMoney()<CreationCost) {
            throw new InteractionException("money");
        } else {
            Integer CreatedID = Game.Get().CreateLine(V1,V2);
            if(CreatedID!=null) {
                SM.GetData().put("createdid",CreatedID);
                Game.Get().AddMoney(-1*CreationCost);
            } else {
                throw new InteractionException("obstructed");
            }
        }
    }
}
