package agh.ics.oop;

import javafx.scene.layout.GridPane;

public interface IMapChangeObserver {
    void mapChanged(GrassField map, GridPane worldMap);
}
