package cardgame.gui.panels;

import cardgame.JsonAccessor;
import cardgame.cards.Card;
import cardgame.cards.cardtypes.Monster.Monster;
import cardgame.gui.GuiManager;
import org.apache.commons.codec.binary.Base64;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

public class CardPanel extends JPanel {

    public static final Dimension CARD_DIMS = new Dimension(130, 180);
    public static final Dimension CARD_DIMS_LARGE = new Dimension(347, 480);
    public Dimension img_dims;
    private Dimension preferredSize;
    private Card card;
    public static BufferedImage cardBack;
    public static BufferedImage cardFront;
    JLayeredPane pane;
    private int cardNum;

    static {
        try {
            cardBack = ImageIO.read(new ByteArrayInputStream(Base64.decodeBase64(JsonAccessor.getCardBack())));
            String front = JsonAccessor.getCardFront();
            if (front != null) {
                cardFront = ImageIO.read(new ByteArrayInputStream(Base64.decodeBase64(front)));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public CardPanel(Card card, int numCards, boolean large, int x, int y) {
        preferredSize = large ? CARD_DIMS_LARGE : CARD_DIMS;
        this.cardNum = numCards;
        img_dims = new Dimension((int) (preferredSize.width * (float) (.845)), preferredSize.height / 3);
        setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.BLACK));
//        setPreferredSize(preferredSize);

        pane = new JLayeredPane();
        pane.setBounds(-2, -2, preferredSize.width, preferredSize.height);
        pane.setPreferredSize(preferredSize);
        add(pane);

        if (card == null && numCards > 0) {
            drawCardBack();
        } else if (numCards > 0) {
            drawCard(card);
        }
        if (numCards > 1) {
            drawNum(numCards);
        }
        GuiManager.getInstance().labelFix(this);
        setBounds(x, y, preferredSize.width, preferredSize.height);
    }

    public void setCard (Card card) {
        this.card = card;

        pane.removeAll();
        if (card == null) {
            drawCardBack();
        } else {
            drawCard(card);
        }
        repaint();
    }

    public void removeCard() {
        this.cardNum = 0;
        pane.removeAll();
        repaint();
        card = null;
    }

    public void changeCardNum(int num) {
        if (card == null) {
            this.cardNum = num;
            pane.removeAll();
            if (num > 0) {
                drawCardBack();
                if (num > 1) {
                    drawNum(num);
                }
            }
            repaint();
        }
    }

    private void drawCard(Card card) {
        //card bg
        if (cardFront != null) {
            ImageIcon imageIcon = new ImageIcon(cardFront.getScaledInstance(preferredSize.width, preferredSize.height, 0));
            JLabel bg = new JLabel(imageIcon);
            bg.setBounds(0, 0, preferredSize.width, preferredSize.height);
            pane.add(bg, JLayeredPane.DEFAULT_LAYER);
        } else {
            setBackground(Color.LIGHT_GRAY);
        }

        // info
        String name = card.getName();

        BufferedImage img = null;
        try {
            img = ImageIO.read(new ByteArrayInputStream(Base64.decodeBase64(card.getImage())));
        } catch (IOException e) {
            try {
                System.out.println("Image not found from Base64 String. Using default image.");
                img = ImageIO.read(new File("external_data/default.png"));
            } catch (IOException ignored){}
        }
        try {
            if (img == null) {
                System.out.println("Image not found from Base64 String. Using default image.");
                img = ImageIO.read(new File("external_data/default.png"));
            }
        } catch (IOException ignored){}

        String description = "<html>" + card.getDescription();
        if (card instanceof Monster) {
            description += "<br>ATK: " + ((Monster) card).getAtk() + "   DEF: " + ((Monster) card).getAtk();
        }
        description += "</html>";

        //add info to card
        JLabel nameLabel = new JLabel(name);
        nameLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 24));
        nameLabel.setBounds(0, 10, preferredSize.width, 30);
        nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        pane.add(nameLabel, Integer.valueOf(1));

        JLabel imgLabel = new JLabel(new ImageIcon(img.getScaledInstance(img_dims.width, img_dims.height, 0)));
        imgLabel.setPreferredSize(img_dims);
        imgLabel.setBounds((int) (preferredSize.width * 0.0775), 50, img_dims.width, img_dims.height);
        pane.add(imgLabel, Integer.valueOf(1));


        JLabel descLabel = new JLabel(description);
        descLabel.setBounds(0, preferredSize.height / 2, preferredSize.width, preferredSize.height / 2);
        pane.add(descLabel, Integer.valueOf(1));

    }

    private void drawCardBack() {
        ImageIcon imageIcon = new ImageIcon(cardBack.getScaledInstance(preferredSize.width, preferredSize.height, 0));
        JLabel img = new JLabel(imageIcon);
        img.setBounds(0, 0, preferredSize.width, preferredSize.height);
        pane.add(img, JLayeredPane.DEFAULT_LAYER);
    }

    private void drawNum(int numCards) {
        JLabel num = new JLabel(String.valueOf(numCards));
        num.setFont(new Font("Impact", Font.BOLD, 30));
        num.setBounds(0, 0, preferredSize.width, preferredSize.height);
        num.setHorizontalAlignment(SwingConstants.CENTER);
        num.setForeground(Color.BLACK);
        pane.add(num, JLayeredPane.POPUP_LAYER);
    }

    public CardPanel(Card card, boolean large, int x, int y) {
        this(card, 1, large, x, y);
    }

    public CardPanel(Card card, int x, int y) {
        this(card, 1, false, x, y);
    }

    public int getCardNum() {
        return cardNum;
    }

    public static class CardBackPanel extends CardPanel {

        public CardBackPanel(int numCards, boolean large, int x, int y) {
            super(null, numCards, large, x, y);
        }

        public CardBackPanel(int numCards, int x, int y) {
            super(null, numCards, false, x, y);
        }
    }
}
