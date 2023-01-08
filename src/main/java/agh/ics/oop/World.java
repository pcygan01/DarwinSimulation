package agh.ics.oop;

//import agh.ics.oop.gui.App;
import javafx.application.Application;

public class World {
    public static void main(String[] args) {

//        Application.launch(App.class, args);

        System.out.println("start");
        GrassField map = new GrassField(5, 6, 6, false, 5, false, 1);
        SimulationEngine engine = new SimulationEngine(map, 300, 5, 30, 15, 10,0,4,32, false, false);
        engine.run();
        System.out.println("stop");
    }
}

//f b r l f f r r f f f f f f f f

