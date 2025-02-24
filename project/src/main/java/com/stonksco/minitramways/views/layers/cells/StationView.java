package com.stonksco.minitramways.views.layers.cells;

import com.stonksco.minitramways.logic.Game;
import com.stonksco.minitramways.logic.Vector2;
import com.stonksco.minitramways.views.GameView;
import com.stonksco.minitramways.views.items.ImageGetter;
import com.stonksco.minitramways.views.items.ImagesEnum;
import com.stonksco.minitramways.views.items.PinView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;


public class StationView extends CellView {

    private final ImageView Sprite;
    private int PV =-1;


    public StationView(GameView gw, Vector2 gridPos) {
        super(gw,gridPos);

        Image img = new ImageGetter().GetImageOf(ImagesEnum.STATION);
        Sprite = new ImageView();

        Sprite.fitHeightProperty().bind(gw.GetCellSizeY().multiply(0.95d));
        Sprite.fitWidthProperty().bind(gw.GetCellSizeX().multiply(0.95d));
        Sprite.setImage(img);
        this.getChildren().add(Sprite);
        
    }

    public void Enable() {

        
        if(Game.Get().GetDebug()>2) {
            Text t = new Text(GridPos.toString());
            t.setFill(Color.RED);
            t.wrappingWidthProperty().bind(Gameview.GetCellSizeX());
            t.autosize();
            this.getChildren().add(t);
        }
        this.opacityProperty().setValue(1);


    }

    public void ShowRadius() {
        Gameview.GetRadiusLayer().ShowRadiusAt(GridPos);
    }

    public void HideRadius() {
        Gameview.GetRadiusLayer().HideRadiusAt(GridPos);
    }

   
    public void SetAmount(int nb) {
        if(PV!=-1) {
            if(Gameview.GetPinNumber(PV)!=nb) {
                Gameview.RemovePin(PV);
                PV=-1;
                CreatePin(nb);
            }
        }
        else {
            CreatePin(nb);
        }

    }

    private void CreatePin(int nb) {
        PV = Gameview.AddPin(this.GridPos,nb);
    }


}
