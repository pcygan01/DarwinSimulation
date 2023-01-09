package agh.ics.oop;


import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Animal extends AbstractWorldMapElement{
    private MapDirection direction;
    private GrassField map;
    private int energy;
    private final int moveEnergy;
//    private final int startEnergy;
    private final Gene genes;
    private int childrenAmount;
    private int daysLived;

//    private final boolean mutationType; //0 - pełna losowość, 1 - lekka korekta
    private final boolean behaviorType; //0 - pełna predestynacja, 1 - nieco szaleństwa

    private final int energyToReproduce;
    private int plantsEaten;

    private int deathDay = -1;

    private int activatedGene;


    //at start
    public Animal(GrassField map, Vector2d initialPosition, int energy, boolean behaviorType, int genesLength, int energyToReproduce){
        super(initialPosition);
        this.direction = MapDirection.NORTH;
        this.map = map;
        this.observers = new ArrayList<>();
        this.energy = energy;
        this.moveEnergy = 1;
        //this.startEnergy = startEnergy;
        this.genes = new Gene(genesLength);
        this.childrenAmount = 0;
        this.daysLived = 0;
        this.behaviorType = behaviorType;
        this.energyToReproduce = energyToReproduce;
        this.plantsEaten = 0;

    }

    //kid
    public Animal(GrassField map, Vector2d initialPosition, int energy, int minMutation, int maxMutation, int energyToReproduce, boolean mutationType, boolean behaviorType, Animal parent1, Animal parent2){
        super(initialPosition);
        this.direction = MapDirection.NORTH;
        this.map = map;
        this.observers = new ArrayList<>();
        this.energy = energy;
        this.moveEnergy = 1;
        //this.startEnergy = startEnergy;
        this.genes = new Gene(parent1, parent2, mutationType, minMutation, maxMutation);
        this.childrenAmount = 0;
        this.daysLived = 0;
        this.behaviorType = behaviorType;
        this.energyToReproduce = energyToReproduce;
        this.plantsEaten = 0;

    }
    public String toString(){
        return (this.direction.toString());
    }

    @Override
    public String getPath() {
        return "src/main/resources/zebra.png";
    }

    public MapDirection getDirection() {
        return direction;
    }

    public void addObserver(IPositionChangeObserver observer){
        observers.add(observer);
    }
    public void removeObserver(IPositionChangeObserver observer){
        observers.remove(observer);
    }
    public void positionChanged(Vector2d newPosition){
        for(IPositionChangeObserver observer: observers){
            observer.positionChanged(this, this.position, newPosition);
        }
        this.position = newPosition;
    }

    public void move(){
        Vector2d newPos;
        MapDirection moveDirection = this.genes.getMove(this.behaviorType);
        this.activatedGene = this.genes.getActivatedGene();
        boolean moved = true;
        newPos = this.position.add(moveDirection.toUnitVector());
        this.direction = moveDirection;
        if (!this.map.getBorderType()){//ziemia
            if (newPos.x < this.map.getLowerLeft().x){
                newPos = new Vector2d(this.map.getUpperRight().x, newPos.y);
            } else if (newPos.x > this.map.getUpperRight().x) {
                newPos = new Vector2d(this.map.getLowerLeft().x, newPos.y);
            }
            if (newPos.y < this.map.getLowerLeft().y || newPos.y > this.map.getUpperRight().y){
                this.direction = this.direction.opposite();
                moved = false;
            }
        }
        else{//piekielny portal
            if(!(newPos.follows(this.map.getLowerLeft()) && newPos.precedes(this.map.getUpperRight()))){
               this.energy = this.energy - this.energyToReproduce;
               Random random = new Random();
               int randomX = random.nextInt(this.map.getUpperRight().x+1);
               int randomY = random.nextInt(this.map.getUpperRight().y+1);
               newPos = new Vector2d(randomX, randomY);
            }
        }
        if(moved){
            if (this.map.canMoveTo(newPos)){ //chyba zwykle move no bo moze
//                System.out.println("moved to " + newPos);
                //this.position = newPos;
                positionChanged(newPos);
            }
        }

    }


    public int getDaysLived(){
        return daysLived;
    }
    public Gene getGenes(){
        return genes;
    }
    public void addEnergy(int energyToAdd){

        this.energy += energyToAdd;
    }
    public void addAge(){
        this.daysLived += 1;
    }

    public int getChildrenAmount(){
        return childrenAmount;
    }
    public int getEnergy(){
        return energy;
    }

    public void hasKid(){
        this.childrenAmount += 1;
    }
    public boolean getBehaviorType(){
        return this.behaviorType;
    }

    public void eatPlant(){
        this.plantsEaten+=1;
    }

    public int getPlantsEaten(){
        return this.plantsEaten;
    }

    public void setDeathDay(int day){
        this.deathDay = day;
    }
    public int getDeathDay(){
        return this.deathDay;
    }

    public void setActivatedGene(int g){
        this.activatedGene = g;
    }
    public int getActivatedGene(){
        return this.activatedGene;
    }

}