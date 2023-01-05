package agh.ics.oop;
import java.util.*;

import static java.lang.Math.*;





public class GrassField implements IWorldMap, IPositionChangeObserver{
    private final int n;
    private Map<Vector2d, ArrayList<Animal>> animals;
    private Map<Vector2d, Grass> grasses;
//    private MapBoundary mapBoundaries;
    private final boolean borderType; //0 - kula ziemska, 1 - piekielny portal
    private final boolean grassesType; //0 - zalesione równiki, 1 - toksyczne trupy
    //private final int mutationType; //0 - pełna losowość, 1 - lekka korekta
    //private final int behaviorType; //0 - pełna predestynacja, 1 - nieco szaleństwa

    private final int energyFromPlant;
    private final Vector2d lowerLeft;
    private final Vector2d upperRight;
    private final int grassesMoreEachDay;
    private final int jungleHeight;
    private final int jungleBottom;
    private final int jungleSize;
    private final int noJungleSize; //kiedy miejsce na trawe sie skonczy to nie spawni
    private int grassesInJungle;
    private List<Vector2d> sortedVectors;
    private final int jungleSizeIfGT1;


    public GrassField(int n, int height, int width, boolean borderType, int energyFromPlant, boolean grassesType, int grassesMoreEachDay){
        this.lowerLeft = new Vector2d(0,0);
        this.upperRight = new Vector2d(width-1, height-1);
        this.borderType = borderType;
        this.energyFromPlant = energyFromPlant;
        this.grassesType = grassesType;
        this.n = n;
        this.grasses = new HashMap<>();
        this.grassesMoreEachDay = grassesMoreEachDay;
        this.animals = new HashMap<>();
        //only for grassesType 0
        this.jungleHeight = (int) Math.ceil(height/5.0);
        this.jungleBottom = (height / 2) - (jungleHeight / 2);
        this.jungleSize = jungleHeight * width;
        this.noJungleSize = height * width - jungleSize;
        this.grassesInJungle = 0;
        //only for grassesType 1
        this.sortedVectors = new ArrayList<>();
        this.jungleSizeIfGT1 = (int) Math.ceil(height*width/5.0);

        for(int i = 0; i < n; i++){
            while (true)
                if (spawnGrass())
                    break;
        }
    }

    public boolean place(Animal animal) {
        if (canMoveTo(animal.getPosition())) {
            List<Animal> animalsToAdd = this.animals.computeIfAbsent(animal.getPosition(), k -> new ArrayList<>());
            animalsToAdd.add(animal);
            animal.addObserver(this);
            return true;
        }
        throw new IllegalArgumentException(animal.getPosition() + " is not a valid position to place to");
    }

    @Override
    public boolean canMoveTo(Vector2d position) {
        return true;
    }

    @Override
    public Object objectAt(Vector2d position){
        if (animals.get(position) == null)
            return grasses.get(position);
        if (animals.get(position).size() == 0)
            return grasses.get(position);
        //TODO:
        //return getBestAnimal(position);
    }


    public void positionChanged(Animal a, Vector2d oldPosition, Vector2d newPosition) {
        List<Animal> animalsOnSquare = this.animals.get(oldPosition);
        List<Animal> newAnimalsOnSquare = this.animals.computeIfAbsent(newPosition, k -> new ArrayList<>());

        animalsOnSquare.remove(a);
        newAnimalsOnSquare.add(a);
        //not sure
    }

    public boolean getBorderType(){
        return this.borderType;
    }
    public Vector2d getLowerLeft(){
        return this.lowerLeft;
    }

    public Vector2d getUpperRight(){
        return this.upperRight;
    }

    public boolean spawnGrass(){
        Random random = new Random();
        int rInt = random.nextInt(10);
        if (!grassesType){ //rownik
            if(grassesInJungle < jungleSize && (rInt == 8 || rInt == 9)) {
                while(true){
                    Vector2d pos = new Vector2d(random.nextInt(this.upperRight.x + 1), random.nextInt(jungleHeight) + jungleBottom);
                    if (grasses.get(pos) == null) {
                        this.grasses.put(pos, new Grass(pos));
                        return true;
                    }
                }
            }
            if (grasses.size() < (this.upperRight.x+1) * (this.upperRight.y+1)){
                //spawnij poza jungla
                while(true){
                    int randomY = random.nextInt(this.upperRight.y+1-jungleHeight);
                    if(randomY >= jungleBottom){
                        randomY += jungleHeight;
                    }
                    Vector2d pos = new Vector2d(random.nextInt(random.nextInt(this.upperRight.x + 1)), randomY);
                    if (grasses.get(pos) == null) {
                        this.grasses.put(pos, new Grass(pos));
                        return true;
                    }
                }
            }
            return false; // nie da sie zakladamy ze przed wywoalniem sprawdzamy czy nie ma wszystkich
        }
        else{//truposze
            if(rInt == 8 || rInt == 9){
                boolean isPlace = false;
                for(int i = 0; i < jungleSizeIfGT1; i++){
                    if (grasses.get(sortedVectors.get(i))==null){
                        isPlace = true;
                    }
                }
                if(isPlace){
                    while(true){
                        int rId = random.nextInt(jungleSizeIfGT1);
                        Vector2d pos = sortedVectors.get(rId);
                        if (grasses.get(pos) == null) {
                            this.grasses.put(pos, new Grass(pos));
                            return true;
                        }
                    }
                }
            }
            if((grasses.size() < (this.upperRight.x+1) * (this.upperRight.y+1))) {
                while (true) {
                    int rId = random.nextInt(sortedVectors.size() - jungleSizeIfGT1) + jungleSizeIfGT1;
                    Vector2d pos = sortedVectors.get(rId);
                    if (grasses.get(pos) == null) {
                        this.grasses.put(pos, new Grass(pos));
                        return true;
                    }
                }
            }
            return false;
        }
    }

    public void initializeVectors(){
        for(int i = 0; i <= this.upperRight.x; i++){
            for(int j = 0; j <= this.upperRight.y; j++){
                this.sortedVectors.add(new Vector2d(i, j));
            }
        }
    }
    public void updateVectors(Vector2d vector){ //when animals die
        //TODO: update this
        int i = this.sortedVectors.indexOf(vector);
        while (i + 1 < sortedVectors.size() && sortedVectors.get(i+1).getDeadAnimals() < vector.getDeadAnimals()){
            Collections.swap(sortedVectors, i, i+1);
        }
        while (i - 1 > -1 && sortedVectors.get(i-1).getDeadAnimals() > vector.getDeadAnimals()){
            Collections.swap(sortedVectors, i, i-1);
        }
    }
    public String toString(){
        return new MapVisualizer(this).draw(getLowerLeft(), getUpperRight());
    }


//    public String toString(){
//        return super.toString();
//    }





}
