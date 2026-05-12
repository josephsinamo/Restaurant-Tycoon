
package view;

import java.awt.*;
import javax.swing.*;

public class Frame extends JFrame{

    private CardLayout cardLayout;
    private JPanel mainPanel
    ;
    public Frame() {

        setTitle("Game Restoran");;
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);


        tampilkanLayarPenuh();
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
        Frame f = new Frame();
    }
}