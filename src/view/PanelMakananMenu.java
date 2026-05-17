package view;

import controller.GameManager;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import models.menu.Menu;
import view.modelsDaftarPanel.PanelUntukAturMenu;

public class PanelMakananMenu extends javax.swing.JPanel {

    private GameManager gm;

    public PanelMakananMenu() {
        initComponents();
    }

    public PanelMakananMenu(GameManager gm) {
        this.gm = gm;
        initComponents();
        jLabel1.setText("DAPUR — Atur Harga & Resep Menu");
        loadMenu();
    }

    public void loadMenu() {
        container.removeAll();
        if (gm == null || gm.getRestaurant() == null) {
            container.revalidate();
            container.repaint();
            return;
        }

        for (Menu menu : gm.getRestaurant().lihatDaftarMenu()) {
            PanelUntukAturMenu baris = new PanelUntukAturMenu(menu, gm);
            baris.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
            baris.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
            container.add(baris);
            container.add(javax.swing.Box.createVerticalStrut(6));
        }

        container.revalidate();
        container.repaint();
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        container = new javax.swing.JPanel();

        setBackground(new java.awt.Color(26, 30, 42));
        setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 8, 8, 8));
        setLayout(new java.awt.BorderLayout());

        jLabel1.setFont(new Font("SansSerif", Font.BOLD, 16));
        jLabel1.setForeground(new Color(255, 204, 102));
        jLabel1.setBorder(BorderFactory.createEmptyBorder(8, 4, 16, 4));
        add(jLabel1, BorderLayout.PAGE_START);

        jScrollPane1.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        container.setBackground(new Color(41, 33, 45));
        container.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        container.setLayout(new javax.swing.BoxLayout(container, javax.swing.BoxLayout.Y_AXIS));
        jScrollPane1.setViewportView(container);

        add(jScrollPane1, BorderLayout.CENTER);
    }

    private javax.swing.JPanel container;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
}
