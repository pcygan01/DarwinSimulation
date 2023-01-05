package agh.ics.oop;

import java.util.Arrays;
import java.util.Random;

public class Gene {
    private int n;
    private int[] genes;
    Random r = new Random();
    private int moveNumber;
    public Gene(int n) {
        this.n = n;
        this.moveNumber = 0;
        this.genes = new int[this.n];
        for (int i = 0; i < this.n; i++)
            this.genes[i] = r.nextInt(8);
        //Arrays.sort(this.genes);
    }
    public Gene(int[] genes) {
        this.genes = genes;
    }
    //TODO:
//    public Gene(Animal animal1, Animal animal2){
//    }

    public MapDirection getMove(boolean behaviorType){
        //false - jeden po drugim zawsze, true - 80% że nastepny, 20% ze losowy
        if (!behaviorType){
            moveNumber = (moveNumber+1)%n; //starts at 1
        }
        else{
            int rInt = r.nextInt(10);
            if (rInt == 9 || rInt == 8){
                moveNumber = r.nextInt(n);
            }
            else{
                moveNumber = (moveNumber+1)%n;
            }
        }

        return toMapDirection(this.genes[moveNumber]);
    }
    public int[] getGenes(){
        return this.genes;
    }

    public MapDirection toMapDirection(int gene){
        return switch(gene){
            case 0 -> MapDirection.NORTH;
            case 1 -> MapDirection.NORTHEAST;
            case 2 -> MapDirection.EAST;
            case 3 -> MapDirection.SOUTHEAST;
            case 4 -> MapDirection.SOUTH;
            case 5 -> MapDirection.SOUTHWEST;
            case 6 -> MapDirection.WEST;
            case 7 -> MapDirection.NORTHWEST;
            default -> null;
        };
    }

    public void mutate(boolean mutationType){
        //false - pełna losowość, true - 1 w dół lub 1 w góre
        int rInt = r.nextInt(n);
    }


}
