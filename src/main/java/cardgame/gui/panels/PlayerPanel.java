package cardgame.gui.panels;

import cardgame.Player;
import cardgame.gui.GuiManager;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class PlayerPanel extends JPanel {

    Player p;
    private Dimension preferredSize;
    private JPanel deckPanel;
    public static final Dimension PLAYER_PANEL_DIMS = new Dimension((int) (GuiManager.CARD_DIMS.getWidth() * 6 + 50), (int) (GuiManager.CARD_DIMS.getHeight() * 2 + 35));

    public PlayerPanel(Player p, Frame f, boolean p1) {
        this.p = p;
        setLayout(new BorderLayout());
        preferredSize = PLAYER_PANEL_DIMS;
        deckPanel = new JPanel();
        deckPanel.setLayout(new BorderLayout());
        deckPanel.setPreferredSize(new Dimension((int) (GuiManager.CARD_DIMS.getWidth() + 20), getHeight()));
        add(deckPanel, p1 ? BorderLayout.EAST : BorderLayout.WEST);
    }

    @Override
    public Dimension getPreferredSize() {
        return preferredSize;
    }

    public void addToDeckPanel(Component component, Object constraints) {
        deckPanel.add(component, constraints);
    }

    public void addToDeckPanel(JComponent component, Object constraints, Border border) {
        component.setBorder(border);
        addToDeckPanel(component, constraints);
    }
}
