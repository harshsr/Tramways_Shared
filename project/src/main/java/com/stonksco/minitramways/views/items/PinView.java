package com.stonksco.minitramways.views.items;

import com.stonksco.minitramways.views.ColorEnum;
import com.stonksco.minitramways.views.GameView;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectExpression;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.text.*;

public class PinView extends StackPane {

    private ImageView PinImage;
    private final int NB;


    public PinView(GameView gw, int nb) {
        super();
        this.NB = nb;

        if(nb > 0){
            Image img = new ImageGetter().GetImageOf(ImagesEnum.PIN);
            PinImage = new ImageView();
            Text text = new Text(String.valueOf(nb));


            ObjectExpression<Font> dynFont = Bindings.createObjectBinding(() -> Font.font("Helvetica", FontWeight.SEMI_BOLD, FontPosture.REGULAR, getWidth()*0.55d), widthProperty());
            text.fontProperty().bind(dynFont);
            text.setFill(gw.GetColor(ColorEnum.PIN_COLOR));

            PinImage.fitHeightProperty().bind(gw.GetCellSizeY().multiply(0.85d+(nb/15d)));
            PinImage.setPreserveRatio(true);
            PinImage.translateYProperty().bind(gw.GetCellSizeY().multiply(-0.5d));
            PinImage.setImage(img);


            this.getChildren().add(PinImage);

            text.setTextAlignment(TextAlignment.CENTER);
            text.wrappingWidthProperty().bind(gw.GetCellSizeY().multiply(1d));
            text.translateYProperty().bind(PinImage.fitHeightProperty().multiply(-0.15).add(gw.GetCellSizeY().multiply(-0.5)));
            text.autosize();

            this.getChildren().add(text);

        }
    }

    public int GetNb() {
        return NB;
    }

}

