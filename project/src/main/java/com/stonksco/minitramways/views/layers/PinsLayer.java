package com.stonksco.minitramways.views.layers;

import com.stonksco.minitramways.logic.Vector2;
import com.stonksco.minitramways.views.GameView;
import com.stonksco.minitramways.views.items.PinView;
import javafx.scene.layout.Pane;

import java.util.HashMap;

public class PinsLayer extends Pane {

    private GameView GameView;

    private HashMap<Integer, PinView> Pins;

    public PinsLayer(GameView gw) {
        super();
        this.GameView = gw;
        Pins = new HashMap<>();
    }

   
    public int AddPin(Vector2 at,int nb) {
        PinView pv = new PinView(GameView,nb);
        int id = Pins.size();
        Pins.put(id,pv);
        this.getChildren().add(pv);

        pv.translateXProperty().bind(GameView.GetCellSizeX().multiply(at.GetX()));
        pv.translateYProperty().bind(GameView.GetCellSizeY().multiply(at.GetY()));

        pv.setViewOrder(-100*at.GetY()+(10*at.GetX()));

        return id;
    }

    public void RemovePin(int id) {
        PinView pv = Pins.get(id);
        if(pv!=null) {
            this.getChildren().remove(pv);
            this.Pins.remove(id);
        }
    }

    public void Reset() {
        this.getChildren().clear();
        this.Pins.clear();
        GameView.UpdatePins();
    }

    public int GetNbOf(int id) {
        int res = -1;
        PinView pv = this.Pins.get(id);
        if(pv!=null)
            res = pv.GetNb();
        return res;
    }

    public boolean DoesPinExists(int pinID) {
        return this.Pins.get(pinID)!=null;
    }


}
