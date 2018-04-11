import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import processing.core.PApplet;
import processing.event.KeyEvent;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Window extends PApplet implements Constants {
    private static final int WINDOW_WIDTH = MAP_SIZE_X * BLOCK_SIZE + MARGIN * 2;
    private static final int WINDOW_HEIGHT = MAP_SIZE_Y * BLOCK_SIZE + MARGIN + MARGIN_TOP;
    private static final int SAVE_ICON_X = WINDOW_WIDTH / 2 - 90;
    private static final int LOAD_ICON_X = WINDOW_WIDTH / 2 + 50;

    private ResourceManager resourceManager;

    private List<Block> blocks = new ArrayList<>();
    private List<Block> connections = new ArrayList<>();
    private int[] mines;

    private int gameState;
    private int smileState;
    private int keyState;

    private int countMines;
    private int invisibleBlockCount;
    private static long startTime = 0;
    private static int timer = 0;

    private boolean isMousePressed = false;
    private int countFlag;
    private int[] bombIndex;
    private String strMines = "";

    private static final String[] smileImageNames = {
            "SMILE_NORMAL", "SMILE_FLAG", "SMILE_WIN", "SMILE_LOSE"
    };


    @Override
    public void settings() {
        size(WINDOW_WIDTH, WINDOW_HEIGHT);
    }

    @Override
    public void setup() {
        gameState = GAME_STATE_INIT_MINES;
        resourceManager = new ResourceManager(this);
        initBlocks();

    }

    private void initBlocks() {
        smileState = SMILE_NORMAL;
        blocks = new ArrayList<>();
        invisibleBlockCount = MAP_SIZE;
        bombIndex = new int[MAP_SIZE];
        for (int i = 0; i < MAP_SIZE; i++) {
            int posX = i % MAP_SIZE_X;
            int posY = i / MAP_SIZE_X;
            bombIndex[i] = i;
            blocks.add(new Block(this, posX, posY, BLOCK_STATE_INVISIBLE, resourceManager));
        }
    }


    @Override
    public void draw() {
        background(250);

        switch (gameState) {
            case GAME_STATE_INIT_MINES:
                drawMap();
                image(resourceManager.getPImage("MINE"), WINDOW_WIDTH / 2 - MINE_ICON_SIZE, 100);
                image(resourceManager.getPImage("?"), WINDOW_WIDTH / 2, 100);
                fill(0);
                textSize(50);
                text(strMines, WINDOW_WIDTH / 2 - 50, WINDOW_HEIGHT / 2);
                break;
            case GAME_STATE_SETUP_MAP:
                initBlocks();
                setupMap();
                break;
            case GAME_STATE_PLAYING:
                drawMap();
                break;
            case GAME_STATE_LOSE:
                drawMap();
                for (Block b : blocks) {
                    if (b.getValue() == BLOCK_MINE) {
                        b.setState(BLOCK_STATE_VISIBLE);
                    }
                }
                image(resourceManager.getPImage("LOSE"),
                        WINDOW_WIDTH / 2 - LOSE_ICON_W / 2, WINDOW_HEIGHT / 2 - LOSE_ICON_H / 2);
                smileState = SMILE_LOSE;
                break;
            case GAME_STATE_WIN:
                drawMap();
                image(resourceManager.getPImage("WIN"),
                        WINDOW_WIDTH / 2 - WIN_ICON_W / 2, WINDOW_HEIGHT / 2 - WIN_ICON_H / 2);
                smileState = SMILE_WIN;
                break;

        }


    }


    private void drawMap() {
        drawTop();

        if (gameState == GAME_STATE_INIT_MINES) {
            for (Block b : blocks) {
                b.draw();
            }
        } else {

            if (keyPressed) {
                switch (key) {
                    case '1':
                        keyState = KEY_PRESSED_A;
                        smileState = SMILE_FLAG;
                        break;
                    case '2':
                        keyState = KEY_PRESSED_S;
                        smileState = SMILE_FLAG;
                        break;
                    case '3':
                        keyState = KEY_PRESSED_D;
                        smileState = SMILE_FLAG;
                        break;
                }
            } else {
                keyState = KEY_PRESSED_NONE;
                smileState = SMILE_NORMAL;
            }


            boolean isFlagged = false;

            if (countMines == 0 && invisibleBlockCount == 0) {
                gameState = GAME_STATE_WIN;
            }

            if (mousePressed) {
                isMousePressed = true;
            } else if (isMousePressed) {
                countFlag = -1;
                isMousePressed = false;
            }

            for (Block block : blocks) {
                block.draw();

                if (mousePressed && block.isCollision(mouseX, mouseY)
                        && gameState != GAME_STATE_LOSE) {

                    if (keyState == KEY_PRESSED_A) {
                        if (block.getState() == BLOCK_STATE_INVISIBLE) {
                            block.setState(BLOCK_STATE_VISIBLE);
                            if (block.getValue() == BLOCK_MINE) {
                                gameState = GAME_STATE_LOSE;
                            } else if (block.getValue() == 0) {
                                openBlocks(block);
                            }
                        }
                    } else if (keyState == KEY_PRESSED_S) {
                        if (block.getState() == BLOCK_STATE_INVISIBLE) {
                            countFlag++;
                            isFlagged = true;
                        } else if (block.getState() == BLOCK_STATE_MARKING) {
                            countFlag++;
                            isFlagged = false;
                        }

                        if (countFlag == 0) {
                            if (isFlagged) {
                                block.setState(BLOCK_STATE_MARKING);
                                if (block.getValue() == BLOCK_MINE) {
                                    countMines--;
                                }
                            } else {
                                block.setState(BLOCK_STATE_INVISIBLE);
                                if (block.getValue() == BLOCK_MINE) {
                                    countMines++;
                                }
                            }
                        }

                    } else if (keyState == KEY_PRESSED_D) {
                        if (block.getState() == BLOCK_STATE_VISIBLE)
                            openEightBlocks(block);
                    }

                    setInvisibleBlockCount();
                }
            }
        }

    }

    private void setInvisibleBlockCount() {
        int n = 0;
        for (Block b : blocks) {
            if (b.getState() == BLOCK_STATE_INVISIBLE) {
                n++;
            }
        }
        invisibleBlockCount = n;
    }


    private void drawTop() {

        if (gameState == GAME_STATE_PLAYING)
            timer = (int) (System.currentTimeMillis() - startTime) / 1000;

        image(resourceManager.getPImage(timer / 100 + ""), WINDOW_WIDTH - 30 - 60, 30);
        image(resourceManager.getPImage((timer / 10) % 10 + ""), WINDOW_WIDTH - 30 - 40, 30);
        image(resourceManager.getPImage(timer % 10 + ""), WINDOW_WIDTH - 30 - 20, 30);

        image(resourceManager.getPImage(countMines / 100 + ""), 20, 30);
        image(resourceManager.getPImage((countMines / 10) % 10 + ""), 40, 30);
        image(resourceManager.getPImage(countMines % 10 + ""), 60, 30);

        image(resourceManager.getPImage(invisibleBlockCount / 100 + ""), 110, 30);
        image(resourceManager.getPImage((invisibleBlockCount / 10) % 10 + ""), 130, 30);
        image(resourceManager.getPImage(invisibleBlockCount % 10 + ""), 150, 30);


        image(resourceManager.getPImage(smileImageNames[smileState]), WINDOW_WIDTH / 2 - SMILE_SIZE / 2, 20);


        image(resourceManager.getPImage("SAVE"), SAVE_ICON_X, BLOCK_SIZE);
        image(resourceManager.getPImage("LOAD"), LOAD_ICON_X, BLOCK_SIZE);

    }

    @Override
    public void mousePressed() {
        if (mouseX > WINDOW_WIDTH / 2 - SMILE_SIZE / 2
                && mouseX < WINDOW_WIDTH / 2 + SMILE_SIZE / 2
                && mouseY > 20 && mouseY < 20 + SMILE_SIZE) {

            gameState = GAME_STATE_INIT_MINES;

        } else if (mouseX > SAVE_ICON_X && mouseX < SAVE_ICON_X + BLOCK_SIZE
                && mouseY > BLOCK_SIZE && mouseY < BLOCK_SIZE * 2) {

            try {
                saveData();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else if (mouseX > LOAD_ICON_X && mouseX < LOAD_ICON_X + BLOCK_SIZE
                && mouseY > BLOCK_SIZE && mouseY < BLOCK_SIZE * 2) {

            try {
                loadData();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }

    }

    private void saveData() throws IOException {
        Data data = new Data(blocks, mines, gameState, countMines, invisibleBlockCount, timer);
        Type blockListType = new TypeToken<List<Block>>() {
        }.getType();

        Gson gson = new GsonBuilder().registerTypeAdapter(blockListType,
                (JsonSerializer<List<Block>>) (blocks, type, context) -> {
                    JsonArray jsonElements = new JsonArray();
                    for (Block block : blocks) {
                        jsonElements.add(block.toJSONObject());
                    }
                    return jsonElements;
                }).setPrettyPrinting().create();

        String jsonString = gson.toJson(data);
        //System.out.println(jsonString);

        Writer writer = new FileWriter(DATA_PATH);
        writer.write(jsonString);
        writer.close();

    }

    private void loadData() throws FileNotFoundException {

        Gson gson = new Gson();
        JsonReader reader = new JsonReader(new FileReader(DATA_PATH));
        Data data = gson.fromJson(reader, Data.class);
        blocks = data.getBlocks();
        mines = data.getMines();
        countMines = data.getCountMines();
        invisibleBlockCount = data.getInvisibleBlockCount();
        timer = data.getTimer();
        startTime = System.currentTimeMillis() - timer * 1000;

        gameState = GAME_STATE_LOAD;
        setConnections();
        gameState = data.getGameState();


    }


    private void openEightBlocks(Block block) {
        int value = block.getValue();
        int countMarkings = 0;
        for (Block b : block.getConnections()) {
            if (b.getState() == BLOCK_STATE_MARKING)
                countMarkings++;
        }

        if (value == countMarkings) {
            for (Block b : block.getConnections()) {
                if (b.getState() != BLOCK_STATE_MARKING) {
                    if (b.getValue() == BLOCK_MINE) {
                        gameState = GAME_STATE_LOSE;
                    }
                    b.setState(BLOCK_STATE_VISIBLE);
                }
            }
        }

    }

    private void openBlocks(Block block) {

        for (Block b : block.getConnections()) {
            if (b.getState() == BLOCK_STATE_INVISIBLE) {
                b.setState(BLOCK_STATE_VISIBLE);
                if (b.getValue() == 0) {
                    openBlocks(b);
                }
            }
        }

    }


    private void setupMines(int n) {

        for (int i = 0; i < MAP_SIZE * 2; i++) {
            int index1 = (int) (Math.random() * MAP_SIZE);
            int index2 = (int) (Math.random() * MAP_SIZE);
            int temp = bombIndex[index1];
            bombIndex[index1] = bombIndex[index2];
            bombIndex[index2] = temp;
        }


        mines = new int[n];
        for (int i = 0; i < n; i++) {
            int shuffledIndex = bombIndex[i];
            mines[i] = shuffledIndex;
            blocks.get(shuffledIndex).setValue(BLOCK_MINE);
        }

    }

    private void setupMap() {
        startTime = System.currentTimeMillis();

        invisibleBlockCount = MAP_SIZE;

        countMines = mines.length;
        setupMines(countMines);

        setConnections();

        for (int mine : mines) {
            for (Block b : blocks.get(mine).getConnections()) {
                if (b.getValue() != BLOCK_MINE) {
                    b.increaseValue();
                }
            }
        }

        for (int i = 0; i < blocks.size(); i++) {
            Block b = blocks.get(i);
            if (i % MAP_SIZE_X == 0)
                System.out.print("\n");
            System.out.print(b.getValue() + "\t");
        }

        gameState = GAME_STATE_PLAYING;

    }

    private void setConnections() {
        for (int i = 0; i < blocks.size(); i++) {
            Block block = blocks.get(i);
            connections = new ArrayList<>();

            if (gameState == GAME_STATE_LOAD) {
                block.setPApplet(this);
                block.setResourceManager(this.resourceManager);
            }

            int posX = block.getPosX();

            if (posX != 0) {
                addConnections(i - 1);
                addConnections(i - MAP_SIZE_X - 1);
                addConnections(i + MAP_SIZE_X - 1);
            }

            if (posX != MAP_SIZE_X - 1) {
                addConnections(i + 1);
                addConnections(i - MAP_SIZE_X + 1);
                addConnections(i + MAP_SIZE_X + 1);

            }
            addConnections(i - MAP_SIZE_X);
            addConnections(i + MAP_SIZE_X);

            block.setConnections(connections);

        }

    }

    private void addConnections(int index) {
        if (index >= 0 && index < MAP_SIZE) {
            connections.add(blocks.get(index));
        }

    }


    @Override
    public void keyPressed(KeyEvent event) {
        char key = event.getKey();
        try {
            if (gameState == GAME_STATE_INIT_MINES) {

                if (key == ENTER) {
                    int n = Integer.parseInt(strMines);
                    if (n >= 0 && n <= MAP_SIZE) {
                        gameState = GAME_STATE_SETUP_MAP;
                        mines = new int[n];
                    }
                    strMines = "";

                } else {
                    strMines += key;
                }
            }

        } catch (NumberFormatException e) {
            e.printStackTrace();
            strMines = "";
        }
    }


}
