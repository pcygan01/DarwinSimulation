package agh.ics.oop;

//import agh.ics.oop.gui.App;
import javafx.application.Application;

public class World {
    public static void main(String[] args) {

//        Application.launch(App.class, args);

        System.out.println("start");
        GrassField map = new GrassField(5, 6, 6, true, 20, false, 2);
        SimulationEngine engine = new SimulationEngine(map, 300, 2, 200, 30, 100,80,0,2, 32, false, false);
        engine.run();
        System.out.println("stop");
    }
}

//f b r l f f r r f f f f f f f f

