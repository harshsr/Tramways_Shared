package com.stonksco.minitramways.views.layers.cells;

import com.stonksco.minitramways.logic.Game;
import com.stonksco.minitramways.logic.Vector2;
import com.stonksco.minitramways.logic.map.buildings.BuildingEnum;
import com.stonksco.minitramways.views.GameView;
import com.stonksco.minitramways.views.items.ImageGetter;
import com.stonksco.minitramways.views.items.ImagesEnum;
import com.stonksco.minitramways.views.items.PinView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class BuildingView extends CellView {
    private ImageView Sprite;
    private int PV;

    private final BuildingEnum Type;

    public BuildingView(GameView gw, Vector2 gridPos, BuildingEnum type) {
        super(gw,gridPos);
        this.Type = type;
        PV=-1;

        if (this.Type == BuildingEnum.HOUSE) {
            Image img = new ImageGetter().GetImageOf(ImagesEnum.HOUSE);
            Sprite = new ImageView();

            Sprite.fitHeightProperty().bind(gw.GetCellSizeY().multiply(0.95d));
            Sprite.fitWidthProperty().bind(gw.GetCellSizeX().multiply(0.95d));
            Sprite.setImage(img);
            Game.Debug(4, "Created Building image HOUSE :" + Sprite.getBoundsInLocal());
            this.getChildren().add(Sprite);
        }
        else if(this.Type == BuildingEnum.SHOP) {
            Image img = new ImageGetter().GetImageOf(ImagesEnum.SHOP);
            Sprite = new ImageView();

            Sprite.fitHeightProperty().bind(gw.GetCellSizeY().multiply(0.95d));
            Sprite.fitWidthProperty().bind(gw.GetCellSizeX().multiply(0.95d));
            Sprite.setImage(img);
            Game.Debug(4, "Created Building image SHOP :" + Sprite.getBoundsInLocal());
            this.getChildren().add(Sprite);
        }
        else if(this.Type == BuildingEnum.OFFICE) {
            Image img = new ImageGetter().GetImageOf(ImagesEnum.OFFICE);
            Sprite = new ImageView();

            Sprite.fitHeightProperty().bind(gw.GetCellSizeY().multiply(0.95d));
            Sprite.fitWidthProperty().bind(gw.GetCellSizeX().multiply(0.95d));
            Sprite.setImage(img);
            Game.Debug(4, "Created Building image OFFICE :" + Sprite.getBoundsInLocal());
            this.getChildren().add(Sprite);
        }

        // Rotation variable 
        int rotation = ((int)gridPos.GetX() + (int)gridPos.GetY())%4;
        Sprite.setRotate(rotation*90);

    }

    public void SetAmount(int nb) {
        if(PV!=-1) {
            if(Gameview.GetPinNumber(PV)!=nb || !Gameview.DoesPinExists(PV)) {
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