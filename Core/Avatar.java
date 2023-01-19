package byow.Core;

import byow.TileEngine.TETile;

public class Avatar {
    private Position pos;
    private TETile type;

    public Avatar(int x, int y, TETile tileType) {
        this.pos = new Position(x, y);
        this.type = tileType;
    }

    public void moveUp() {
        this.pos = new Position(this.pos.x, this.pos.y + 1);
    }

    public void moveDown() {
        this.pos = new Position(this.pos.x, this.pos.y - 1);
    }

    public void moveRight() {
        this.pos = new Position(this.pos.x + 1, this.pos.y);
    }

    public void moveLeft() {
        this.pos = new Position(this.pos.x - 1, this.pos.y);
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
