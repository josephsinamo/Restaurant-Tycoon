package view.modelsDaftarPanel;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import models.jimat.*;

public class PanelInventarisJimat extends JPanel implements ListCellRenderer<Jimat> {

    private static final Color BG_NORMAL = new Color(41, 33, 45);
    private static final Color BG_SELECT = new Color(60, 80, 120);
    private static final Color TEXT_MAIN = new Color(230, 230, 240);
    private static final Color TEXT_DIM  = new Color(160, 155, 175);
    private static final Color ACCENT    = new Color(255, 180, 50);

    private JLabel lblIcon;
    private JLabel lblNama;
    private JLabel lblPower;

    public PanelInventarisJimat() {
        setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        setPreferredSize(new Dimension(400, 64));
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 64));
        setBorder(new CompoundBorder(
            new EmptyBorder(2, 2, 2, 2),
            new LineBorder(new Color(70, 60, 85), 1)));

        lblIcon = new JLabel("🧿", JLabel.CENTER);
        lblIcon.setFont(new Font("Serif", Font.PLAIN, 28));
        lblIcon.setPreferredSize(new Dimension(52, 52));
        lblIcon.setMinimumSize(new Dimension(52, 52));
        lblIcon.setMaximumSize(new Dimension(52, 52));

        JPanel info = new JPanel();
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        info.setOpaque(false);
        info.setBorder(new EmptyBorder(8, 12, 8, 8));

        lblNama  = new JLabel("Nama Jimat");
        lblNama.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblNama.setForeground(TEXT_MAIN);

        lblPower = new JLabel("Power: 0");
        lblPower.setFont(new Font("SansSerif", Font.PLAIN, 11));
        lblPower.setForeground(TEXT_DIM);

        info.add(lblNama);
        info.add(Box.createVerticalStrut(4));
        info.add(lblPower);

        add(lblIcon);
        add(info);
        add(Box.createHorizontalGlue());
    }

    @Override
    public Component getListCellRendererComponent(
            JList<? extends Jimat> list, Jimat jimat,
            int index, boolean isSelected, boolean cellHasFocus) {

        if (jimat != null) {
            String icon = switch (jimat.getClass().getSimpleName()) {
                case "Charming"  -> "✨";
                case "Cleaner"   -> "🧹";
                case "Security"  -> "🛡";
                default          -> "🧿";
            };
            lblIcon.setText(icon);
            lblNama.setText(jimat.getName());
            lblPower.setText(String.format("Power : %.1f", jimat.getPower()));
        }

        setBackground(isSelected ? BG_SELECT : BG_NORMAL);
        setOpaque(true);
        lblNama.setForeground(isSelected ? Color.WHITE : TEXT_MAIN);
        lblPower.setForeground(isSelected ? new Color(200, 200, 220) : TEXT_DIM);
        return this;
    }
}
