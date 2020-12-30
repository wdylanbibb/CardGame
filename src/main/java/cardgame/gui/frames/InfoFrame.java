package cardgame.gui.frames;

import cardgame.cards.Card;
import cardgame.gui.GuiManager;
import cardgame.gui.panels.CardPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class InfoFrame extends JFrame {
    public InfoFrame(Card card) throws HeadlessException {
        super(card.getName());
        GuiManager.getInstance().initWindowTheme(this);
        CardPanel panel;
        JPanel content = new JPanel();
        panel = new CardPanel(card, true, 0, 0);
//        content.setPreferredSize(panel.getPreferredSize());
//        content.setBounds(0, 0, panel.getPreferredSize().width, panel.getPreferredSize().height);
        content.add(panel);
        GuiManager.getInstance().labelFix(content);
        add(content);
//        pack();
        setBounds(GuiManager.getInstance().getInfoFrameLoc());
        setVisible(true);
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                if (e.getComponent() == InfoFrame.this) {
                    String info = e.paramString().split("\\(")[1].split("\\)")[0];
                    int x = Integer.parseInt(info.split(",")[0]);
                    int y = Integer.parseInt(info.split(",")[1].split(" ")[0]);
                    int w = Integer.parseInt(info.split(" ")[1].split("x")[0]);
                    int h = Integer.parseInt(info.split(" ")[1].split("x")[1]);
                    GuiManager.getInstance().setInfoFrameLoc(x, y, w, h);
                }
            }

            @Override
            public void componentMoved(ComponentEvent e) {
                if (e.getComponent() == InfoFrame.this) {
                    String info = e.paramString().split("\\(")[1].split("\\)")[0];
                    int x = Integer.parseInt(info.split(",")[0]);
                    int y = Integer.parseInt(info.split(",")[1].split(" ")[0]);
                    int w = Integer.parseInt(info.split(" ")[1].split("x")[0]);
                    int h = Integer.parseInt(info.split(" ")[1].split("x")[1]);
                    GuiManager.getInstance().setInfoFrameLoc(x, y, w, h);
                }
            }
        });
    }
}
