package comarch.client.ishift.pl.gui;


import javax.swing.*;
import java.awt.*;

/**
 * Clears GUI view
 *
 * @since 1.0
 */
public abstract class InterfaceClear {

    /**
     * Removes all components in given component and refreshes the view
     *
     * @param component {@link JComponent} to clear and refresh
     */
    protected void clearView(JComponent component) {
        component.removeAll();
        component.repaint();
    }

   protected JScrollPane createJScrollPane(JPanel panel, int height, int width) {
        JScrollPane jScrollPane = new JScrollPane(panel);

        if (height > 700) {
            height = 700;
        }

        jScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane.getVerticalScrollBar().setUnitIncrement(10);
        jScrollPane.setPreferredSize(new Dimension(width, height));

        return jScrollPane;
    }
}
