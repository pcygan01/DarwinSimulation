package agh.ics.oop;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SimulationEngine implements IEngine{
    private GrassField map;
    private ArrayList<Animal> animals;
    private ArrayList<Animal> deadAnimals;
    private final int minEnergyToReproduce;
    private int moveDelay;


    public SimulationEngine(GrassField map, int moveDelay, int animalsAtStart, int startEnergy, int moveEnergy, int minEnergyToReproduce, int energyToReproduce, int minMutation, int maxMutation, int genesLength, boolean mutationType, boolean behaviorType){
        this.map = map;
        this.moveDelay = moveDelay;
        this.animals = new ArrayList<>();
        this.deadAnimals = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < animalsAtStart; i++){
            int randomX = random.nextInt(this.map.getUpperRight().x+1);
            int randomY = random.nextInt(this.map.getUpperRight().y+1);
            Animal animalToAdd = new Animal(map, new Vector2d(randomX, randomY), startEnergy, moveEnergy, minMutation, maxMutation, mutationType, behaviorType, genesLength, energyToReproduce);
            if (map.place(animalToAdd))
                animals.add(animalToAdd);
        }
        this.minEnergyToReproduce = energyToReproduce;

    }

    public void deleteDeadAnimals(){
        List<Animal> tmp = new ArrayList<>();
        for (Animal a: animals){
            if(a.getEnergy() <= 0){
                tmp.add(a);
                this.deadAnimals.add(a);
            }

        }
        for(Animal a: tmp){
            this.animals.remove(a);
            this.map.removeAnimal(a);
        }
    }
    public void moveAnimals(){
        for (Animal a: animals){
            a.move();
        }
    }

    public void eatPlants(){
        for(Animal a: animals){
            if(map.getGrassAt(a.getPosition()) != null){
                this.map.deleteGrassAtPos(a.getPosition());
                Animal an = this.map.getAnimalWhoEats(a.getPosition());
                an.addEnergy(this.map.getEnergyFromPlant());
            }
        }
    }

    public void spawnNewGrass(){
        for(int i = 0; i < this.map.getGrassesMoreEachDay(); i++){
            this.map.spawnGrass();
        }
    }

    public void liveDay(){
        deleteDeadAnimals();
        System.out.println("deleted dead animals");
        moveAnimals();
        System.out.println("moved animals");
        eatPlants();
        System.out.println("ate plants");
        //reproduce
        spawnNewGrass();
        System.out.println("spawned new grass");
    }
    public void run(){
//        while(true){
            try {
                Thread.sleep(moveDelay);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
//            while(true){
            for(int i = 0; i < 50; i++){
                System.out.println(this.map);
                liveDay();
                try {
                    Thread.sleep(moveDelay);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
//            }
        }
    }

    //metoda stworzona aby ulatwic robienie testow, nie jestem pewny czy to dobre wyjscie
    public ArrayList<Animal> getAnimals(){
        return animals;
    }
}
