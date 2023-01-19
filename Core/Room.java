package byow.Core;

public class Room {

    int width;
    int height;
    Position pos; // Bottom left position.

    public Room(Position pos, int width, int height) {
        this.width = width;
        this.height = height;
        this.pos = pos;
    }

    public static double distanceBetweenTwoRooms(Room r1, Room r2) {
        return Math.sqrt(Math.pow(r1.pos.x - r2.pos.x, 2) + Math.pow(r1.pos.y - r2.pos.y, 2));
    }
}
