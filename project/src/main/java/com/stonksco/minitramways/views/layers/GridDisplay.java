package com.stonksco.minitramways.views.layers;

import com.stonksco.minitramways.logic.Vector2;
import com.stonksco.minitramways.views.GameView;
import com.stonksco.minitramways.views.layers.cells.GridDisplayCell;

public class GridDisplay extends GridView {



    public GridDisplay(GameView gw, Vector2 size) {
        super(gw,size);

        Fill(GridDisplayCell.class);



    }




}
