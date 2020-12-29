package cardgame.gui.panels;

import cardgame.Player;
import cardgame.gui.GuiManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

public class PlayerPanel extends JPanel {

    Player p;
    private Dimension preferredSize;
    private JPanel deckPanel;
    public static final Dimension PLAYER_PANEL_DIMS = new Dimension((int) (CardPanel.CARD_DIMS.getWidth() * 6 + 50), (int) (CardPanel.CARD_DIMS.getHeight() * 2 + 35));

    public PlayerPanel(Player p, boolean p1) {
        this.p = p;
        setLayout(new BorderLayout());
        preferredSize = PLAYER_PANEL_DIMS;
        deckPanel = new JPanel();
        deckPanel.setLayout(new BorderLayout());
        deckPanel.setPreferredSize(new Dimension((int) (CardPanel.CARD_DIMS.getWidth() + 40), getHeight()));
        add(deckPanel, p1 ? BorderLayout.EAST : BorderLayout.WEST);

        CardPanel discard = new CardPanel(p.getDiscard().look(1).get(0), p.getDiscard().size(), false, 20, p1 ? 15 : 200);
        CardPanel deck = new CardPanel.CardBackPanel(p.getDeck().size(), 20, p1 ? 200 : 15);
        if (p1) {
            deck.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getButton() == MouseEvent.BUTTON1) {
                        System.out.println("draw");
                        deck.changeCardNum(deck.getCardNum() - 1);
                        discard.changeCardNum(discard.getCardNum() + 1);
                    }
                }
            });
        }

        deckPanel.add(discard);
        deckPanel.add(deck);
        GuiManager.getInstance().labelFix(deckPanel);


    }

    @Override
    public Dimension getPreferredSize() {
        return preferredSize;
    }
}
