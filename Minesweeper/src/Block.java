import com.google.gson.JsonObject;
import processing.core.PApplet;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Block implements Constants, Serializable {
    private transient PApplet pApplet;
    private transient ResourceManager resourceManager;
    private int value;
    private int state;
    private int posX;
    private int posY;
    private transient List<Block> connections = new ArrayList<>();

    private transient static final String[] blockImageNames = {
            "BLOCK_VALUE_0",
            "BLOCK_VALUE_1",
            "BLOCK_VALUE_2",
            "BLOCK_VALUE_3",
            "BLOCK_VALUE_4",
            "BLOCK_VALUE_5",
            "BLOCK_VALUE_6",
            "BLOCK_VALUE_7",
            "BLOCK_VALUE_8",
            "BLOCK_MINE"
    };


    Block(PApplet pApplet, int posX, int posY, int state, ResourceManager resourceManager) {
        this.pApplet = pApplet;
        this.state = state;
        this.posX = posX;
        this.posY = posY;
        this.resourceManager = resourceManager;
    }

    public void setPApplet(PApplet pApplet) {
        this.pApplet = pApplet;
    }

    public void setResourceManager(ResourceManager resourceManager) {
        this.resourceManager = resourceManager;
    }

    public void setState(int state) {
        this.state = state;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void increaseValue() {
        this.value++;
    }

    public void setConnections(List<Block> connections) {
        this.connections = connections;
    }

    public int getPosX() {
        return posX;
    }

    public int getValue() {
        return value;
    }

    public int getState() {
        return state;
    }


    public List<Block> getConnections() {
        return connections;
    }

    private int getX() {
        return posX * BLOCK_SIZE + MARGIN;
    }

    private int getY() {
        return posY * BLOCK_SIZE + MARGIN_TOP;
    }

    public void draw() {
        int x = getX();
        int y = getY();

        String imageName = "";

        if (state == BLOCK_STATE_INVISIBLE) {
            imageName = "BLOCK_INVISIBLE";
        } else if (state == BLOCK_STATE_MARKING) {
            imageName = "BLOCK_FLAGGED";
        } else if (state == BLOCK_STATE_VISIBLE) {
            imageName = blockImageNames[value];
        }

        if (!imageName.equals("")) {
            pApplet.image(resourceManager.getPImage(imageName), x, y);
        }

    }

    public boolean isCollision(int mx, int my) {
        int x = getX();
        int y = getY();

        return (mx > x && mx < x + BLOCK_SIZE && my > y && my < y + BLOCK_SIZE);

    }

    public JsonObject toJSONObject() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("posX", posX);
        jsonObject.addProperty("posY", posY);
        jsonObject.addProperty("value", value);
        jsonObject.addProperty("state", state);

        return jsonObject;

    }

}
