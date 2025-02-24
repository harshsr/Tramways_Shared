package com.stonksco.minitramways.logic.interactions.states;

import com.stonksco.minitramways.logic.Game;
import com.stonksco.minitramways.logic.Vector2;
import com.stonksco.minitramways.logic.interactions.ClickStateMachine;
import com.stonksco.minitramways.logic.interactions.InteractionException;
import com.stonksco.minitramways.logic.map.lines.LinePart;

import java.util.HashMap;

public class LineExtensionState extends AbstractClickState{

    public LineExtensionState(ClickStateMachine machine) {
        super(machine);
        StateName = "Line extension";
        HashMap<String,Object> DataMap = SM.GetData();
        if(DataMap.containsKey("secondcell")) {
            DataMap.remove("firstcell");
            DataMap.put("firstcell", DataMap.get("secondcell"));
            DataMap.remove("secondcell");
        }

        if(Game.Get().IsAtExtremity((Vector2)DataMap.get("firstcell"))) {
            CanExtend=true;

            DataMap.put("linetoextend",Game.Get().LineFromExtremity((Vector2)DataMap.get("firstcell")));

        } else {
            Game.Debug(1,"Selected station is not at an extremity of its line. Extension will abort, and a new line will be created.");
        }
    }

    private boolean CanExtend = false;

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
    public AbstractClickState NaturalTransition() {
        AbstractClickState Res = this;
        if(!CanExtend)
            Res=new LineCreationState(SM);
        return Res;
    }

    @Override
    public void Action() throws InteractionException {
        Vector2 V1 = (Vector2) SM.GetData().get("firstcell");
        Vector2 V2 = (Vector2) SM.GetData().get("secondcell");

        Integer CreationCost = (35)+(int)(Vector2.AbstractDistance(V1,V2)/7);
        if(Game.Get().GetMoney()<CreationCost) {
            throw new InteractionException("money");
        } else {
            Integer Res = Game.Get().ExtendLine(V1, V2, (Integer) SM.GetData().get("linetoextend"));
            if(Res!=null && Res>-1) {
                Game.Get().AddMoney(-1*CreationCost);
            } else {
                throw new InteractionException("unknown");
            }
        }
    }
}
