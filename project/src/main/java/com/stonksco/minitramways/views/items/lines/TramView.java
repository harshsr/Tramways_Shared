package com.stonksco.minitramways.views.items.lines;

import com.stonksco.minitramways.logic.Game;
import com.stonksco.minitramways.views.GameView;
import com.stonksco.minitramways.views.items.ImageGetter;
import com.stonksco.minitramways.views.items.ImagesEnum;
import com.stonksco.minitramways.views.items.PinView;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


public class TramView extends Group {

    private final LineView LineView;
    private LinePartView LinePartView;
    private final GameView GameView;


    private final ImageView Sprite;
    private PinView PinView;

    public TramView(LineView lv,double at, GameView gw, int colorID, int peopleAmount) {
        super();
        this.LineView = lv;
        this.GameView = gw;
        this.LinePartView = lv.GetPartAt(at);


        Image img = null;

        try{
            switch (colorID){
                case 0:
                    img = new ImageGetter().GetImageOf(ImagesEnum.TRAMWAY_GOLD);
                    break;
                case 1:
                    img = new ImageGetter().GetImageOf(ImagesEnum.TRAMWAY_BLUE);
                    break;
                case 2:
                    img = new ImageGetter().GetImageOf(ImagesEnum.TRAMWAY_RED);
                    break;
                case 3:
                    img = new ImageGetter().GetImageOf(ImagesEnum.TRAMWAY_LIME);
                    break;
                case 4:
                    img = new ImageGetter().GetImageOf(ImagesEnum.TRAMWAY_PURPLE);
                    break;
                case 5:
                    img = new ImageGetter().GetImageOf(ImagesEnum.TRAMWAY_CYAN);
                    break;
                case 6:
                    img = new ImageGetter().GetImageOf(ImagesEnum.TRAMWAY_YELLOW);
                    break;
                case 7:
                    img = new ImageGetter().GetImageOf(ImagesEnum.TRAMWAY_ROSEGOLD);
                    break;
            }
        } catch(Exception e) {
            Game.Debug(1,"ERROR loading an asset : "+e.getMessage());
        }

    
        Sprite  = new ImageView();
        Sprite.setPreserveRatio(true);
      
        Sprite.fitHeightProperty().bind(gw.GetCellSizeY().divide(2));
        double ratio = img.getWidth()/img.getHeight();
        Sprite.fitWidthProperty().bind(Sprite.fitHeightProperty().multiply(ratio));
  

        Sprite.setImage(img);
        Sprite.smoothProperty().set(true);


        Sprite.translateXProperty().bind(Sprite.fitWidthProperty().divide(-2d));
        Sprite.translateYProperty().bind(Sprite.fitHeightProperty().divide(-2d));

        this.getChildren().add(Sprite);



        if(peopleAmount>0) {
            PinView = new PinView(gw,peopleAmount);
            this.getChildren().add(PinView);
            PinView.translateXProperty().bind(Sprite.fitWidthProperty().multiply(-0.5d));
            PinView.translateYProperty().bind(Sprite.fitHeightProperty().multiply(-1d));
        }

        PositionAt(at);

    }

    private void PositionAt(double at) {
        this.layoutXProperty().bind(LineView.GetPosXAt(at));
        this.layoutYProperty().bind(LineView.GetPoxYAt(at));
        LinePartView = LineView.GetPartAt(at);
        this.Sprite.setRotate(LinePartView.GetOrientation());
    }



}
