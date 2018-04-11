import processing.core.PApplet;
import processing.core.PImage;

import java.util.HashMap;

public class ResourceManager implements Constants {
    private PApplet pApplet;
    private static HashMap<String, PImage> imageLibrary = new HashMap<>();

    ResourceManager(PApplet pApplet) {
        this.pApplet = pApplet;

        allocImage("BLOCK_VALUE_0", "images/0.png", BLOCK_SIZE, BLOCK_SIZE);
        allocImage("BLOCK_VALUE_1", "images/1.png", BLOCK_SIZE, BLOCK_SIZE);
        allocImage("BLOCK_VALUE_2", "images/2.png", BLOCK_SIZE, BLOCK_SIZE);
        allocImage("BLOCK_VALUE_3", "images/3.png", BLOCK_SIZE, BLOCK_SIZE);
        allocImage("BLOCK_VALUE_4", "images/4.png", BLOCK_SIZE, BLOCK_SIZE);
        allocImage("BLOCK_VALUE_5", "images/5.png", BLOCK_SIZE, BLOCK_SIZE);
        allocImage("BLOCK_VALUE_6", "images/6.png", BLOCK_SIZE, BLOCK_SIZE);
        allocImage("BLOCK_VALUE_7", "images/7.png", BLOCK_SIZE, BLOCK_SIZE);
        allocImage("BLOCK_VALUE_8", "images/8.png", BLOCK_SIZE, BLOCK_SIZE);

        allocImage("BLOCK_MINE", "images/bomb.png", BLOCK_SIZE, BLOCK_SIZE);
        allocImage("BLOCK_INVISIBLE", "images/facingDown.png", BLOCK_SIZE, BLOCK_SIZE);
        allocImage("BLOCK_FLAGGED", "images/flagged.png", BLOCK_SIZE, BLOCK_SIZE);

        allocImage("SAVE", "images/save.png", BLOCK_SIZE, BLOCK_SIZE);
        allocImage("LOAD", "images/load.png", BLOCK_SIZE, BLOCK_SIZE);

        allocImage("WIN", "images/win.png", WIN_ICON_W, WIN_ICON_H);
        allocImage("LOSE", "images/gameover.png", LOSE_ICON_W, LOSE_ICON_H);

        allocImage("MINE", "images/mine.png", MINE_ICON_SIZE, MINE_ICON_SIZE);
        allocImage("?", "images/question.png", MINE_ICON_SIZE, MINE_ICON_SIZE);

        allocImage("SMILE_NORMAL", "images/emojis.png", 0, 0, 200, 188, SMILE_SIZE);
        allocImage("SMILE_WIN", "images/emojis.png", 1, 0, 200, 188, SMILE_SIZE);
        allocImage("SMILE_FLAG", "images/emojis.png", 1, 2, 200, 188, SMILE_SIZE);
        allocImage("SMILE_LOSE", "images/emojis.png", 1, 1, 200, 188, SMILE_SIZE);

        allocImage("0", "images/timer.png", 0, 0, 20, 25, 100, 50);
        allocImage("1", "images/timer.png", 1, 0, 20, 25, 100, 50);
        allocImage("2", "images/timer.png", 2, 0, 20, 25, 100, 50);
        allocImage("3", "images/timer.png", 3, 0, 20, 25, 100, 50);
        allocImage("4", "images/timer.png", 4, 0, 20, 25, 100, 50);
        allocImage("5", "images/timer.png", 0, 1, 20, 25, 100, 50);
        allocImage("6", "images/timer.png", 1, 1, 20, 25, 100, 50);
        allocImage("7", "images/timer.png", 2, 1, 20, 25, 100, 50);
        allocImage("8", "images/timer.png", 3, 1, 20, 25, 100, 50);
        allocImage("9", "images/timer.png", 4, 1, 20, 25, 100, 50);

    }


    private void allocImage(String id, String fileName, int w, int h) {
        PImage pImage = pApplet.loadImage(fileName);
        pImage.resize(w, h);
        imageLibrary.put(id, pImage);
    }


    private void allocImage(String id, String fileName, int posX, int posY, int w, int h, int resizeX, int resizeY) {
        PImage pImage = pApplet.loadImage(fileName);
        pImage.resize(resizeX, resizeY);
        pImage = pImage.get(w * posX, h * posY, w, h);
        imageLibrary.put(id, pImage);
    }


    private void allocImage(String id, String fileName, int posX, int posY, int w, int h, int size) {
        PImage pImage = pApplet.loadImage(fileName).get(w * posX, h * posY, w, h);
        pImage.resize(size, size);
        imageLibrary.put(id, pImage);
    }

    public PImage getPImage(String id) {
        return imageLibrary.get(id);
    }

}
