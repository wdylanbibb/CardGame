package cardgame.gui.frames;

import cardgame.GUILog;
import cardgame.Player;
import cardgame.cards.Card;
import cardgame.gui.GuiManager;
import cardgame.gui.panels.CardPanel;
import org.apache.commons.codec.binary.Base64;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class HandFrame extends JFrame {

    private final Player player;
    public static final int CARDS_SHOWN = Player.STARTING_HAND;
    public static Dimension preferredSize = new Dimension((CardPanel.CARD_DIMS.width + 75) * CARDS_SHOWN, CardPanel.CARD_DIMS.height + 60);
    int startIndex;
    JPanel content;
    JButton left;
    JButton right;
    CardPanel[] visCards;

    public HandFrame(Player player, int playerNum) throws HeadlessException {
        super(String.format("Player %d's hand", playerNum));
        setBounds(GuiManager.getInstance().getHandFrameLoc());
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

        redrawHand();

        GuiManager.getInstance().labelFix(content);
        GuiManager.getInstance().initWindowTheme(this);
        pack();
        setVisible(true);
        repaint();

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                if (e.getComponent() == HandFrame.this) {
                    String info = e.paramString().split("\\(")[1].split("\\)")[0];
                    int x = Integer.parseInt(info.split(",")[0]);
                    int y = Integer.parseInt(info.split(",")[1].split(" ")[0]);
                    int w = Integer.parseInt(info.split(" ")[1].split("x")[0]);
                    int h = Integer.parseInt(info.split(" ")[1].split("x")[1]);
                    GuiManager.getInstance().setHandFrameLoc(x, y, w, h);
                }
            }

            @Override
            public void componentMoved(ComponentEvent e) {
                if (e.getComponent() == HandFrame.this) {
                    String info = e.paramString().split("\\(")[1].split("\\)")[0];
                    int x = Integer.parseInt(info.split(",")[0]);
                    int y = Integer.parseInt(info.split(",")[1].split(" ")[0]);
                    int w = Integer.parseInt(info.split(" ")[1].split("x")[0]);
                    int h = Integer.parseInt(info.split(" ")[1].split("x")[1]);
                    GuiManager.getInstance().setHandFrameLoc(x, y, w, h);
                }
            }
        });
    }

    public void redrawHand() {
        content.removeAll();
        revalidate();
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
        int cardSpaceWidth = subList.size() * CardPanel.CARD_DIMS.width + ((subList.size() + 1) * 25);
        int[] x = {(preferredSize.width - cardSpaceWidth) / 2 + 25};
        int y = (preferredSize.height - CardPanel.CARD_DIMS.height) / 2;
        if (player.getHand().size() > subList.size()) {
            left.setBounds((preferredSize.width - cardSpaceWidth) / 2 - 35, preferredSize.height / 2 - (50 / 2), 35, 50);
            content.add(left);
            right.setBounds((preferredSize.width - cardSpaceWidth) / 2 + cardSpaceWidth, preferredSize.height / 2 - 25, 35, 50);
            content.add(right);
        }
        for (int i = 0, subListSize = subList.size(); i < subListSize; i++) {
            Card card = subList.get(i);
            CardPanel panel = new CardPanel(card, card == null ? 0 : 1, false, x[0], y);
            visCards[i] = panel;
            content.add(panel);
            if (card != null) {
                panel.addMouseListener(new MouseAdapter() {
                    // listener to change
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (e.getButton() == MouseEvent.BUTTON1) {
                            GUILog.println("play " + card.getName());
                            player.getHand().remove(card);
                            redrawHand();
                        } else if (e.getButton() == MouseEvent.BUTTON3) {
                            GuiManager.getInstance().giveInfo(card);
                        }
                    }
                });
            }
            x[0] += CardPanel.CARD_DIMS.width + 25;
        }
    }
}
