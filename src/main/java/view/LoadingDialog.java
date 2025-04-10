package view;
import javax.swing.*;
import java.awt.*;

public class LoadingDialog {

    private static JDialog dialog;

    /**
     * Displays the loading popup centered over the given parent component.
     * If already visible, does nothing.
     */
    public static void show(Component parent, String message) {
        if (dialog != null && dialog.isShowing()) return;

        Frame owner = JOptionPane.getFrameForComponent(parent);
        dialog = new JDialog(owner, "Loading", true); // modal
        dialog.setUndecorated(true);
        dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(new Color(0, 0, 0, 150));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel messageLabel = new JLabel(message != null ? message : "Loading...");
        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        messageLabel.setForeground(Color.WHITE);
        messageLabel.setFont(new Font("SansSerif", Font.BOLD, 14));

        JProgressBar progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        progressBar.setAlignmentX(Component.CENTER_ALIGNMENT);
        progressBar.setMaximumSize(new Dimension(200, 20));

        contentPanel.add(Box.createVerticalGlue());
        contentPanel.add(messageLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        contentPanel.add(progressBar);
        contentPanel.add(Box.createVerticalGlue());

        dialog.setContentPane(contentPanel);
        dialog.setSize(300, 100);
        dialog.setLocationRelativeTo(parent);

        SwingUtilities.invokeLater(() -> dialog.setVisible(true));
    }

    /**
     * Disposes the currently visible loading popup, if any.
     */
    public static void hide() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dispose();
            dialog = null;
        }
    }
}