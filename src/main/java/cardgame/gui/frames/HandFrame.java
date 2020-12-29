package cardgame.gui.frames;

import cardgame.Player;
import cardgame.cards.Card;
import cardgame.gui.GuiManager;
import cardgame.gui.panels.CardPanel;
import org.apache.commons.codec.binary.Base64;
import org.checkerframework.checker.units.qual.C;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.FontUIResource;
import javax.swing.plaf.metal.DefaultMetalTheme;
import javax.swing.plaf.metal.MetalLookAndFeel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class HandFrame extends JFrame {

    private final Player player;
    public static final int CARDS_SHOWN = Player.STARTING_HAND;
    Dimension preferredSize = new Dimension((CardPanel.CARD_DIMS.width + 75) * CARDS_SHOWN, CardPanel.CARD_DIMS.height + 60);
    int startIndex;
    JPanel content;
    JButton left;
    JButton right;
    CardPanel[] visCards;

    public HandFrame(Player player, int playerNum) throws HeadlessException {
        super(String.format("Player %d's hand", playerNum));
        this.player = player;
        startIndex = 0;
        visCards = new CardPanel[CARDS_SHOWN];

        setResizable(false);

        content = new JPanel(null);
        content.setPreferredSize(preferredSize);
        content.setBounds(0, 0, preferredSize.width, preferredSize.height);
        content.setBackground(Color.decode("#EDCB80"));
        content.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(content);
        left = new JButton();
        right = new JButton();
        try {
            BufferedImage leftImg = ImageIO.read(new ByteArrayInputStream(Base64.decodeBase64("iVBORw0KGgoAAAANSUhEUgAAAMAAAADACAQAAAD41aSMAAABDUlEQVR4Ae3RCwkCMABF0RdDU6hgPD8LrBhhhpDxYJ4T4d70BQAAAAAAAAAAAAAAAAAAAACAkZEi+WemBd38FtTzW1DPb0Exf3+B/P0F8vcXyN9fIH9/gfz9BfLPvHMIxfyXIL/8yC8/8suP/PKzMP85yC8/8suP/PIj/975X/LLLz/yy4/88iO//KzNfwryy4/88iO//MgvP/LvOuCTa7DAAiywAAsswAILsMACLLAACyzAAguwwAIssAALLMACC7DAAiywAAsswAILsMACLPj3BcdQXHALxQXyVxfIX11wD8UF8lcXyF9d8AjFBfJXF8hfXfAMNUN+AAAAAAAAAAAAAAAAAAAAAAAA+MUXflB1WJeTFwAAAAAASUVORK5CYII=")));
            BufferedImage rightImg = ImageIO.read(new ByteArrayInputStream(Base64.decodeBase64("iVBORw0KGgoAAAANSUhEUgAAAMAAAADACAQAAAD41aSMAAABEklEQVR4Ae3RXa3CABgFwe/6qAtKgrzDj19AQUklkPtwQjojYXcAAAAAAAAAAAAAAAAAAAAAAODX/c11MkXyb7O1Fsh/2/NXF8hfXSB/a4H89z15dYH81QXytxbI/9gzVxfIX10gf2sBy7xmay7g3F6ABRZggQVYYAEWWMB6+AUWYIEFWGABFljAOs/uAiywAAsswAILsMACTgdfYAEWWIAFFmCBBVhgAZd5GyD/V+RHfvmRX37klx/55Ud++ZFffuSXn//nR375kV9+5Jcf+eVHfvmRX37klx/55Ud++Vna+Uk7P2nnJ+38pJ2ftPOTdn7Szk/a+Uk7P2nnJwfPDwAAAAAAAAAAAAAAAAAAAAAAwAc+8Hd9ZIH8bwAAAABJRU5ErkJggg==")));
            left.setIcon(new ImageIcon(leftImg.getScaledInstance(35, 35, 0)));
            left.setFocusable(false);
            left.setPreferredSize(new Dimension(35, 50));
            left.addActionListener(e -> {
                startIndex--;
                redrawHand();
            });
            if (startIndex < 1) {
                left.setEnabled(false);
            }
            right.setIcon(new ImageIcon(rightImg.getScaledInstance(35, 35, 0)));
            right.setFocusable(false);
            right.setPreferredSize(new Dimension(35, 50));
            right.addActionListener(e -> {
                startIndex++;
                redrawHand();
            });
            if (startIndex + CARDS_SHOWN >= player.getHand().size()) {
                right.setEnabled(false);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        content.add(left);

        redrawHand();

        content.add(right);


        GuiManager.getInstance().initWindowTheme(this);
        pack();
        setVisible(true);
    }

    public void redrawHand() {
        Arrays.stream(visCards).forEach(c -> {
            if (Arrays.stream(content.getComponents()).anyMatch(comp -> comp == c))
                content.remove(c);
        });
        repaint();
        left.setEnabled(startIndex >= 1);
        right.setEnabled(startIndex + CARDS_SHOWN < player.getHand().size());
        List<Card> subList;
//        if (player.getHand().size() >= startIndex + CARDS_SHOWN) {
//            subList = player.getHand().subList(startIndex, startIndex + CARDS_SHOWN);
//        } else {
//            subList = player.getHand();
//            while (subList.size() < CARDS_SHOWN) subList.add(null);
//        }
//        for (int i = 0, subListSize = subList.size(); i < subListSize; i++) {
//            Card card = subList.get(i);
//            CardPanel panel = visCards[i];
//            if (panel.getMouseListeners().length > 0) panel.removeMouseListener(panel.getMouseListeners()[0]);
//            if (card != null) panel.setCard(card);
//            else panel.removeCard();
//            if (card != null) {
//                panel.addMouseListener(new MouseAdapter() {
//                    @Override
//                    public void mouseClicked(MouseEvent e) {
//                        if (e.getButton() == MouseEvent.BUTTON1) {
//                            System.out.println("play " + card.getName());
//                            player.getHand().remove(card);
//                            redrawHand();
//                        } else if (e.getButton() == MouseEvent.BUTTON3) {
//                            GuiManager.getInstance().giveInfo(card);
//                        }
//                    }
//                });
//            }
//        }
//    }
        if (player.getHand().size() >= startIndex + CARDS_SHOWN) {
            subList = player.getHand().subList(startIndex, startIndex + CARDS_SHOWN);
        } else if (player.getHand().size() >= CARDS_SHOWN) {
            startIndex = 0;
            subList = player.getHand().subList(startIndex, startIndex + CARDS_SHOWN);
        } else {
            subList = player.getHand();
        }
        int[] x = {0};
        int y = 10;
        for (int i = 0, subListSize = subList.size(); i < subListSize; i++) {
            Card card = subList.get(i);
            CardPanel panel = new CardPanel(card, card == null ? 0 : 1, false, x[0], y);
            visCards[i] = panel;
            content.add(panel);
            if (card != null) {
                panel.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (e.getButton() == MouseEvent.BUTTON1) {
                            System.out.println("play " + card.getName());
                            player.getHand().remove(card);
                            redrawHand();
                        } else if (e.getButton() == MouseEvent.BUTTON3) {
                            GuiManager.getInstance().giveInfo(card);
                        }
                    }
                });
            }
        }
    }
}
