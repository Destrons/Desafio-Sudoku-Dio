import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;

import ui.custom.frame.MainFrame;
import ui.custom.panel.MainPanel;

public class UIMain {

    public static void main(String[] args) {
        var dimension = new Dimension(600, 600);
        JPanel mainPanel = new MainPanel(dimension);
        JFrame mainFrame = new MainFrame(dimension, mainPanel);
        mainFrame.revalidate();
        mainFrame.repaint();
    }
    
}
