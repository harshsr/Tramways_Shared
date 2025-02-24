package com.stonksco.minitramways.views.layers;

import com.stonksco.minitramways.logic.Game;
import com.stonksco.minitramways.logic.Vector2;
import com.stonksco.minitramways.views.GameView;
import com.stonksco.minitramways.views.items.ItemsEnum;
import com.stonksco.minitramways.views.layers.cells.CellView;
import javafx.geometry.Pos;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

public abstract class GridView extends GridPane {

    protected GameView gw;

    protected final Vector2 gridSize;
    protected HashMap<Vector2, CellView> cells;

    public GridView(GameView gw, Vector2 size) {
        super();
        gridSize = size;
        this.gw = gw;
    }

  
    protected void Fill(Class<?> type) {
        if(CellView.class.isAssignableFrom(type)) {
            Game.Debug(3,"Starting grid fill with "+type);
            cells = new HashMap<>();
            for(int i=0; i<gridSize.GetY(); i++) {
                for(int j=0; j<gridSize.GetX(); j++) {

                    try {
                        Vector2 v = new Vector2(j,i);
                        
                        CellView cell = (CellView) type.getDeclaredConstructor(GameView.class,Vector2.class).newInstance(gw,v);

                      
                        cell.setAlignment(Pos.CENTER);

                        cells.put(v,cell);
                        this.add(cell,j,i);

                    } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
                        Game.Debug(1,"ERROR when filling a grid : "+e.getMessage()+"\n"+e.getCause());
                    }
                }
            }
            Game.Debug(3,"Grid successfully filled with "+cells.size()+" cells.");
            SetConstraints();
        } else {
            Game.Debug(2,"ERROR when trying to fill grid : "+type+" is not a CellView.");
        }
    }

    private void SetConstraints() {
        ColumnConstraints cc = new ColumnConstraints();
        cc.setPercentWidth(100d/this.getColumnCount());

        RowConstraints rc = new RowConstraints();
        rc.setPercentHeight(100d/this.getRowCount());

        for(int i = 0; i<this.getColumnCount(); i++)
            this.getColumnConstraints().add(cc);
        for(int i = 0; i<this.getRowCount(); i++)
            this.getRowConstraints().add(rc);
    }

  
    public CellView GetCellAt(Vector2 pos) {
        return cells.get(pos);
    }

    
}
