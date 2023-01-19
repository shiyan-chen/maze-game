package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
import java.io.IOException;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.BufferedReader;

/**
 * @Source Hug's InputDemo/StringInputDevice
 */

public class Engine {
    TERenderer ter = new TERenderer();
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;

    private TETile[][] world = new TETile[WIDTH][HEIGHT];
    private long seed;
    private TETile avatarType = Tileset.AVATAR;
    private WorldGenerator worldGenerator = null;
    private StringBuilder seedBuilder = new StringBuilder();

    private void showTileInfo() {
        int x = (int) StdDraw.mouseX();
        int y = (int) StdDraw.mouseY();
        if (y == 30) {
            y = 29;
        }
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.textLeft(1, HEIGHT - 1, world[x][y].description());
        StdDraw.show();
    }

    private void gameOver() {
        Font fontBig = new Font("Monaco", Font.BOLD, 30);
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.PINK);
        StdDraw.setFont(fontBig);
        StdDraw.text(WIDTH / 2, (HEIGHT / 9) * 5, "Game Over. You Win!");
        StdDraw.text(WIDTH / 2, (HEIGHT / 9) * 4, "Try another game (N)");
        StdDraw.text(WIDTH / 2, (HEIGHT / 9) * 3, "Quit (Q)");
        StdDraw.show();
    }

    private void renderNewWorld() {
        ter.initialize(WIDTH, HEIGHT);
        seed = Long.parseLong(seedBuilder.toString());
        Random random = new Random(seed);
        worldGenerator = new WorldGenerator(world, random, avatarType);
        ter.renderFrame(world);
    }

    private void saveGame() {
        if (worldGenerator != null) {
            try {
                FileWriter writer = new FileWriter("./save.txt", false);
                // First line is the seed
                writer.write(Long.toString(seed));
                writer.write("\n");
                // Second line is the x of the Avatar
                writer.write(Integer.toString(worldGenerator.getAvatarX()));
                writer.write("\n");
                // Third line is the y of the Avatar
                writer.write(Integer.toString(worldGenerator.getAvatarY()));
                writer.write("\n");
                // Forth line is the x of the Fountain
                writer.write(Integer.toString(worldGenerator.getFountainX()));
                writer.write("\n");
                // Fifth line is the y of the Fountain
                writer.write(Integer.toString(worldGenerator.getFountainY()));
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.exit(0); // Close the window
    }

    private void readGame() {
        try {
            FileReader reader = new FileReader("./save.txt");
            BufferedReader bufferedReader = new BufferedReader(reader);
            String line;
            // Save the content in the file for further processed
            ArrayList<String> file = new ArrayList<>(5);
            while ((line = bufferedReader.readLine()) != null) {
                file.add(line);
            }
            reader.close();
            // Assign the content to seed and position of avatar
            seed = Long.parseLong(file.get(0));
            Position avatarPos = new Position(Integer.parseInt(file.get(1)),
                    Integer.parseInt(file.get(2)));
            Position fountainPos = new Position(Integer.parseInt(file.get(3)),
                    Integer.parseInt(file.get(4)));
            // Create world with previous saving
            ter.initialize(WIDTH, HEIGHT);
            Random random = new Random(seed);
            worldGenerator = new WorldGenerator(world, random, avatarPos, fountainPos);
            ter.renderFrame(world);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void moveAvatar(char current) {
        if (current == 'w') {
            worldGenerator.moveAvatarUp();
            ter.renderFrame(world);
            if (worldGenerator.gameOver()) {
                gameOver();
                worldGenerator = null;
                seedBuilder = new StringBuilder();
            }
        }
        if (current == 's') {
            worldGenerator.moveAvatarDown();
            ter.renderFrame(world);
            if (worldGenerator.gameOver()) {
                gameOver();
                worldGenerator = null;
                seedBuilder = new StringBuilder();
            }
        }
        if (current == 'd') {
            worldGenerator.moveAvatarRight();
            ter.renderFrame(world);
            if (worldGenerator.gameOver()) {
                gameOver();
                worldGenerator = null;
                seedBuilder = new StringBuilder();
            }
        }
        if (current == 'a') {
            worldGenerator.moveAvatarLeft();
            ter.renderFrame(world);
            if (worldGenerator.gameOver()) {
                gameOver();
                worldGenerator = null;
                seedBuilder = new StringBuilder();
            }
        }
    }


    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public void interactWithKeyboard() {
        KeyboardInputSource keyboardListener = new KeyboardInputSource();
        Menu menu = new Menu(WIDTH, HEIGHT);
        menu.createMenu();

        while (keyboardListener.hasNext()) {

            // The part that I move from the end to here.
            if (worldGenerator != null) {
                while (!StdDraw.hasNextKeyTyped()) {
                    ter.renderFrame(world);
                    showTileInfo();
                }
            }
            char current = keyboardListener.getCurrentKey();
            if (worldGenerator != null) {
                if (current == 'w' || current == 'a' || current == 's' || current == 'd') {
                    moveAvatar(current);
                }
            }


            if (current == 'n') {
                seedBuilder = new StringBuilder();
                menu.inputSeed("");
                while (true) {
                    char currentSeed = keyboardListener.getCurrentKey();
                    if (currentSeed == 's') {
                        menu.clearMenu();
                        renderNewWorld();
                        break;
                    }
                    seedBuilder.append(currentSeed);
                    menu.inputSeed(seedBuilder.toString());
                }
            }

            if (current == ':') {
                char quitListener = keyboardListener.getCurrentKey();
                if (quitListener == 'q') {
                    saveGame();
                }
            }

            if (current == 'q') {
                saveGame();
            }

            if (current == 'c') {
                menu.chooseAvatar();
                boolean isChoosing = true;
                char choice = keyboardListener.getCurrentKey();
                while (isChoosing) {
                    if (choice == '1') {
                        avatarType = Tileset.AVATAR;
                        isChoosing = false;
                        menu.mainMenu();
                    }
                    if (choice == '2') {
                        avatarType = Tileset.MOUNTAIN;
                        isChoosing = false;
                        menu.mainMenu();
                    }
                    if (choice == '3') {
                        avatarType = Tileset.TREE;
                        isChoosing = false;
                        menu.mainMenu();
                    }
                }
            }

            if (current == 'l') {
                menu.clearMenu();
                readGame();
            }
        }
    }


    /**
     * Method used for autograding and testing your code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The engine should
     * behave exactly as if the user typed these characters into the engine using
     * interactWithKeyboard.
     *
     * Recall that strings ending in ":q" should cause the game to quite save. For example,
     * if we do interactWithInputString("n123sss:q"), we expect the game to run the first
     * 7 commands (n123sss) and then quit and save. If we then do
     * interactWithInputString("l"), we should be back in the exact same state.
     *
     * In other words, both of these calls:
     *   - interactWithInputString("n123sss:q")
     *   - interactWithInputString("lww")
     *
     * should yield the exact same world state as:
     *   - interactWithInputString("n123sssww")
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] interactWithInputString(String input) {
        StringInputSource stringDevice = new StringInputSource(input.toLowerCase());
        TETile[][] finalWorldFrame = null;

        while (stringDevice.hasNext()) {
            char current = stringDevice.getCurrentKey();
            seedBuilder = new StringBuilder();
            if (current == 'n') {
                while (true) {
                    char currentSeed = stringDevice.getCurrentKey();
                    if (currentSeed != 's') {
                        seedBuilder.append(currentSeed);
                    } else {
                        break;
                    }
                }
                seed = Long.parseLong(seedBuilder.toString());
//                ter.initialize(WIDTH, HEIGHT);
                Random random = new Random(seed);
                worldGenerator = new WorldGenerator(world, random, Tileset.AVATAR);
                finalWorldFrame = world;
//                ter.renderFrame(world);
            }

            if (current == ':') {
                char nextChar = stringDevice.getCurrentKey();
                if (nextChar == 'q') {
                    saveGame();
                }
            }

            if (current == 'l') {
                try {
                    FileReader reader = new FileReader("./save.txt");
                    BufferedReader bufferedReader = new BufferedReader(reader);
                    String line;
                    ArrayList<String> file = new ArrayList<>(5);
                    while ((line = bufferedReader.readLine()) != null) {
                        file.add(line);
                    }
                    reader.close();
                    seed = Long.parseLong(file.get(0));
                    Position avatarPos = new Position(Integer.parseInt(file.get(1)),
                            Integer.parseInt(file.get(2)));
                    Position fountainPos = new Position(Integer.parseInt(file.get(3)),
                            Integer.parseInt(file.get(4)));
                    Random random = new Random(seed);
                    worldGenerator = new WorldGenerator(world, random, avatarPos, fountainPos);
                    finalWorldFrame = world;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (current == 'w') {
                worldGenerator.moveAvatarUp();
            }
            if (current == 's') {
                worldGenerator.moveAvatarDown();
            }
            if (current == 'd') {
                worldGenerator.moveAvatarRight();
            }
            if (current == 'a') {
                worldGenerator.moveAvatarLeft();
            }
        }
        return finalWorldFrame;
    }
}
