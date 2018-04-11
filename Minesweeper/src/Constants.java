public interface Constants {
    String DATA_PATH = "data/data.json";

    int GAME_STATE_INIT_MINES = 1;
    int GAME_STATE_SETUP_MAP = 2;
    int GAME_STATE_PLAYING = 3;
    int GAME_STATE_WIN = 4;
    int GAME_STATE_LOSE = 5;
    int GAME_STATE_LOAD = 6;


    int BLOCK_STATE_INVISIBLE = 0;
    int BLOCK_STATE_VISIBLE = 1;
    int BLOCK_STATE_MARKING = 2;

    int BLOCK_MINE = 9;

    int SMILE_NORMAL = 0;
    int SMILE_FLAG = 1;
    int SMILE_WIN = 2;
    int SMILE_LOSE = 3;

    int KEY_PRESSED_NONE = 0;
    int KEY_PRESSED_A = 1;
    int KEY_PRESSED_S = 2;
    int KEY_PRESSED_D = 3;


    int MAP_SIZE_X = 30;
    int MAP_SIZE_Y = 16;
    int MAP_SIZE = MAP_SIZE_X * MAP_SIZE_Y;

    int MARGIN_TOP = 80;
    int MARGIN = 10;
    int BLOCK_SIZE = 30;
    int SMILE_SIZE = 50;
    int WIN_ICON_W = 200;
    int WIN_ICON_H = 300;
    int LOSE_ICON_W = 300;
    int LOSE_ICON_H = 180;

    int MINE_ICON_SIZE = 100;


}
