package com.stonksco.minitramways.control.utils;

import java.util.ArrayList;

public abstract class Listened {
    private final ArrayList<Listener> Listeners = new ArrayList<>();

    public void Register(Listener listener) {
        this.Listeners.add(listener);
    }

    public void Notify(Notification msg) {
        for(Listener o : Listeners) {
            o.Notify(msg);
        }
    }

}
