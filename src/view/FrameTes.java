
package view;

import java.awt.*;
import javax.swing.*;

public class FrameTes extends JFrame{

    private CardLayout cardLayout;
    private JPanel mainPanel;

    public static final String KARTU_MENU = "menu";
    public static final String KARTU_PERMAINAN = "permainan";

    public FrameTes() {

        setTitle("Game Restoran");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        mainPanel.add(new PanelMenuUtama(), KARTU_MENU);
        mainPanel.add(new PanelPermainan(), KARTU_PERMAINAN);

        setContentPane(mainPanel);
        cardLayout.show(mainPanel, KARTU_MENU);

        tampilkanLayarPenuh();
    }

    /** Panel layar menu / beranda. */
    private class PanelMenuUtama extends JPanel {
        PanelMenuUtama() {
            setLayout(new BorderLayout());
            add(new JLabel("Menu Utama", SwingConstants.CENTER), BorderLayout.CENTER);
        }
    }

    /** Panel area permainan utama. */
    private class PanelPermainan extends JPanel {
        PanelPermainan() {
            setLayout(new BorderLayout());
            add(new JLabel("Permainan", SwingConstants.CENTER), BorderLayout.CENTER);
        }
    }

    

    public void tampilkanLayarPenuh() {
        
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setVisible(true);

        /* untuk fullScreen
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();

        if (gd.isFullScreenSupported()) {
            gd.setFullScreenWindow(this);
        } else {
            System.err.println("Full screen tidak didukung, menggunakan mode Maksimal.");
            this.setExtendedState(JFrame.MAXIMIZED_BOTH);
            this.setVisible(true);
        }
         */
    }
}

class mainSementara{
    public static void main(String[] args) {
        FrameTes f = new FrameTes();
    }
}