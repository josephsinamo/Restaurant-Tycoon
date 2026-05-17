package view;

import controller.GameManager;
import java.awt.*;
import javax.swing.*;
import models.menu.Menu;
import view.modelsDaftarPanel.*;

public class PanelMakananMenu extends javax.swing.JPanel {
    private GameManager gm;

    // ── constructor lama ───────────────────────────────────────────────────
    public PanelMakananMenu() {
        initComponents();
    }

    // ── constructor baru dengan GameManager ────────────────────────────────
    public PanelMakananMenu(GameManager gm) {
        this.gm = gm;
        initComponents();
        loadMenu();
    }

    // ── load menu dari game ────────────────────────────────────────────────
    public void loadMenu() {
        container.removeAll();
        if (gm == null) return;

        for (Menu menu : gm.getRestaurant().lihatDaftarMenu()) {
            container.add(createMenuCard(menu));
        }
        container.revalidate();
        container.repaint();
    }

    // ── card tiap menu dengan set harga ───────────────────────────────────
    private JPanel createMenuCard(Menu menu) {
        JPanel card = new JPanel(new BorderLayout(4, 4));
        card.setBackground(new Color(41, 33, 45));
        card.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));

        JLabel lblNama = new JLabel(menu.getName());
        lblNama.setForeground(Color.WHITE);
        lblNama.setFont(new Font("SansSerif", Font.BOLD, 13));

        JLabel lblHarga = new JLabel("Rp " + String.format("%.0f", menu.getPrice()));
        lblHarga.setForeground(new Color(255, 180, 50));

        JTextField txtHarga = new JTextField(String.valueOf((int) menu.getPrice()), 8);

        JButton btnSet = new JButton("Set Harga");
        btnSet.addActionListener(e -> {
            try {
                double hargaBaru = Double.parseDouble(txtHarga.getText().trim());
                gm.getRestaurant().setHarga(menu, hargaBaru);
                lblHarga.setText("Rp " + String.format("%.0f", hargaBaru));
                JOptionPane.showMessageDialog(this,
                    "Harga " + menu.getName() + " → Rp " + String.format("%.0f", hargaBaru));
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Masukkan angka yang valid!");
            }
        });

        JPanel south = new JPanel(new FlowLayout(FlowLayout.LEFT));
        south.setBackground(new Color(41, 33, 45));
        south.add(new JLabel("Harga baru:"));
        south.add(txtHarga);
        south.add(btnSet);

        card.add(lblNama, BorderLayout.NORTH);
        card.add(lblHarga, BorderLayout.CENTER);
        card.add(south, BorderLayout.SOUTH);

        return card;
    }

    // ── generated code — jangan diubah ────────────────────────────────────
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        container = new javax.swing.JPanel();

        setBackground(new java.awt.Color(26, 30, 42));
        setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 8, 8, 8));
        setLayout(new java.awt.BorderLayout());

        jLabel1.setFont(new java.awt.Font("Stencil", 1, 24));
        jLabel1.setForeground(new java.awt.Color(255, 204, 102));
        jLabel1.setText("MENU");
        jLabel1.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 1, 20, 1));
        add(jLabel1, java.awt.BorderLayout.PAGE_START);

        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        container.setBackground(new java.awt.Color(255, 255, 255));
        container.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 3, 10, 3));
        container.setAutoscrolls(true);
        container.setLayout(new javax.swing.BoxLayout(container, javax.swing.BoxLayout.Y_AXIS));
        jScrollPane1.setViewportView(container);

        add(jScrollPane1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel container;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}