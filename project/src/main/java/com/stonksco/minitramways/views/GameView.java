package com.stonksco.minitramways.views;

import com.stonksco.minitramways.control.MapController;
import com.stonksco.minitramways.control.utils.Listener;
import com.stonksco.minitramways.control.utils.Notification;
import com.stonksco.minitramways.logic.Game;
import com.stonksco.minitramways.logic.Vector2;
import com.stonksco.minitramways.logic.map.buildings.BuildingEnum;
import com.stonksco.minitramways.views.layers.*;
import com.stonksco.minitramways.views.layers.cells.CellView;
import com.stonksco.minitramways.views.layers.cells.GridDisplayCell;
import com.stonksco.minitramways.views.layers.cells.StationView;
import com.stonksco.minitramways.views.ui.infosLayer;
import com.stonksco.minitramways.views.ui.InteractionsViewLayer;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.When;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Map;

public class GameView extends Scene implements Listener {

    private static final ArrayList<GameView> instances = new ArrayList<>();
    private final Group root;
    private final Stage primaryStage;
  
    private final MapController mapController;

 
    private final Map<ColorEnum,Color> colors = Map.ofEntries(
            Map.entry(ColorEnum.LINE_BLUE,Color.web("0x3333FF",1)),
            Map.entry(ColorEnum.LINE_CYAN,Color.web("0x0099CC",1)),
            Map.entry(ColorEnum.LINE_GOLD,Color.web("0xCCCC33",1)),
            Map.entry(ColorEnum.LINE_LIME,Color.web("0x2FD61D",1)),
            Map.entry(ColorEnum.LINE_ROSEGOLD,Color.web("0xE0BFB8",1)),
            Map.entry(ColorEnum.LINE_PURPLE,Color.web("0xCC00CC",1)),
            Map.entry(ColorEnum.LINE_RED,Color.web("0xCC0000",1)),
            Map.entry(ColorEnum.LINE_YELLOW,Color.web("0xFFFF33",1)),
            Map.entry(ColorEnum.BACKGROUND,Color.web("0xE9E9E9",1)),
            Map.entry(ColorEnum.GRID_DOT,Color.web("0x000000",0.07)),
            Map.entry(ColorEnum.RESIDENTIAL_BACKGROUND,Color.web("0xD1DFBC",1)),
            Map.entry(ColorEnum.RESIDENTIAL_BORDER,Color.web("0xE9FFD6",1)),
            Map.entry(ColorEnum.COMMERCIAL_BACKGROUND,Color.web("0xEE6F66",1)),
            Map.entry(ColorEnum.COMMERCIAL_BORDER,Color.web("0xFF8A6D",1)),
            Map.entry(ColorEnum.OFFICE_BACKGROUND,Color.web("0x53B0D1",1)),
            Map.entry(ColorEnum.OFFICE_BORDER,Color.web("0x65D6FF",1)),
            Map.entry(ColorEnum.PIN_COLOR,Color.web("0xED362E",1)),
            Map.entry(ColorEnum.TARGET_COLOR,Color.web("0xE0BFB8",0.2d)),
            Map.entry(ColorEnum.TARGET_OUTLINE_COLOR,Color.web("0xE0BFB8",0.6d)),
            Map.entry(ColorEnum.CELL_SELECTION,Color.web("0x74df64",0.7d))
    );

    DoubleProperty cellSizeX;
    DoubleProperty cellSizeY;
 
    private GridDisplay gridDisplay; 
    private StationsLayer gridStations; 
    private BuildingsLayer gridBuildings;
    private CellInteractionsLayer cellInteractionsLayer; 
    private AreasLayer areasPane; 
    private LinesLayer linesPane; 
    private RadiusLayer radiusLayer; 
    private TargetsLayer targetsLayer;
    private PinsLayer pinsLayer; 
    private infosLayer infosLayer; 
    private InteractionsViewLayer interactionsViewLayer; 

    private StackPane mainPane; 
    private Pane centerPane; 


  
    public GameView(Group parent, Stage primaryStage, MapController controller) {
        super(parent, 1600,900);
        instances.add(this);

        this.root = parent;
        this.primaryStage = primaryStage;

        mapController = controller;
        controller.Register(this);

        Game.Get().InitGame();

        this.setFill(GetColor(ColorEnum.BACKGROUND));
        String stylesheetpath = getClass().getResource("/style.css").toExternalForm();
        this.getStylesheets().clear();
        this.getStylesheets().add(stylesheetpath);
    }

    public static void FrameUpdate() {
        for(GameView gw : instances) {
            gw.Update();
        }
    }

    public void Enable() {
        InitWindowLayout();
        InitMapLayers();
        gridBuildings.UpdateBuildings();
        Clock.Get().start();
    }

    private void InitWindowLayout() {

        centerPane = new Pane();
        mainPane = new StackPane();
        infosLayer = new infosLayer(this);
        interactionsViewLayer = new InteractionsViewLayer(this);

        this.getWindow().setWidth(this.getWindow().getWidth()+0.001);
        this.getWindow().setWidth(this.getWindow().getWidth()-0.001);

        mainPane.prefWidthProperty().bind(this.widthProperty());
        mainPane.prefHeightProperty().bind(this.heightProperty());

        root.layout();
        mainPane.layout();

        if(Game.Get().GetDebug()>2) {
            BorderStroke[] strokes = {
                    new BorderStroke(Color.RED,BorderStrokeStyle.SOLID,new CornerRadii(5),new BorderWidths(2d)),
                    new BorderStroke(Color.RED,BorderStrokeStyle.SOLID,new CornerRadii(5),new BorderWidths(2d)),
                    new BorderStroke(Color.RED,BorderStrokeStyle.SOLID,new CornerRadii(5),new BorderWidths(2d)),
                    new BorderStroke(Color.RED,BorderStrokeStyle.SOLID,new CornerRadii(5),new BorderWidths(2d))
            };
            centerPane.setBorder(new Border(strokes));
            mainPane.setBorder(new Border(strokes));
        }


        mainPane.paddingProperty().bind(Bindings.createObjectBinding(() -> new Insets(mainPane.heightProperty().multiply(0.05d).get())));
        mainPane.getChildren().add(centerPane);
        mainPane.getChildren().add(interactionsViewLayer);
        mainPane.getChildren().add(infosLayer);
        root.getChildren().add(mainPane);

        When bindCondition = new When(mainPane.widthProperty().lessThan(mainPane.heightProperty().multiply(Game.Get().GetMapSize().GetX()/Game.Get().GetMapSize().GetY())));
        SimpleDoubleProperty widthBinding = new SimpleDoubleProperty();
        SimpleDoubleProperty heightBinding = new SimpleDoubleProperty();
        widthBinding.bind(bindCondition.then(mainPane.widthProperty().multiply(0.85d)).otherwise(  heightBinding.divide(Game.Get().GetMapSize().GetY()).multiply(Game.Get().GetMapSize().GetX())  ));
        heightBinding.bind(bindCondition.then(  widthBinding.divide(Game.Get().GetMapSize().GetX()).multiply(Game.Get().GetMapSize().GetY())  ).otherwise(mainPane.heightProperty().multiply(0.85d)));

        centerPane.prefWidthProperty().bind(widthBinding);
        centerPane.prefHeightProperty().bind(heightBinding);
        centerPane.maxWidthProperty().bind(widthBinding);
        centerPane.maxHeightProperty().bind(heightBinding);


        root.layout();
        mainPane.layout();
        centerPane.layout();
        mainPane.layout();
        root.layout();

        Game.Debug(3,"Main container got width of "+mainPane.widthProperty().get()+" (wanted: "+mainPane.prefWidthProperty().get()+" ) pixels and height of "+mainPane.heightProperty().get()+" pixels (wanted: "+mainPane.prefHeightProperty().get()+" )");
        Game.Debug(3,"Center container got width of "+centerPane.widthProperty().get()+" (wanted: "+centerPane.prefWidthProperty().get()+" ) pixels and height of "+centerPane.heightProperty().get()+" pixels (wanted: "+centerPane.prefHeightProperty().get()+" )");

    }


    private void InitMapLayers() {
        Vector2 s = Game.Get().GetMapSize();

        gridDisplay = new GridDisplay(this,s);
        gridStations = new StationsLayer(this,s);
        gridBuildings = new BuildingsLayer(this,s);
        cellInteractionsLayer = new CellInteractionsLayer(this,s);
        areasPane = new AreasLayer(this);
        linesPane = new LinesLayer(this);
        radiusLayer = new RadiusLayer(this);
        targetsLayer = new TargetsLayer(this);
        pinsLayer = new PinsLayer(this);

        centerPane.getChildren().add(areasPane);
        centerPane.getChildren().add(gridDisplay);
        centerPane.getChildren().add(gridBuildings);
        centerPane.getChildren().add(linesPane);
        centerPane.getChildren().add(radiusLayer);
        centerPane.getChildren().add(gridStations);
        centerPane.getChildren().add(pinsLayer);
        centerPane.getChildren().add(targetsLayer);
        mainPane.getChildren().add(cellInteractionsLayer);

        centerPane.layout();

        this.getWindow().setWidth(this.getWindow().getWidth()+0.001);
        this.getWindow().setWidth(this.getWindow().getWidth()-0.001);

        gridDisplay.prefWidthProperty().bind(centerPane.widthProperty());
        gridDisplay.prefHeightProperty().bind(centerPane.heightProperty());

        ArrayList<Pane> layersList = new ArrayList<>();
    
        layersList.add(gridStations);
        layersList.add(gridBuildings);
        layersList.add(cellInteractionsLayer);
        layersList.add(areasPane);
        layersList.add(linesPane);
        layersList.add(radiusLayer);
        layersList.add(targetsLayer);
        layersList.add(pinsLayer);

        for (Pane layer:layersList) {
            layer.prefWidthProperty().bind(gridDisplay.widthProperty());
            layer.prefHeightProperty().bind(gridDisplay.heightProperty());
            layer.maxWidthProperty().bind(gridDisplay.widthProperty());
            layer.maxHeightProperty().bind(gridDisplay.heightProperty());
            layer.minWidthProperty().bind(gridDisplay.widthProperty());
            layer.minHeightProperty().bind(gridDisplay.heightProperty());
        }

        centerPane.layout();

        Game.Debug(1,"Map layers initialized with a size of "+centerPane.widthProperty().get()+" * "+centerPane.heightProperty().get()+" pixels");


    }


    public void CellLeftClick(CellView cell)
    {
        mapController.SendLeftClick(new Vector2(GridPane.getColumnIndex(cell),GridPane.getRowIndex(cell)));
    }

 
    public void CellRightClick(CellView cell) {
        mapController.SendRightClick(new Vector2(GridPane.getColumnIndex(cell),GridPane.getRowIndex(cell)));
    }


    public void CellEnter(Vector2 cell) {
        this.gridStations.ShowRadiusOf(cell);
        targetsLayer.Enter(cell);
    }

    public void CellExit(Vector2 cell) {
        this.gridStations.HideRadiusOf(cell);
        targetsLayer.Exit(cell);
    }


    @Override
    public void Notify(Notification notif) {
        String msg = notif.GetMessage();
        switch(msg) {
            case "updatelines" :
                UpdateLines((ArrayList<Integer>)notif.GetData());
                break;
            case "updateinteractions":
                interactionsViewLayer.ChangeState(Game.Get().GetCurrentClickState());
                break;
            case "interactionerror":
                this.SetErrorMessage((String) notif.GetData());
                break;
        }
    }



    public ReadOnlyDoubleProperty CellToPixelsX(Vector2 cellPos) {
        return ((GridDisplayCell)gridDisplay.GetCellAt(cellPos)).GetPixelsX();
    }

    public ReadOnlyDoubleProperty CellToPixelsY(Vector2 cellPos) {
        return ((GridDisplayCell)gridDisplay.GetCellAt(cellPos)).GetPixelsY();
    }


 
    public void AddStationAt(Vector2 at) {
        gridStations.AddStationAt(at);
    }

  
    public ReadOnlyDoubleProperty GetCellSizeX() {
        DoubleProperty res;
        if(cellSizeX==null) {
            cellSizeX = new SimpleDoubleProperty(0);
            cellSizeX.bind(centerPane.widthProperty().divide(Game.Get().GetMapSize().GetX()));
        }
        res = cellSizeX;
        return res;
    }


    public ReadOnlyDoubleProperty GetCellSizeY() {
        DoubleProperty res;
        if(cellSizeY==null) {
            cellSizeY = new SimpleDoubleProperty(0);
            cellSizeY.bind(centerPane.heightProperty().divide(Game.Get().GetMapSize().GetY()));
        }
        res = cellSizeY;
        return res;
    }



    
    public Color GetColor(ColorEnum c) {
        return this.colors.get(c);
    }


   
    private void AddBuildingAt(Vector2 at,BuildingEnum types) {
        gridBuildings.AddBuildingAt(at,types);
    }
    
    public ReadOnlyDoubleProperty GridPosX() {
        SimpleDoubleProperty p = new SimpleDoubleProperty();
        //p.bind(this.gridDisplay.layoutXProperty());
        p.bind(centerPane.layoutXProperty());
        return p;
    }

    public ReadOnlyDoubleProperty GridPosY() {
        SimpleDoubleProperty p = new SimpleDoubleProperty();
        //p.bind(this.gridDisplay.layoutYProperty());
        p.bind(centerPane.layoutYProperty());
        return p;
    }

    public void UpdateLines(ArrayList<Integer> data) {
        for(int l : data) {
            Game.Debug(2,"VIEW : Refreshing line "+l);
            linesPane.RemoveLine(l);
            linesPane.AddLine(l);
        }
        UpdateStations();
    }

    public void UpdateStations() {
        ArrayList<Vector2> stations = gridStations.UpdateStations();
        radiusLayer.UpdateStationsSet(stations);
    }

    public void Update() {
        if(Game.Get().NeedPinsUpdate())
            pinsLayer.Reset();

        linesPane.Update();
        gridBuildings.UpdateBuildings();
        gridBuildings.UpdateBuildingsPins();
        gridStations.UpdateStationsPins();
        infosLayer.Update();
    }

    public RadiusLayer GetRadiusLayer() {
        return radiusLayer;
    }

    public TargetsLayer GetTargetsLayer() { return targetsLayer;}

    /**  ------ Pins ------ */

    public int AddPin(Vector2 at, int nb) {
        return pinsLayer.AddPin(at,nb);
    }

    public void RemovePin(int id) {
        pinsLayer.RemovePin(id);
    }

    public void ResetPins() {
        pinsLayer.Reset();
    }

    public int GetPinNumber(int id) {
        return pinsLayer.GetNbOf(id);
    }

    public boolean DoesPinExists(int id) {
        return pinsLayer.DoesPinExists(id);
    }

    public void UpdatePins() {
        gridStations.UpdateStationsPins();
        gridBuildings.UpdateBuildingsPins();
    }

 

    private void SetErrorMessage(String messagecode) {
        this.infosLayer.SetErrorMessage(messagecode);
    }

}













