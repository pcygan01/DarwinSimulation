package agh.ics.oop;

import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public interface IMapChangeObserver {
    void mapChanged(SimulationEngine engine);
}
