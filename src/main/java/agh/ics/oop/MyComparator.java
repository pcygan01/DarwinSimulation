package agh.ics.oop;

import java.util.Comparator;

public class MyComparator implements Comparator<Animal> {


    public MyComparator(){
    }

    @Override
    public int compare(Animal a1, Animal a2){
        if (a1.getEnergy() > a2.getEnergy())
            return -1;
        else if(a1.getEnergy() < a2.getEnergy()){
            return 1;
        }
        if(a1.getDaysLived() > a2.getDaysLived()){
            return -1;
        }
        else if(a1.getDaysLived() < a2.getDaysLived()){
            return 1;
        }
        if(a1.getChildrenAmount() > a2.getChildrenAmount()){
            return -1;
        }
        else if(a1.getChildrenAmount() < a2.getChildrenAmount()){
            return 1;
        }
        return -1;
    }
}
