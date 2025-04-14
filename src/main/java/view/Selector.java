package view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 * Custom activity selector, used to set up a dialog with checkboxes for selecting activities to filter by.
 */
public final class Selector {

    /** Private constructor to prevent instantiation. */
    private Selector() {
    }
    /**
     * Display a dialog with checkboxes holding the activities passed in as an argument.
     * @param parent
     * @param title
     * @param options
     * @return a list of the selected options
     */
    public static List<String> showSelector(Component parent, String title, List<String> options) {
        List<String> selectedItems = new ArrayList<>();
        
        JDialog dialog = new JDialog();
        dialog.setTitle(title);
        dialog.setModal(true);
        
        JPanel mainPanel = new JPanel();
        int columns = 2;
        mainPanel.setLayout(new GridLayout(0, columns, 10, 5));
        
        List<JCheckBox> checkboxes = new ArrayList<>();
        for (String option : options) {
            JCheckBox checkBox = new JCheckBox(option);
            checkboxes.add(checkBox);
            mainPanel.add(checkBox);
        }
        
        if (options.size() % columns != 0) {
            mainPanel.add(new JPanel());
        }

        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JPanel buttonPanel = new JPanel();
        JButton confirmButton = new JButton("Confirm");
        buttonPanel.add(confirmButton);
        
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedItems.clear();
                for (JCheckBox checkBox : checkboxes) {
                    if (checkBox.isSelected()) {
                        selectedItems.add(checkBox.getText());
                    }
                }
                dialog.dispose();
            }
        });
        
        dialog.setLayout(new BorderLayout());
        dialog.add(scrollPane, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        
        dialog.setPreferredSize(new Dimension(500, 400));
        dialog.pack();
        dialog.setLocationRelativeTo(parent);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setVisible(true);
        
        return selectedItems;
    }
    
}
