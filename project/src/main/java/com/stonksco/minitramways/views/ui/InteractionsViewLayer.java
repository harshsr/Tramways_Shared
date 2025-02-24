package com.stonksco.minitramways.views.ui;

import com.stonksco.minitramways.logic.Game;
import com.stonksco.minitramways.logic.Vector2;
import com.stonksco.minitramways.logic.interactions.states.AbstractClickState;
import com.stonksco.minitramways.logic.interactions.states.LineCreationState;
import com.stonksco.minitramways.logic.interactions.states.LineExtensionState;
import com.stonksco.minitramways.logic.interactions.states.StartState;
import com.stonksco.minitramways.views.GameView;
import com.stonksco.minitramways.views.ui.elements.controls.MouseControls;
import com.stonksco.minitramways.views.ui.elements.selections.CellSelection;
import javafx.scene.layout.Pane;

import java.util.ArrayList;

public class InteractionsViewLayer extends Pane {

    private GameView GameView;
    private ArrayList<CellSelection> Selected = new ArrayList<>();
    private MouseControls Mousectrls;
    private AbstractClickState State;

    public InteractionsViewLayer(GameView gw) {
        this.GameView = gw;
        Mousectrls = new MouseControls(gw);
        this.getChildren().add(Mousectrls);

        ChangeState(Game.Get().GetCurrentClickState());
    }

    public void ChangeState(AbstractClickState state) {
        this.State = state;
        Mousectrls.setState(state);
        Update();
    }

    private void Update() {
        this.getChildren().clear();
        Selected.clear();
        if(State instanceof StartState) {

        } else if(State instanceof LineCreationState) {
            Selected.add(new CellSelection(GameView, (Vector2) State.getData().get("firstcell")));

        } else if(State instanceof LineExtensionState) {
            Selected.add(new CellSelection(GameView, (Vector2) State.getData().get("firstcell")));

        }

        for(CellSelection cs : Selected) {
            this.getChildren().add(cs);
        }
    }









}
