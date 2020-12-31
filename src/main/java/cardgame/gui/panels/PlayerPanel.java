package cardgame.gui.panels;

import cardgame.GUILog;
import cardgame.Player;
import cardgame.cards.Card;
import cardgame.gui.GuiManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;

public class PlayerPanel extends JPanel {

    Player p;
    private Dimension preferredSize;
    private JPanel deckPanel;
    private JPanel fieldPanel;
    public static final Dimension PLAYER_PANEL_DIMS = new Dimension((int) (CardPanel.CARD_DIMS.getWidth() * 6 + 200), (int) (CardPanel.CARD_DIMS.getHeight() * 2 + 80));
    boolean p1;
    CardPanel[] cards;

    public PlayerPanel(Player p, boolean p1) {
        this.p = p;
        this.p1 = p1;

        preferredSize = PLAYER_PANEL_DIMS;

        setLayout(new BorderLayout());

        deckPanel = new JPanel(null);
        deckPanel.setPreferredSize(new Dimension(CardPanel.CARD_DIMS.width + 70, preferredSize.height));
        add(deckPanel, BorderLayout.EAST);

        fieldPanel = new JPanel(null);
        fieldPanel.setPreferredSize(new Dimension(160 + (CardPanel.CARD_DIMS.width * 5), preferredSize.height));
        add(fieldPanel, BorderLayout.WEST);

        drawPanel();
    }

    public void setP(Player p) {
        this.p = p;
        setLayout(new BorderLayout());
        deckPanel.removeAll();
        drawPanel();

    }

    public void drawPanel() {
        fieldPanel.setBackground(p.getColor());
        CardPanel discard = new CardPanel(p.getDiscard().look(), p.getDiscard().size(), false, 20, 30);
        CardPanel deck = new CardPanel.CardBackPanel(p.getDeck().size(), 20, 30 + 20 + CardPanel.CARD_DIMS.height);
        if (p1) {
            deck.addMouseListener(new MouseAdapter() {
                // listener to change
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
            // listener to change
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

        if (cards == null) {
            cards = new CardPanel[Player.FIELD_LEN * 2];
            int x = 50;
            int y = 30;
            for (int i = 0; i < Player.FIELD_LEN * 2; i++) {
                if (i == Player.FIELD_LEN) {
                    x = 50;
                    y += CardPanel.CARD_DIMS.height + 20;
                }

                cards[i] = new CardPanel(null, 0, false, x, y);
                fieldPanel.add(cards[i]);
                x += CardPanel.CARD_DIMS.width + 15;
                GUILog.println(x + ", " + y);
            }
        }
        for (int i = 0; i < cards.length; i++) {
            Card card;
            if (i < Player.FIELD_LEN) {
                card = p.getField().getMonsters()[i];
            } else {
                card = p.getField().getBottomRow()[i - Player.FIELD_LEN];
            }
            if (cards[i].getMouseListeners().length > 0) {
                cards[i].removeMouseListener(cards[i].getMouseListeners()[0]);
            }
            cards[i].setCard(card);
            if (card != null) {
                cards[i].changeCardNum(1);
                cards[i].addMouseListener(new MouseAdapter() {
                    // listener to change
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (e.getButton() == MouseEvent.BUTTON1) {
                            for (int x = 0; x < p.getField().getMonsters().length; x++) {
                                if (p.getField().getMonsters()[x] == card) {
                                    p.getField().getMonsters()[x] = null;
                                }
                            }
                            drawPanel();
                        } else if (e.getButton() == MouseEvent.BUTTON3) {
                            GuiManager.getInstance().giveInfo(card);
                        }
                    }
                });
            }
        }


        GuiManager.getInstance().labelFix(this);
        GuiManager.getInstance().labelFix(fieldPanel);

        repaint();
    }

    @Override
    public Dimension getPreferredSize() {
        return preferredSize;
    }


    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        if (!p1) {
            int x = getWidth() / 2;
            int y = getHeight() / 2;
            g2.rotate(Math.toRadians(180.0), x, y);
        }
        super.paintComponent(g2);
    }
}
