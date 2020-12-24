package cardgame.gui;

import cardgame.JsonAccessor;
import cardgame.Player;
import cardgame.UtilMaps;
import cardgame.gui.frames.BoardFrame;

import java.awt.*;
import java.io.IOException;

public class GuiManager {

    public static final Dimension CARD_DIMS = new Dimension(150, 180);
    public static final Dimension CARD_DIMS_LARGE = new Dimension(400, 480);
    public static final Color transparentColor = new Color(0, 0, 0, 0);

    private static GuiManager INSTANCE;

    public static GuiManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new GuiManager();
        }
        return INSTANCE;
    }

    public static void main(String[] args) throws IOException {
        JsonAccessor.fillMaps();
        getInstance().initGame(new Player(UtilMaps.getInstance().getDeckByName("deck1")), new Player(UtilMaps.getInstance().getDeckByName("deck2")));
    }

    public void initGame(Player p1, Player p2) {
        BoardFrame boardFrame = new BoardFrame(p1, p2);
    }
}
