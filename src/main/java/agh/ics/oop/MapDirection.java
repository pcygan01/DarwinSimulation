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

    MapDirection next() {
        return switch (this) {
            case NORTH -> NORTHEAST;
            case NORTHEAST -> EAST;
            case EAST -> SOUTHEAST;
            case SOUTHEAST -> SOUTH;
            case SOUTH -> SOUTHWEST;
            case SOUTHWEST -> WEST;
            case WEST -> NORTHWEST;
            case NORTHWEST -> NORTH;
        };
    }

    MapDirection previous(){
        return switch (this) {
            case NORTH -> NORTHWEST;
            case NORTHEAST -> NORTH;
            case EAST -> NORTHEAST;
            case SOUTHEAST -> EAST;
            case SOUTH -> SOUTHEAST;
            case SOUTHWEST -> SOUTH;
            case WEST -> SOUTHWEST;
            case NORTHWEST -> WEST;
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
