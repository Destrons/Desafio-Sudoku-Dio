package ui.custom.frame;

import ui.custom.panel.MainPanel;

import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.Dimension;
import java.util.Map;

public class MainFrame extends JFrame{

    public MainFrame(final Dimension dimension, final JPanel mainPanel){

        super("Sudoku");
        this.setSize(dimension);
        this.setPreferredSize(dimension);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.add(mainPanel);

    }
    
}
