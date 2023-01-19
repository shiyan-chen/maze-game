package byow.Core;

import byow.TileEngine.TETile;

public class Fountain {
    private Position pos;
    private TETile type;

    public Fountain(int x, int y, TETile tileType) {
        pos = new Position(x, y);
        type = tileType;
    }

    public int getX() {
        return pos.x;
    }

    public int getY() {
        return pos.y;
    }

    public TETile getType() {
        return type;
    }

}

