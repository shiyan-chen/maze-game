package byow.Core;

import edu.princeton.cs.introcs.StdDraw;
import java.awt.*;

public class Menu {
    private int width;
    private int height;
    private Font fontBig = new Font("Monaco", Font.BOLD, 30);
    private Font fontSmall = new Font("Monaco", Font.BOLD, 16);

    public Menu(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void createMenu() {
        StdDraw.setCanvasSize(this.width * 16, this.height * 16);
        mainMenu();
    }

    public void mainMenu() {
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.PINK);
        StdDraw.setXscale(0, this.width);
        StdDraw.setYscale(0, this.height);
        StdDraw.enableDoubleBuffering();

        StdDraw.setFont(fontBig);
        StdDraw.text(width / 2, (height / 3) * 2, "Find the Fountain to Survive");

        StdDraw.setFont(fontSmall);
        StdDraw.text(width / 2, (height / 9) * 5, "New Game(N)");
        StdDraw.text(width / 2, (height / 9) * 4, "Load Game(L)");
        StdDraw.text(width / 2, (height / 9) * 3, "Choose Avatar(C)");
        StdDraw.text(width / 2, (height / 9) * 2, "Quit(Q)");

        StdDraw.show();
    }

    public void inputSeed(String s) {
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.PINK);
        StdDraw.setFont(fontSmall);
        StdDraw.text(width / 2, height / 2, s);
        StdDraw.line(0, height - 2, width, height - 2);
        StdDraw.text(width / 2, height - 1, "Please input the seed, and press 's' while done");
        StdDraw.show();
    }

    public void chooseAvatar() {
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.PINK);
        StdDraw.setFont(fontSmall);
        StdDraw.text(width / 2, (height / 9) * 5, "@ (1)");
        StdDraw.text(width / 2, (height / 9) * 4, "▲ (2)");
        StdDraw.text(width / 2, (height / 9) * 3, "♠ (3)");
        StdDraw.line(0, height - 2, width, height - 2);
        StdDraw.text(width / 2, height - 1,
                "Choose the avatar you like by typing the responding key");
        StdDraw.show();
    }

    public void clearMenu() {
        StdDraw.clear(Color.BLACK);
    }




//    StdDraw.clear(Color.BLACK);
//        StdDraw.setPenColor(Color.PINK);
//    Font fontLarge = new Font("Monaco", Font.CENTER_BASELINE, 30);
//        StdDraw.setFont(fontLarge);
//        StdDraw.text( width / 2, height / 2, s);
//
//        if (!gameOver) {
//        Font fontSmall = new Font("Monaco", Font.BOLD, 20);
//        StdDraw.setFont(fontSmall);
//        StdDraw.line(0, height - 2, width, height - 2);
//        StdDraw.textLeft(0, height - 1, "Round: " + round);
//        if (playerTurn) {
//            StdDraw.text(width / 2, height - 1, "Please Type");
//        } else {
//            StdDraw.text(width / 2, height - 1, "Please Watch");
//        }
//        int randomEncouragement = RandomUtils.uniform(rand, ENCOURAGEMENT.length);
//        StdDraw.textRight(width, height - 1, ENCOURAGEMENT[randomEncouragement]);
//    }



}
