package com.stonksco.minitramways.views.items;

import com.stonksco.minitramways.logic.Game;
import javafx.scene.image.Image;

import java.net.URL;

/**
 * Class to get images from the assets folder
 */
public class ImageGetter {

    public Image GetImageOf(ImagesEnum img) {
        Image image = null;
        URL imgPath;
        try {
            switch (img) {
                case STATION:
                    imgPath = getClass().getResource("/assets/station.png");
                    Game.Debug(4,"Found URL for STATION image : "+imgPath);
                    image = new Image(imgPath.toString());
                    break;
                case TRAMWAY_GOLD:
                    imgPath = getClass().getResource("/assets/tram_gold.png");
                    Game.Debug(4,"Found URL for TRAMWAY_GOLD image : "+imgPath);
                    image = new Image(imgPath.toString());
                    break;
                case TRAMWAY_BLUE:
                    imgPath = getClass().getResource("/assets/tram_blue@4x.png");
                    Game.Debug(4,"found url for TRAMWAY_BLUE image : " +imgPath);
                    image = new Image(imgPath.toString());
                    break;
                case TRAMWAY_RED:
                    imgPath = getClass().getResource("/assets/tram_red@4x.png");
                    Game.Debug(4,"foud URL for TRAMWAY_RED image : " +imgPath);
                    image = new Image(imgPath.toString());
                    break;
                case TRAMWAY_LIME:
                    imgPath = getClass().getResource("/assets/tram_lime@4x.png");
                    Game.Debug(4,"found URL for TRAMWAY_LIME image : " +imgPath);
                    image = new Image(imgPath.toString());
                    break;
                case TRAMWAY_PURPLE:
                    imgPath = getClass().getResource("/assets/tram_purple@4x.png");
                    Game.Debug(4,"found URL for TRAMWAY_PURPLE image : " +imgPath);
                    image= new Image(imgPath.toString());
                    break;
                case TRAMWAY_CYAN:
                    imgPath = getClass().getResource("/assets/tram_cyan@4x.png");
                    Game.Debug(4,"found URL for TRAMWAY_CYAN image : " +imgPath);
                    image = new Image(imgPath.toString());
                    break;
                case TRAMWAY_YELLOW:
                    imgPath = getClass().getResource("/assets/tram_yellow@4x.png");
                    Game.Debug(4,"found URl for TRAMWAY_YELLOW image : " +imgPath);
                    image = new Image(imgPath.toString());
                    break;
                case TRAMWAY_ROSEGOLD:
                    imgPath = getClass().getResource("/assets/tram_rosegold@4x.png");
                    Game.Debug(4,"found URL for TRAMWAY_ROSEGOLD image : " +imgPath);
                    image = new Image(imgPath.toString());
                    break;
                case HOUSE:
                    imgPath = getClass().getResource("/assets/house.png");
                    Game.Debug(4,"found URL for House image : " +imgPath);
                    image = new Image(imgPath.toString());
                    break;
                case SHOP:
                    imgPath = getClass().getResource("/assets/shop.png");
                    Game.Debug(4,"found URL for SHOP image : " +imgPath);
                    image = new Image(imgPath.toString());
                    break;
                case OFFICE:
                    imgPath = getClass().getResource("/assets/office.png");
                    Game.Debug(4,"found URL for OFFICE image : " +imgPath);
                    image = new Image(imgPath.toString());
                    break;
                case PIN:
                    imgPath = getClass().getResource("/assets/pin.png");
                    Game.Debug(4,"found URL for PIN image : " +imgPath);
                    image = new Image(imgPath.toString());
                    break;
                case MOUSE:
                    imgPath = getClass().getResource("/assets/mouse.png");
                    Game.Debug(4,"found URL for MOUSE image : " +imgPath);
                    image = new Image(imgPath.toString());
                    break;
            }
        } catch(Exception e) {
            Game.Debug(1,"ERROR loading an asset : "+e.getMessage());
        }
        return image;
    }

}

