package cardgame.gui.frames;

import cardgame.GUILog;
import cardgame.Player;
import cardgame.gui.GuiManager;
import cardgame.gui.panels.PlayerPanel;

import javax.swing.*;
import java.awt.*;

public class BoardFrame extends JFrame {

    private final Player p1;
    private final Player p2;
    private PlayerPanel currPanel;
    private PlayerPanel nonCurrPanel;
    JPanel contentPanel;
    JPanel playerPanel;
    private Player currPlayer;
    private Player nonCurrPlayer;
    private int currPlayerNum;


    public BoardFrame(Player p1, Player p2) throws HeadlessException {

        super("Card Game");

        this.p1 = p1;
        this.p2 = p2;
        currPlayer = p1;
        nonCurrPlayer = p2;
        currPlayerNum = 1;

        setResizable(false);

        GuiManager.getInstance().initWindowTheme(this);
        contentPanel = new JPanel(new BorderLayout());

        playerPanel = new JPanel(new BorderLayout());

        JButton endBtn = new JButton("END TURN");
        endBtn.setFocusable(false);
        endBtn.addActionListener(e -> {
            // listener to change
            GUILog.println("end");
            GuiManager.getInstance().takeTurn();
        });
        endBtn.setPreferredSize(new Dimension(100, 25));


        currPanel = new PlayerPanel(currPlayer, true);
        nonCurrPanel = new PlayerPanel(nonCurrPlayer, false);

        playerPanel.add(currPanel, BorderLayout.SOUTH);
        playerPanel.add(nonCurrPanel, BorderLayout.NORTH);
        playerPanel.setBackground(Color.BLACK);

        JPanel buttonPanel = new JPanel(new BorderLayout());
        buttonPanel.setBackground(Color.blue);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 15, 30, 15));
        buttonPanel.add(endBtn, BorderLayout.SOUTH);

        contentPanel.add(playerPanel, BorderLayout.WEST);
        contentPanel.add(buttonPanel, BorderLayout.EAST);
        contentPanel.setPreferredSize(new Dimension((int) PlayerPanel.PLAYER_PANEL_DIMS.getWidth() + 130, (int) PlayerPanel.PLAYER_PANEL_DIMS.getHeight() * 2));
        contentPanel.setBounds(0, 0, contentPanel.getPreferredSize().width, contentPanel.getPreferredSize().height);
        setPreferredSize(new Dimension(contentPanel.getPreferredSize().width + 15, contentPanel.getPreferredSize().height + 40));
        add(contentPanel);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
        pack();
    }

    public void takeTurn() {
        Player toCurrPlayer = nonCurrPlayer;
        nonCurrPlayer = currPlayer;
        currPlayer = toCurrPlayer;
        currPlayerNum = currPlayer == p1 ? 1 : 2;
        currPanel.setP(currPlayer);
        nonCurrPanel.setP(nonCurrPlayer);
    }

    public Player getCurrPlayer() {
        return currPlayer;
    }

    public int getCurrPlayerNum() {
        return currPlayerNum;
    }
}
