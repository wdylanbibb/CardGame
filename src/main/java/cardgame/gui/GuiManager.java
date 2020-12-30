package cardgame.gui;

import cardgame.JsonAccessor;
import cardgame.Player;
import cardgame.UtilMaps;
import cardgame.cards.Card;
import cardgame.gui.frames.BoardFrame;
import cardgame.gui.frames.HandFrame;
import cardgame.gui.frames.InfoFrame;

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

    public static final DefaultMetalTheme WINDOW_THEME = new DefaultMetalTheme(){

    };

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
        boardFrame = new BoardFrame(p1, p2);
        p1.draw();
        new HandFrame(p1, 1);
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
}
