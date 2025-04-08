package view;

import javax.swing.*;
import java.awt.*;
import model.Records.Park;

/**
 * A panel that uses CardLayout to switch between a summary view (JList)
 * and a detail view (DetailViewPanel).
 */
public class DisplayAreaPanel extends JPanel {

    private CardLayout cardLayout;
    private JScrollPane summaryScrollPane; // Scroll pane containing the JList
    private DetailViewPanel detailViewPanel;

    // Define card names as constants
    private static final String SUMMARY_CARD = "SUMMARY";
    private static final String DETAIL_CARD = "DETAIL";

    /**
     * Constructs the DisplayAreaPanel.
     * 
     * @param summaryScrollPane The scroll pane containing the summary JList.
     * @param detailViewPanel   The panel responsible for showing park details.
     */
    public DisplayAreaPanel(JScrollPane summaryScrollPane, DetailViewPanel detailViewPanel) {
        this.summaryScrollPane = summaryScrollPane;
        this.detailViewPanel = detailViewPanel;

        cardLayout = new CardLayout();
        setLayout(cardLayout);

        add(summaryScrollPane, SUMMARY_CARD);
        add(detailViewPanel, DETAIL_CARD);
    }

    /**
     * Shows the summary list view.
     */
    public void showSummaryView() {
        cardLayout.show(this, SUMMARY_CARD);
        // Scrolling of the summary list should be handled by the caller (TextPanel)
        // after potential selection changes.
    }

    /**
     * Shows the detail view and updates it with the given park's details.
     * 
     * @param park The park to display details for.
     */
    public void showDetailView(Park park) {
        if (park != null) {
            detailViewPanel.setParkDetails(park);
            cardLayout.show(this, DETAIL_CARD);
        } else {
            // Optionally handle null park case, maybe show summary or an error message
            System.err.println("Attempted to show detail view for a null park.");
            showSummaryView(); // Revert to summary view as a fallback
        }
    }
} 