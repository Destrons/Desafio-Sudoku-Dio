package ui.custom.screen;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import Model.Space;
import Service.BoardService;
import Service.NotifierService;
import ui.custom.button.FinishGameButton;
import ui.custom.button.CheckGameStatusButton;
import ui.custom.button.ResetGameButton;
import ui.custom.frame.MainFrame;
import ui.custom.input.NumberText;
import ui.custom.panel.MainPanel;
import ui.custom.panel.SudokuSector;

import static javax.swing.JOptionPane.YES_NO_OPTION;
import static javax.swing.JOptionPane.showConfirmDialog;
import static javax.swing.JOptionPane.showMessageDialog;
import static Service.EventEnum.CLEAR_SPACE;
import static javax.swing.JOptionPane.QUESTION_MESSAGE;

public class MainScreen {

    private final static Dimension dimension = new Dimension(600, 600);

    private final BoardService boardService;
    private final NotifierService notifierService;

    private JButton finisheGameButton;
    private JButton checkGameStatusButton;
    private JButton resetButton;

    public MainScreen(final Map<String, String> gameConfig) {
        this.boardService = new BoardService(gameConfig);
        this.notifierService = new NotifierService();
    }

    public void buildMainScreen() {
        JPanel mainPanel = new MainPanel(dimension);
        JFrame mainFrame = new MainFrame(dimension, mainPanel);
                for (int r = 0; r < 9; r +=3 ){
            var endRow = r + 2;
                        for (int c = 0; c < 9; c +=3 ){
                var endCol = c + 2;
                var spaces = getSpacesFromSector(boardService.getSpaces(), c , endCol, r , endRow);
                JPanel sector = generateSection(spaces);
                mainPanel.add(sector);
            }
        }
        addfinisheGameButton(mainPanel);
        addCheckGameStatusButton(mainPanel);
        addResetGameButton(mainPanel);
        mainFrame.revalidate();
        mainFrame.repaint();
    }

    private List<Space> getSpacesFromSector(final List<List<Space>> spaces, final int initColl, final int endCol,final int initRow, final int endRow) {
        List<Space> spaceSector = new ArrayList();
        for (int r = initRow; r <= endRow; r++){
            for (int c = initColl; c <= endCol; c++){
                spaceSector.add(spaces.get(c).get(r));
            }
        }
                return spaceSector;
    }

    private JPanel generateSection(final List<Space> spaces){
        List<NumberText> fields = new ArrayList<>(spaces.stream().map(NumberText::new).toList());
        fields.forEach(t -> notifierService.subscriber(CLEAR_SPACE, t));
        return new SudokuSector(fields);
    }
        
        private void addfinisheGameButton(final JPanel mainPanel) {
            finisheGameButton = new FinishGameButton(e -> {
                if (boardService.gameIsFinished()){
                    showMessageDialog(null,"Parabéns! você terminou o jogo.");
                    resetButton.setEnabled(false);
                    checkGameStatusButton.setEnabled(false);
                    finisheGameButton.setEnabled(false);
                } else {
                    var message = "Seu jogo contém algum erro, ajuste e tente novamente.";
                    showMessageDialog(null,message);
                }
            });
            mainPanel.add(finisheGameButton);
        }

        private void addCheckGameStatusButton(final JPanel mainPanel) {
            checkGameStatusButton = new CheckGameStatusButton(e -> {
                var hasErrors = boardService.hasErrors();
                var gameStatus = boardService.getStatus();
                var message = switch (gameStatus){
                    case NON_STARTED -> "O jogo não foi iniciado!";
                    case INCOMPLETE -> "O jogo está incompleto!";
                    case COMPLETE -> "O jogo está completo";
                };
                message += hasErrors ? " e contém erros!" : " e não contém erros!";
                showMessageDialog(null, message);
            });
            mainPanel.add(checkGameStatusButton);
        }

        private void addResetGameButton(final JPanel mainPanel) {
            resetButton = new ResetGameButton(e -> {
                var dialogResult = showConfirmDialog(
                    null, 
                    "Deseja realmente reiniciar o jogo?",
                    "Limpar o jogo",
                    YES_NO_OPTION,
                    QUESTION_MESSAGE
                );
                if (dialogResult == 0){
                    boardService.reset();
                    notifierService.notify(CLEAR_SPACE);
                }
            });
            mainPanel.add(resetButton);
        }

}
