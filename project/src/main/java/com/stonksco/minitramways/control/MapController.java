package com.stonksco.minitramways.control;

import com.stonksco.minitramways.control.utils.Listened;
import com.stonksco.minitramways.control.utils.Notification;
import com.stonksco.minitramways.logic.Game;
import com.stonksco.minitramways.logic.Vector2;
import com.stonksco.minitramways.logic.interactions.InteractionException;
import com.stonksco.minitramways.logic.interactions.states.AbstractClickState;
import com.stonksco.minitramways.logic.interactions.states.LineCreationState;
import com.stonksco.minitramways.logic.interactions.states.LineExtensionState;

import java.util.ArrayList;

public class MapController extends Listened {

    public void SendLeftClick(Vector2 at) {
        AbstractClickState OldState = Game.Get().GetCurrentClickState();
        AbstractClickState NewState = null;
        try {
            NewState = Game.Get().SendLeftClick(at);
        } catch (InteractionException e) {
            Notification ntf = new Notification("interactionerror");
            ntf.SetData(e.getMessage());
            Notify(ntf);
        }

        if(NewState instanceof LineExtensionState) {
            Notification Notif = new Notification("updatelines");
            Notif.SetData(Game.Get().GetMap().GetLinesToUpdate());
            Notify(Notif);
        }

        Notify(new Notification("updateinteractions"));
    }

    public void SendRightClick(Vector2 at) {
        AbstractClickState OldState = Game.Get().GetCurrentClickState();
        AbstractClickState NewState = null;
        try {
        NewState = Game.Get().SendRightClick(at);
        } catch (InteractionException e) {
            Notification ntf = new Notification("interactionerror");
            ntf.SetData(e.getMessage());
            Notify(ntf);
        }

        ArrayList<Integer> ToUpdate = Game.Get().GetMap().GetLinesToUpdate();
        if(ToUpdate!=null && ToUpdate.size()>0) {
            Notification Notif = new Notification("updatelines");
            Notif.SetData(ToUpdate);
            Notify(Notif);
        }

        Notify(new Notification("updateinteractions"));
    }
}
