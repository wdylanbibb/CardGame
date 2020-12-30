package cardgame.gui.panels;

import cardgame.GUILog;
import cardgame.Player;
import cardgame.cards.Card;
import cardgame.gui.GuiManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PlayerPanel extends JPanel {

    Player p;
    private Dimension preferredSize;
    private JPanel deckPanel;
    public static final Dimension PLAYER_PANEL_DIMS = new Dimension((int) (CardPanel.CARD_DIMS.getWidth() * 6 + 50), (int) (CardPanel.CARD_DIMS.getHeight() * 2 + 35));
    boolean p1;

    public PlayerPanel(Player p, boolean p1) {
        this.p = p;
        this.p1 = p1;

        preferredSize = PLAYER_PANEL_DIMS;

        setBackground(p.getColor());
        setLayout(new BorderLayout());

        deckPanel = new JPanel();
        deckPanel.setLayout(new BorderLayout());
        deckPanel.setPreferredSize(new Dimension((int) (CardPanel.CARD_DIMS.getWidth() + 40), getHeight()));
        add(deckPanel, p1 ? BorderLayout.EAST : BorderLayout.WEST);

        draw();
    }

    public void setP(Player p) {
        this.p = p;
        setBackground(p.getColor());
        setLayout(new BorderLayout());
        deckPanel.removeAll();
        draw();

    }

    public void draw() {
        CardPanel discard = new CardPanel(p.getDiscard().look(), p.getDiscard().size(), false, 20, p1 ? 15 : 200);
        CardPanel deck = new CardPanel.CardBackPanel(p.getDeck().size(), 20, p1 ? 200 : 15);
        if (p1) {
            deck.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getButton() == MouseEvent.BUTTON1) {
                        deck.changeCardNum(deck.getCardNum() - 1);
                        GUILog.println("draw");
                        p.getDiscard().add(new Card("null", 1, "hi"));
                        discard.setCard(p.getDiscard().look());
                        discard.changeCardNum(discard.getCardNum() + 1);
                    }
                }
            });
        }

        discard.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3) {
                    if (discard.getCardNum() > 0) GuiManager.getInstance().giveInfo(p.getDiscard().look());
                }
            }
        });

        deckPanel.add(discard);
        deckPanel.add(deck);
        GuiManager.getInstance().labelFix(deckPanel);
        repaint();
    }

    @Override
    public Dimension getPreferredSize() {
        return preferredSize;
    }
}
