package cardgame.gui.frames;

import cardgame.Player;
import cardgame.gui.GuiManager;
import cardgame.gui.panels.PlayerPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

public class BoardFrame extends JFrame {

    private final Player p1;
    private final Player p2;
    private PlayerPanel p1Panel;
    private PlayerPanel p2Panel;


    public BoardFrame(Player p1, Player p2) throws HeadlessException {

        super("Card Game");

        this.p1 = p1;
        this.p2 = p2;

        setResizable(false);

        GuiManager.getInstance().initWindowTheme(this);
        JPanel contentPanel = new JPanel(new BorderLayout());

        JPanel playerPanel = new JPanel(new BorderLayout());

        JButton endBtn = new JButton("END TURN");
        endBtn.setFocusable(false);
        endBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    System.out.println("end");
                }
            }
        });
        endBtn.setPreferredSize(new Dimension(100, 25));


        p1Panel = new PlayerPanel(p1, true);
        p1Panel.setBackground(Color.magenta);
        p2Panel = new PlayerPanel(p2, false);
        p2Panel.setBackground(Color.green);

        playerPanel.add(p1Panel, BorderLayout.SOUTH);
        playerPanel.add(p2Panel, BorderLayout.NORTH);

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
}
