package cardgame.gui.frames;

import cardgame.Player;
import cardgame.cardcontainers.Deck;
import cardgame.cardcontainers.Discard;
import cardgame.cardcontainers.Field;
import cardgame.gui.GuiManager;
import cardgame.gui.panels.PlayerPanel;

import javax.swing.*;
import java.awt.*;

public class BoardFrame extends JFrame {

    private final Discard p1Discard;
    private final Discard p2Discard;
    private final Player p1;
    private final Player p2;
    private final Field p1Field;
    private final Field p2Field;
    private final Deck p1Deck;
    private final Deck p2Deck;
    private PlayerPanel p1Panel;
    private PlayerPanel p2Panel;


    public BoardFrame(Player p1, Player p2) throws HeadlessException {

        super("Card Game");

        this.p1 = p1;
        this.p2 = p2;
        this.p1Deck = p1.getDeck();
        this.p2Deck = p2.getDeck();
        this.p1Discard = p1.getDiscard();
        this.p2Discard = p2.getDiscard();
        this.p1Field = p1.getField();
        this.p2Field = p2.getField();

        JPanel contentPanel = new JPanel(new BorderLayout());

        JPanel playerPanel = new JPanel(new BorderLayout());

        JButton endBtn = new JButton("END TURN");
        endBtn.setFocusable(false);
        endBtn.addActionListener(System.out::println);
        endBtn.setPreferredSize(new Dimension(100, 25));


        p1Panel = new PlayerPanel(p1, this, true);
        p1Panel.setBackground(Color.CYAN);
        p2Panel = new PlayerPanel(p2, this, false);
        p2Panel.setBackground(Color.BLUE);

        playerPanel.add(p1Panel, BorderLayout.SOUTH);
        playerPanel.add(p2Panel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new BorderLayout());
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 15, 30, 15));
        buttonPanel.add(endBtn, BorderLayout.SOUTH);

        contentPanel.add(playerPanel, BorderLayout.WEST);
        contentPanel.add(buttonPanel, BorderLayout.EAST);
        contentPanel.setPreferredSize(new Dimension((int) PlayerPanel.PLAYER_PANEL_DIMS.getWidth() + 130, (int) PlayerPanel.PLAYER_PANEL_DIMS.getHeight() * 2));
        contentPanel.setBounds(0, 0, contentPanel.getPreferredSize().width, contentPanel.getPreferredSize().height);
        setPreferredSize(new Dimension(contentPanel.getPreferredSize().width + 15, contentPanel.getPreferredSize().height + 40));
        add(contentPanel);
        add(new JLabel());

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
        pack();
    }
}
