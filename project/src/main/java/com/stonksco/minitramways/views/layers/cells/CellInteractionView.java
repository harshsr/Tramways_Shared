package com.stonksco.minitramways.views.layers.cells;

import com.stonksco.minitramways.logic.Game;
import com.stonksco.minitramways.logic.Vector2;
import com.stonksco.minitramways.views.GameView;
import javafx.event.EventHandler;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

public class CellInteractionView extends CellView {

  
    EventHandler<MouseEvent> CellClickEvent = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent mouseEvent) {
                CellView clicked = (CellView)mouseEvent.getSource();
                Game.Debug(2,"Clicked on cell at ( "+clicked.GetGridPos().GetX()+" , "+clicked.GetGridPos().GetY()+" )");
                if (mouseEvent.getButton() == MouseButton.PRIMARY) {
                    Gameview.CellLeftClick(clicked);
                } else if(mouseEvent.getButton() == MouseButton.SECONDARY) {
                    Gameview.CellRightClick(clicked);
                }
            }
        };

    
    EventHandler<MouseEvent> CellEnterEvent = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent mouseEvent) {
            Vector2 pos = new Vector2(GridPane.getColumnIndex((CellView)mouseEvent.getSource()),GridPane.getRowIndex((CellView)mouseEvent.getSource()));
            Gameview.CellEnter(pos);
        }
    };


    EventHandler<MouseEvent> CellExitEvent = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent mouseEvent) {
            Gameview.CellExit(new Vector2(GridPane.getColumnIndex((CellView)mouseEvent.getSource()),GridPane.getRowIndex((CellView)mouseEvent.getSource())));
        }
    };


    public CellInteractionView(GameView gw, Vector2 gridPos) {
        super(gw,gridPos);
        this.addEventFilter(MouseEvent.MOUSE_CLICKED,CellClickEvent);
        this.addEventFilter(MouseEvent.MOUSE_ENTERED,CellEnterEvent);
        this.addEventFilter(MouseEvent.MOUSE_EXITED,CellExitEvent);
    }

}
