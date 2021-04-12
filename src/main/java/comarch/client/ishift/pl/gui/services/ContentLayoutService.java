package comarch.client.ishift.pl.gui.services;

import comarch.client.ishift.pl.gui.MainWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Main content container services
 *
 * @since 1.0
 */
public class ContentLayoutService implements MouseListener {
    private JLabel selectedLabel;
    private final Color baseColor;

    public ContentLayoutService() {

        baseColor = new JLabel().getBackground();
        selectedLabel = null;
    }

    /**
     * Displays song details cursor pointed song on mouse button clicked
     *
     * @param mouseEvent calling component event
     */
    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        JLabel label = (JLabel) mouseEvent.getComponent();
        label.setBackground(Color.GRAY);

        if (selectedLabel != null) {
            selectedLabel.setBackground(baseColor);
        }

        selectedLabel = label;
        MainWindow.getMainWindow().getAccountingOfficeData().getUserDataList().forEach(userData -> {
            if(userData.getDbName().equals(label.getName())){
                MainWindow.getMainWindow().getSideBar().displayUserDetails(userData);
            }
        });
    }

    /**
     * Not implemented
     */
    @Override
    public void mousePressed(MouseEvent mouseEvent) {
    }

    /**
     * Not implemented
     */
    @Override
    public void mouseReleased(MouseEvent mouseEvent) {
    }

    /**
     * Marks cursor pointed song on mouse enter action
     *
     * @param mouseEvent calling component event
     */
    @Override
    public void mouseEntered(MouseEvent mouseEvent) {
        if (mouseEvent.getComponent() != selectedLabel) {
            mouseEvent.getComponent().setBackground(Color.LIGHT_GRAY);
        }
    }

    /**
     * Clears mark cursor pointed song on mouse leave action
     *
     * @param mouseEvent calling component event
     */
    @Override
    public void mouseExited(MouseEvent mouseEvent) {
        if (mouseEvent.getComponent() != selectedLabel) {
            mouseEvent.getComponent().setBackground(baseColor);
        }
    }
}
