package agh.ics.oop;

import agh.ics.oop.gui.App;
import agh.ics.oop.gui.StatisticsChart;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.time.DateTimeException;
import java.time.LocalDateTime;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.*;


public class SimulationEngine implements IEngine, Runnable {
    private GrassField map;
    private ArrayList<Animal> animals;
    private ArrayList<Animal> deadAnimals;
    private final int minEnergyToReproduce;
    private final int energyToReproduce;
    private int moveDelay;
    private final int minMutation;
    private final int maxMutation;
    private final boolean mutationType;
    private Animal trackedAnimal = null;
    private final LinkedList<IMapChangeObserver> observers;
    private int days;
    private final int startEnergy;
    public boolean isRunning = true;
    VBox statsBox;
    VBox animalBox;
    GridPane grid;
    StatisticsChart[] statisticsCharts;
    private boolean savingToFile;
    LocalDateTime time;


    public SimulationEngine(App application, GridPane grid, GrassField map, VBox statsBox, StatisticsChart[] statisticsCharts, VBox animalBox, int moveDelay, int animalsAtStart, int startEnergy, int minEnergyToReproduce, int energyToReproduce, int minMutation, int maxMutation, int genesLength, boolean mutationType, boolean behaviorType, boolean savingToFile) {
        this.map = map;
        this.moveDelay = moveDelay;
        this.animals = new ArrayList<>();
        this.deadAnimals = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < animalsAtStart; i++) {
            int randomX = random.nextInt(this.map.getUpperRight().x + 1);
            int randomY = random.nextInt(this.map.getUpperRight().y + 1);
            Animal animalToAdd = new Animal(map, new Vector2d(randomX, randomY), startEnergy, behaviorType, genesLength, energyToReproduce);
            if (map.place(animalToAdd))
                animals.add(animalToAdd);
        }
        this.minEnergyToReproduce = minEnergyToReproduce;
        this.energyToReproduce = energyToReproduce;
        this.minMutation = minMutation;
        this.maxMutation = maxMutation;
        this.mutationType = mutationType;
        this.startEnergy = startEnergy;
        this.days = 0;
        this.statsBox = statsBox;
        this.statisticsCharts = statisticsCharts;
        this.animalBox = animalBox;
        this.grid = grid;
        this.savingToFile = savingToFile;
        this.time = LocalDateTime.now();

        this.observers = new LinkedList<>();
        addObserver(application);

    }

    public void deleteDeadAnimals() {
        List<Animal> tmp = new ArrayList<>();
        for (Animal a : animals) {
            if (a.getEnergy() <= 0) {
                tmp.add(a);
                this.deadAnimals.add(a);
            }

        }
        for (Animal a : tmp) {
            a.getPosition().incrementDeadAnimals();
            this.map.updateVectors(a.getPosition());
            a.setDeathDay(this.days);
            this.animals.remove(a);
            this.map.removeAnimal(a);

        }
    }

    public void moveAnimals() {
        for (Animal a : animals) {
            a.move();
        }
    }

    public void eatPlants() {
        for (Animal a : animals) {
            if (map.getGrassAt(a.getPosition()) != null) {
                this.map.deleteGrassAtPos(a.getPosition());
                Animal an = this.map.getAnimalWhoEats(a.getPosition());
                an.addEnergy(Math.min(this.map.getEnergyFromPlant(), this.startEnergy - an.getEnergy()));
                an.eatPlant();
            }
        }
    }

    public void spawnNewGrass() {
        for (int i = 0; i < this.map.getGrassesMoreEachDay(); i++) {
            this.map.spawnGrass();
        }
    }

    public void breedAnimals() {
        Map<Vector2d, Boolean> checked = new HashMap<>();
        List<Animal> childAnimals = new ArrayList<>();
        for (Animal a : animals) {
            if (this.map.getAnimalsAt(a.getPosition()).size() < 2) {
                continue;
            }
            if (checked.get(a.getPosition()) != null) {
                continue;
            }
            List<Animal> potentialParents = this.map.getParentsAtPos(a.getPosition());
            checked.put(a.getPosition(), true);
            if (potentialParents.get(0).getEnergy() < this.minEnergyToReproduce || potentialParents.get(1).getEnergy() < minEnergyToReproduce) {
                continue;
            }
            Animal kid = new Animal(this.map, a.getPosition(), 2 * energyToReproduce, minMutation, maxMutation, energyToReproduce, mutationType, a.getBehaviorType(), potentialParents.get(0), potentialParents.get(1));
            potentialParents.get(0).hasKid();
            potentialParents.get(1).hasKid();
            potentialParents.get(0).addEnergy(-energyToReproduce);
            potentialParents.get(1).addEnergy(-energyToReproduce);
            childAnimals.add(kid);
        }
        for (Animal a : childAnimals) {
//            System.out.println("new kid");
            map.place(a);
            this.animals.add(a);
        }
    }

    public void endDay() {
        for (Animal a : animals) {
            a.addEnergy(-1);
            a.addAge();
        }
        this.days += 1;
    }

    public void liveDay() {
        deleteDeadAnimals();
//        System.out.println("deleting done");
        moveAnimals();
//        System.out.println("moving done");
        eatPlants();
//        System.out.println("eating done");
        breedAnimals();
//        System.out.println("breeding done");
        spawnNewGrass();
//        System.out.println("spawning done");
    }

    public void pause() {
        this.isRunning = false;
    }

    public void start() {
        this.isRunning = true;
    }


    public void run() {
        while (true) {
            try {
                Thread.sleep(this.moveDelay);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            while (this.isRunning) {
                System.out.println(getDays());
//                System.out.println(this.map);
                liveDay();
                endDay();
                update();
                if (this.savingToFile) {
                    saveToCSV();
                }
                try {
                    Thread.sleep(this.moveDelay);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

            }
        }

    }


    public void setAnimalTracked(Vector2d pos) {
        this.trackedAnimal = this.map.getAnimalWhoEats(pos);
        System.out.println("śledzony");
    }

    public void stopTracking() {
        this.trackedAnimal = null;
    }

    public ArrayList<Animal> getAnimals() {
        return animals;
    }

    //for statistics:

    public int allAnimalsCount() {
        return this.animals.size();
    }

    public int deadAnimalsCount() {
        return this.deadAnimals.size();
    }

    public int allGrassesCount() {
        return this.map.getGrassesCount();
    }

    public double getAverageEnergy() {
        int sum = 0;
        for (Animal a : this.animals) {
            sum += a.getEnergy();
        }
        return (sum * 1.0) / allAnimalsCount();
    }

    public double getAverageLifeSpan() { //(for dead animals)
        int sum = 0;
        for (Animal a : this.deadAnimals) {
            sum += a.getDaysLived();
        }
        if (this.deadAnimalsCount() == 0) {
            return 0;
        }
        return (sum * 1.0) / this.deadAnimalsCount();

    }

    public int getBothAtTheSameVectorCount() {
        int sum = 0;
        for (Animal a : this.animals) {
            if (this.map.getGrassAt(a.getPosition()) != null) {
                sum += 1;
            }
        }
        return sum;
    }

    //TODO: duzo zwierzat moze byc na jednym polu -> trzeba sprawdzac po wektorach
    public int getFreeFieldsCount() {
        return (this.map.getUpperRight().x + 1) * (this.map.getUpperRight().y + 1) - this.allAnimalsCount() - this.allGrassesCount() + getBothAtTheSameVectorCount();
    }

    public int getMostPopularGenes() { //chyba int[] bedzie najlepiej
        int[] counter = {0, 0, 0, 0, 0, 0, 0, 0};
        for (Animal a : this.animals) {
            String genes = a.getGenes().toString();
            for (int i = 0; i < genes.length(); i++) {
                int c = genes.charAt(i) - '0';
                counter[c] += 1;
            }
        }
        int max = -1;
        int index = -1;
        for (int i = 0; i < 8; i++) {
            System.out.println("" + i + " " + counter[i]);
            if (max < counter[i]) {
                max = counter[i];
                index = i;
            }
        }
        return index;
    }


    public int getDays() {
        return this.days;
    }

    public VBox getStatsBox() {
        return this.statsBox;
    }

    public GridPane getGrid() {
        return this.grid;
    }

    public GrassField getMap() {
        return this.map;
    }

    public VBox getAnimalBox() {
        return this.animalBox;
    }

    public Animal getTrackedAnimal() {
        return this.trackedAnimal;
    }

    public StatisticsChart[] getStatisticsCharts() {
        return this.statisticsCharts;
    }


    //dla animal statistics mozna chyba brac z animala

    public void addObserver(IMapChangeObserver observer) {
        this.observers.push(observer);
    }


    public void update() {
        for (IMapChangeObserver observer : this.observers) {
            observer.mapChanged(this);
        }
    }

    @Override
    public void mapChanged(SimulationEngine engine) throws Exception {
    }


    // Zapisywanie do pliku CSV

    public void saveToCSV() {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd_MM_yyyy_HH_mm_ss");
        String newTime = this.time.format(format);

        try {
            File file = new File("src/main/resources/data"+newTime+".csv");
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try (FileWriter fileWriter = new FileWriter("src/main/resources/data"+newTime+".csv", true)){

            int dayS = this.days;
            int liveAnimalsS = this.allAnimalsCount();
            int plantsS = this.allGrassesCount();
            double averageEnergyS = this.getAverageEnergy();
            double averageLifespanS = this.getAverageLifeSpan();
            int getMostPopularGeneS = this.getMostPopularGenes();

            String data = String.valueOf(dayS) + ',' + liveAnimalsS + ',' + plantsS + ',' + averageEnergyS + ',' + averageLifespanS +',' +getMostPopularGeneS+'\n';
            fileWriter.write(data);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

}
