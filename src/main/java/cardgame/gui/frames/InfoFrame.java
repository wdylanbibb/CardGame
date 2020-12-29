package cardgame.gui.frames;

import cardgame.cards.Card;
import cardgame.gui.GuiManager;
import cardgame.gui.panels.CardPanel;

import javax.swing.*;
import java.awt.*;

public class InfoFrame extends JFrame {
    public InfoFrame(Card card) throws HeadlessException {
        super(card.getName());
        GuiManager.getInstance().initWindowTheme(this);
        CardPanel panel;
        JPanel content = new JPanel();
        panel = new CardPanel(card, true, 0, 0);
        setPreferredSize(panel.getPreferredSize());
        content.setPreferredSize(panel.getPreferredSize());
        content.setBounds(0, 0, panel.getPreferredSize().width, panel.getPreferredSize().height);
        content.add(panel);
        GuiManager.getInstance().labelFix(content);
        add(content);
        pack();
        setVisible(true);
    }
}
