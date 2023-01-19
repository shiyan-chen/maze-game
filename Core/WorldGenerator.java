package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.*;

public class WorldGenerator {
    private boolean[][] FLOOR;
    private boolean[][] WALL;
    private boolean[][] FLOWERS;
    private List<Room> rooms;
    private TETile[][] world;
    private Random random;

    private Avatar avatar;
    private TETile avatarType;
    private Fountain fountain;
    private int sizeOfView;

    public WorldGenerator(TETile[][] world, Random random, TETile avatarType) {
        this.rooms = new LinkedList<>();
        this.FLOOR = new boolean[world.length][world[0].length];
        this.WALL = new boolean[world.length][world[0].length];
        this.FLOWERS = new boolean[world.length][world[0].length];
        this.world = world;
        this.random = random;
        this.sizeOfView = 3;
        this.avatarType = avatarType;
        createWorld();
        createAvatar();
        createFountain();
//        changeView(); // comment this to see the full map at the beginning.
    }

    public WorldGenerator(TETile[][] world, Random random,
                          Position avatarPos, Position fountainPos) {
        this.rooms = new LinkedList<>();
        this.FLOOR = new boolean[world.length][world[0].length];
        this.WALL = new boolean[world.length][world[0].length];
        this.FLOWERS = new boolean[world.length][world[0].length];
        this.world = world;
        this.random = random;
        this.sizeOfView = 3;
        createWorld();
        createAvatarWithPos(avatarPos);
        createFountainWithPos(fountainPos);
        changeView();
    }

    private void createWorld() {
        setBackground();
        roomGenerator();
        hallwayGenerator();
        wallGenerator();
        flowerGenerator();
    }

    private void flowerGenerator() {
        for (Room room : rooms) {
            if (random.nextBoolean()) {
                int offsetX = random.nextInt(room.width);
                int offsetY = random.nextInt(room.height);
                int x = room.pos.x + offsetX;
                int y = room.pos.y + offsetY;
                changeTileType(x, y, Tileset.FLOWER);
                FLOWERS[x][y] = true;
            }
        }
    }

    private void eatFlower() {
        FLOWERS[getAvatarX()][getAvatarY()] = false;
        this.sizeOfView += 1;
    }

    private void changeView() {
        setBackground();
        int minOfBoundX = avatar.getX() - sizeOfView + 1;
        minOfBoundX = minOfBoundX < 0 ? 0 : minOfBoundX;
        int maxOfBoundX = avatar.getX() + sizeOfView - 1;
        maxOfBoundX = maxOfBoundX > world.length - 1 ? world.length - 1 : maxOfBoundX;
        int minOfBoundY = avatar.getY() - sizeOfView + 1;
        minOfBoundY = minOfBoundY < 0 ? 0 : minOfBoundY;
        int maxOfBoundY = avatar.getY() + sizeOfView - 1;
        maxOfBoundY = maxOfBoundY > world[0].length - 1 ? world[0].length - 1 : maxOfBoundY;
        for (int i = minOfBoundX; i <= maxOfBoundX; i++) {
            for (int j = minOfBoundY; j <= maxOfBoundY; j++) {
                if (i == avatar.getX() && j == avatar.getY()) {
                    changeTileType(i, j, avatar.getType());
                } else if (i == fountain.getX() && j == fountain.getY()) {
                    changeTileType(i, j, Tileset.WATER);
                } else if (FLOWERS[i][j]) {
                    changeTileType(i, j, Tileset.FLOWER);
                } else if (FLOOR[i][j]) {
                    changeTileType(i, j, Tileset.FLOOR);
                } else if (WALL[i][j]) {
                    changeTileType(i, j, Tileset.WALL);
                }
            }
        }
    }

    private boolean isWall(int x, int y) {
        return WALL[x][y];
    }

    public void moveAvatarUp() {
        if (!isWall(avatar.getX(), avatar.getY() + 1)) { // !
            //world[avatar.getX()][avatar.getY()] = Tileset.FLOOR;
            avatar.moveUp();
            if (FLOWERS[getAvatarX()][getAvatarY()]) {
                eatFlower();
            }
            changeView();
            world[avatar.getX()][avatar.getY()] = avatar.getType();
        } else {
            return;
        }
    }

    public void moveAvatarDown() {
        if (!isWall(avatar.getX(), avatar.getY() - 1)) { // !
            //world[avatar.getX()][avatar.getY()] = Tileset.FLOOR;
            avatar.moveDown();
            if (FLOWERS[getAvatarX()][getAvatarY()]) {
                eatFlower();
            }
            changeView();
            world[avatar.getX()][avatar.getY()] = avatar.getType();
        } else {
            return;
        }
    }

    public void moveAvatarRight() {
        if (!isWall(avatar.getX() + 1, avatar.getY())) { // !
            //world[avatar.getX()][avatar.getY()] = Tileset.FLOOR;
            avatar.moveRight();
            if (FLOWERS[getAvatarX()][getAvatarY()]) {
                eatFlower();
            }
            changeView();
            world[avatar.getX()][avatar.getY()] = avatar.getType();
        } else {
            return;
        }
    }

    public void moveAvatarLeft() {
        if (!isWall(avatar.getX() - 1, avatar.getY())) { // !
            //world[avatar.getX()][avatar.getY()] = Tileset.FLOOR;
            avatar.moveLeft();
            if (FLOWERS[getAvatarX()][getAvatarY()]) {
                eatFlower();
            }
            changeView();
            world[avatar.getX()][avatar.getY()] = avatar.getType();
        } else {
            return;
        }
    }

    public int getAvatarX() {
        return avatar.getX();
    }

    public int getAvatarY() {
        return  avatar.getY();
    }

    private void createAvatar() {
        Room room = rooms.get(0);
        while (true) {
            int offsetX = random.nextInt(room.width);
            int offsetY = random.nextInt(room.height);
            int x = room.pos.x + offsetX;
            int y = room.pos.y + offsetY;
            if (!FLOWERS[x][y]) {
                avatar = new Avatar(x, y, avatarType);
                changeTileType(x, y, avatar.getType());
                break;
            }
        }
    }

    private void createAvatarWithPos(Position position) {
        avatar = new Avatar(position.x, position.y, Tileset.AVATAR);
        world[avatar.getX()][avatar.getY()] = avatar.getType();
    }

    private void createFountain() {
        boolean isCreated = false;
        Room room = rooms.get(1);
        while (!isCreated) {
            int offsetX = random.nextInt(room.width);
            int offsetY = random.nextInt(room.height);
            int x = room.pos.x + offsetX;
            int y = room.pos.y + offsetY;
            if (!FLOWERS[x][y]) {
                fountain = new Fountain(x, y, Tileset.WATER);
                changeTileType(x, y, fountain.getType());
                isCreated = true;
            }
        }
    }

    private void createFountainWithPos(Position position) {
        fountain = new Fountain(position.x, position.y, Tileset.WATER);
        world[fountain.getX()][fountain.getY()] = fountain.getType();
    }

    public int getFountainX() {
        return fountain.getX();
    }

    public int getFountainY() {
        return fountain.getY();
    }

    private void setBackground() {
        for (int x = 0; x < world.length; x += 1) {
            for (int y = 0; y < world[0].length; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }
    }

    /**
     * Generate random rooms and render the tile as FLOOR.
     */
    private void roomGenerator() {
        for (int i = 0; i < 100; i++) {

            // random position.
            int x = 1 + random.nextInt(world.length - 1); // [1, 80)
            int y = 1 + random.nextInt(world[0].length - 2); // [1, 29)
            Position pos = new Position(x, y);

            // random width and height.
            int roomWidth = 3 + random.nextInt(5);
            int roomHeight = 3 + random.nextInt(5);

            // create a random room.
            if (x + roomWidth - 1 < world.length - 1
                    && y + roomHeight - 1 < world[0].length - 2) {
                Room room = new Room(pos, roomWidth, roomHeight);
                if (!isOverlapped(room)) {
                    rooms.add(room);
                    changeRoomTiles(room, Tileset.FLOOR);
                }
            }
        }
    }

    private boolean isOverlapped(Room room) {
        for (int i = room.pos.x; i < room.pos.x + room.width; i++) {
            for (int j = room.pos.y; j < room.pos.y + room.height; j++) {
                if (isOccupied(i, j)) {
                    return true;
                }
            }
        }
        return false;
    }

    private void changeRoomTiles(Room room, TETile tile) {
        for (int i = room.pos.x; i < room.pos.x + room.width; i++) {
            for (int j = room.pos.y; j < room.pos.y + room.height; j++) {
                changeTileType(i, j, tile);
                //roomTiles[i][j] = true;
                FLOOR[i][j] = true;
            }
        }
    }

    private void hallwayGenerator() {
        RoomsInKDTree connectedRooms = new RoomsInKDTree();
        for (Room room : rooms) {
            if (connectedRooms.isEmpty()) {
                connectedRooms.insert(room);
                continue;
            }
            // find the nearest room of the current room.
            Room nearestRoom = connectedRooms.nearest(room);
            // connect the current room with the nearest room.
            connectTwoRooms(room, nearestRoom, Tileset.FLOOR);
            // insert current room into the already connected rooms.
            connectedRooms.insert(room);
        }

    }

    /**
     * Helper methods for generating hallways.
     */
    private void connectTwoRooms(Room r1, Room r2, TETile tile) {
        Position center1 = new Position(r1.pos.x + r1.width / 2,
                r1.pos.y + r1.height / 2);
        Position center2 = new Position(r2.pos.x + r2.width / 2,
                r2.pos.y + r2.height / 2);
        if (center1.x <= center2.x && center1.y <= center2.y) {
            changeTilesInRow(center1.x, center2.x, center1.y, tile);
            changeTilesInCol(center1.y, center2.y, center2.x, tile);
        } else if (center1.x <= center2.x && center1.y >= center2.y) {
            changeTilesInRow(center1.x, center2.x, center2.y, tile);
            changeTilesInCol(center2.y, center1.y, center1.x, tile);
        } else if (center1.x >= center2.x && center1.y <= center2.y) {
            changeTilesInRow(center2.x, center1.x, center1.y, tile);
            changeTilesInCol(center1.y, center2.y, center2.x, tile);
        } else {
            changeTilesInRow(center2.x, center1.x, center2.y, tile);
            changeTilesInCol(center2.y, center1.y, center1.x, tile);
        }
    }

    /**
     * Render hallways in row as FLOOR.
     */
    private void changeTilesInRow(int x1, int x2, int y, TETile tile) {
        for (int i = x1; i <= x2; i++) {
            if (!isOccupied(i, y)) {
                changeTileType(i, y, tile);
            }
            //hallwayTiles[i][y] = true;
            FLOOR[i][y] = true;
        }
    }

    /**
     * Render hallways in col as FLOOR.
     */
    private void changeTilesInCol(int y1, int y2, int x, TETile tile) {
        for (int i = y1; i <= y2; i++) {
            if (!isOccupied(x, i)) {
                changeTileType(x, i, tile);
            }
            //hallwayTiles[x][i] = true;
            FLOOR[x][i] = true;
        }
    }


    private boolean isOccupied(int x, int y) {
        //return roomTiles[x][y];
        return FLOOR[x][y];
    }

    private void changeTileType(int x, int y, TETile tile) {
        world[x][y] = tile;
    }

    /**
     * Render the WALL.
     */
    private void wallGenerator() {
        int[][] neighbours = {{-1, -1}, {-1, 0}, {-1, 1}, {0, -1},
            {0, 1}, {1, -1}, {1, 0}, {1, 1}};
        for (int i = 0; i < world.length; i++) {
            for (int j = 0; j < world[0].length; j++) {
                // if world[i][j] is room or hallway, then go to next tile.
                if (FLOOR[i][j]) {
                    continue;
                }

                for (int[] neighbour : neighbours) {
                    int x = i + neighbour[0];
                    int y = j + neighbour[1];
                    // make sure the neighbour tile is in the world realm.
                    if (x >= 0 && x < world.length
                            && y >= 0 && y < world[0].length) {
                        if (FLOOR[x][y]) {
                            changeTileType(i, j, Tileset.WALL);
                            WALL[i][j] = true;
                            break;
                        }
                    }
                }
            }
        }
    }

    public boolean gameOver() {
        return getAvatarX() == getFountainX() && getAvatarY() == getFountainY();
    }
}
