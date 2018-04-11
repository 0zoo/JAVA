import java.io.Serializable;
import java.util.List;

public class Data implements Serializable {
    private List<Block> blocks;
    private int[] mines;
    private int gameState;
    private int countMines;
    private int invisibleBlockCount;
    private int timer;

    Data(List<Block> blocks, int[] mines, int gameState, int countMines, int invisibleBlockCount, int timer) {
        this.blocks = blocks;
        this.mines = mines;
        this.gameState = gameState;
        this.countMines = countMines;
        this.invisibleBlockCount = invisibleBlockCount;
        this.timer = timer;
    }

    public List<Block> getBlocks() {
        return blocks;
    }

    public int[] getMines() {
        return mines;
    }

    public int getGameState() {
        return gameState;
    }

    public int getCountMines() {
        return countMines;
    }

    public int getInvisibleBlockCount() {
        return invisibleBlockCount;
    }

    public int getTimer() {
        return timer;
    }
}
