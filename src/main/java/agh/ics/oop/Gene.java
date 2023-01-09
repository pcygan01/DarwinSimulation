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
    public Gene(Animal animal1, Animal animal2, boolean mutationType, int minMutation, int maxMutation){
        Random r = new Random();
        int[] a1Genes = animal1.getGenes().getGenes();
        int[] a2Genes = animal2.getGenes().getGenes();
        this.n = animal1.getGenes().getGenes().length;
        this.genes = new int[n];
        this.moveNumber = 0;

        int side = r.nextInt(2);
        if(side == 0){ //silny po lewej
            int mergeIndex = (animal1.getEnergy() * this.n)/(animal1.getEnergy() + animal2.getEnergy());
            for(int id = 0; id < mergeIndex; id++){
                this.genes[id] = a1Genes[id];
            }
            for(int id = mergeIndex; id < this.n; id++){
                this.genes[id] = a2Genes[id];
            }
        }
        else{//silny po prawej
            int mergeIndex = (animal2.getEnergy() * this.n)/(animal1.getEnergy() + animal2.getEnergy());
            for(int id = 0; id < mergeIndex; id++){
                this.genes[id] = a2Genes[id];
            }
            for(int id = mergeIndex; id < this.n; id++){
                this.genes[id] = a1Genes[id];
            }
        }
        //MUTACJE
        int mutationsNumber = r.nextInt(maxMutation - minMutation) + minMutation;
        boolean[] tmp = new boolean[this.n];
        Arrays.fill(tmp, true);
        for(int i = 0; i < mutationsNumber; i++){
            while(true){
                int id = r.nextInt(this.n);
                if(tmp[id]){
                    if(!mutationType){
                        this.genes[id] = r.nextInt(8);
                    }
                    else{
                        int minusOrPlus = r.nextInt(2);
                        if (minusOrPlus == 0) {//modulo -1%8 does not work what
                            this.genes[id] = this.genes[id]-1;
                            if (this.genes[id] == -1){
                                this.genes[id] = 7;
                            }
                        }
                        else{
                            this.genes[id] =(this.genes[id]+1) % 8;

                        }
                    }
                    tmp[id] = false;
                    break;
                }
            }
        }

    }

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

    public int getMoveNumber(){
        return moveNumber;
    }

    public String toString(){
        StringBuilder s = new StringBuilder();
        for(int el: this.genes){
            s.append(Integer.toString(el));
        }
        return s.toString();

    }


}
