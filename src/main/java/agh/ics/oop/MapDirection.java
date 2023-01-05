package agh.ics.oop;

public enum MapDirection {
    NORTH,
    NORTHEAST,
    EAST,
    SOUTHEAST,
    SOUTH,
    SOUTHWEST,
    WEST,
    NORTHWEST;

    public String toString(){
        return switch(this){
            case NORTH -> "^";
            case SOUTH -> "v";
            case EAST -> ">";
            case WEST -> "<";
            default -> null;
        };
    }

    MapDirection opposite() {
        return switch (this) {
            case NORTH -> SOUTH;
            case NORTHEAST -> SOUTHWEST;
            case EAST -> WEST;
            case SOUTHEAST -> NORTHWEST;
            case SOUTH -> NORTH;
            case SOUTHWEST ->NORTHEAST;
            case WEST -> EAST;
            case NORTHWEST -> SOUTHEAST;
        };
    }


    Vector2d toUnitVector(){
        return switch(this){
            case NORTH -> new Vector2d(0, 1);
            case NORTHEAST -> new Vector2d(1, 1);
            case NORTHWEST -> new Vector2d(-1, 1);
            case EAST -> new Vector2d(1, 0);
            case SOUTH -> new Vector2d(0, -1);
            case SOUTHEAST -> new Vector2d(1, -1);
            case SOUTHWEST -> new Vector2d(-1, -1);
            case WEST -> new Vector2d(-1, 0);
        };
    }
}
