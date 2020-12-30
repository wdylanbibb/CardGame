package cardgame.gui;

import cardgame.JsonAccessor;
import cardgame.Player;
import cardgame.UtilMaps;
import cardgame.cards.Card;
import cardgame.gui.frames.BoardFrame;
import cardgame.gui.frames.HandFrame;
import cardgame.gui.frames.InfoFrame;
import cardgame.gui.panels.CardPanel;

import javax.swing.*;
import javax.swing.plaf.metal.DefaultMetalTheme;
import javax.swing.plaf.metal.MetalLookAndFeel;
import java.awt.*;
import java.io.IOException;

public class GuiManager {

    public static final Color transparentColor = new Color(0, 0, 0, 0);
    private InfoFrame infoFrame;
    private BoardFrame boardFrame;
    private HandFrame handFrame;
    private Rectangle infoFrameLoc;

    public static final DefaultMetalTheme WINDOW_THEME = new DefaultMetalTheme(){

    };

    GuiManager() {
        infoFrameLoc = new Rectangle(0, 0, CardPanel.CARD_DIMS_LARGE.width, CardPanel.CARD_DIMS_LARGE.height);
    }

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
        MetalLookAndFeel.setCurrentTheme(WINDOW_THEME);
        try {
            UIManager.setLookAndFeel(new MetalLookAndFeel());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        p1.draw();
        boardFrame = new BoardFrame(p1, p2);
        new HandFrame(p1, 1);
    }

    public void takeTurn() {
        boardFrame.takeTurn();
    }

    public void labelFix(JPanel panel) {
        JLabel temp = new JLabel();
        panel.add(temp);
        Runnable run = () -> panel.remove(temp);
        new Timer(1000, e -> run.run());
    }

    public void giveInfo(Card card) {
        if (infoFrame != null) {
            infoFrame.dispose();
        }
        infoFrame = new InfoFrame(card);
    }

    public void initWindowTheme(JFrame f) {

        f.setUndecorated ( true );
        f.getRootPane().setWindowDecorationStyle
                (
                        JRootPane.INFORMATION_DIALOG
                );
        SwingUtilities.updateComponentTreeUI(f);
    }

    public Rectangle getInfoFrameLoc() {
        return infoFrameLoc;
    }

    public void setInfoFrameLoc(int x, int y, int w, int h) {
        this.infoFrameLoc = new Rectangle(x, y, w, h);
    }
}
