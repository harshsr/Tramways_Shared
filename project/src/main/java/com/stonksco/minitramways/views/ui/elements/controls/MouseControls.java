package com.stonksco.minitramways.views.ui.elements.controls;

import com.stonksco.minitramways.logic.interactions.states.AbstractClickState;
import com.stonksco.minitramways.logic.interactions.states.LineCreationState;
import com.stonksco.minitramways.logic.interactions.states.LineExtensionState;
import com.stonksco.minitramways.logic.interactions.states.StartState;
import com.stonksco.minitramways.views.GameView;
import com.stonksco.minitramways.views.items.ImageGetter;
import com.stonksco.minitramways.views.items.ImagesEnum;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.Map;

public class MouseControls extends Pane {

    private GameView GameView;

    private VBox ctrlVContainer;
        private HBox ctrlHContainer;
            private StackPane ctrlStack;
                private HBox leftRightContainer;
                    private StackPane leftStack;
                        private Rectangle leftBg;
                        private Text leftLabel;
                    private StackPane rightStack;
                        private Rectangle rightBg;
                        private Text rightLabel;
                private ImageView mouseImgView;
                    private Image mouseImg;

    public MouseControls(GameView gw) {
        this.GameView = gw;

        // Intiialisation
        ctrlVContainer=new VBox();
        ctrlHContainer = new HBox();
        ctrlStack= new StackPane();
        leftRightContainer=new HBox();
        leftStack = new StackPane();
        leftBg = new Rectangle();
        leftLabel = new Text("");
        rightStack = new StackPane();
        rightBg = new Rectangle();
        rightLabel = new Text("");
        mouseImgView = new ImageView();

        
        this.getChildren().add(ctrlVContainer);
        ctrlVContainer.getChildren().add(ctrlHContainer);
        ctrlHContainer.getChildren().add(ctrlStack);
        ctrlStack.getChildren().add(leftRightContainer);
        leftRightContainer.getChildren().add(leftStack);
        leftRightContainer.getChildren().add(rightStack);
        leftStack.getChildren().add(leftBg);
        leftStack.getChildren().add(leftLabel);
        rightStack.getChildren().add(rightBg);
        rightStack.getChildren().add(rightLabel);
        ctrlStack.getChildren().add(mouseImgView);

        // Image
        ImageGetter imggtr = new ImageGetter();
        mouseImg = imggtr.GetImageOf(ImagesEnum.MOUSE);
        mouseImgView.setImage(mouseImg);

        // Texte
        Font f = new Font(30);
        leftLabel.setFont(f);
        rightLabel.setFont(f);
        //leftLabel.setFill(Color.WHITE);
        //rightLabel.setFill(Color.WHITE);

        // Layout
        this.layout();
        this.prefWidthProperty().bind(gw.widthProperty());
        this.prefHeightProperty().bind(gw.heightProperty());
        ctrlVContainer.prefWidthProperty().bind(gw.widthProperty());
        ctrlVContainer.prefHeightProperty().bind(gw.heightProperty());
        ctrlVContainer.setAlignment(Pos.TOP_LEFT);
        ctrlHContainer.setAlignment(Pos.TOP_LEFT);
        ctrlStack.prefHeightProperty().bind(gw.heightProperty().multiply(0.1d));
        ctrlStack.minHeight(50);
        mouseImgView.fitHeightProperty().bind(ctrlStack.heightProperty().multiply(1.2d));
        mouseImgView.translateYProperty().bind(mouseImg.heightProperty().multiply(0.2));
        this.layout();
        Hide();
    }

    private final Map<String, String> messages = Map.ofEntries(
            Map.entry("destroy","Détruire une station (station)"),
            Map.entry("createline","Construire une nouvelle ligne (case vide ou station)"),
            Map.entry("extendline","Étendre la ligne (case vide)"),
            Map.entry("cancel","Annuler")
    );

    private final Map<String, Color> bgcolors = Map.ofEntries(
            Map.entry("create",Color.web("0x2FD61D")),
            Map.entry("extend",Color.web("0xCCCC33")),
            Map.entry("destroy",Color.web("0xCC0000")),
            Map.entry("cancel",Color.web("0x3e4754"))
    );

    public void setState(AbstractClickState state) {
        if(state instanceof StartState) {
            leftLabel.setText(messages.get("createline"));
            leftBg.setFill(bgcolors.get("create"));
            rightLabel.setText(messages.get("destroy"));
            rightBg.setFill(bgcolors.get("destroy"));
            Show();
        } else if(state instanceof LineCreationState) {

        } else if (state instanceof LineExtensionState) {

        }
    }


    private void Hide() {
        ctrlVContainer.setOpacity(0);
    }

    private void Show() {
        ctrlVContainer.setOpacity(1);
    }



}
