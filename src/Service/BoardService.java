package Service;

import java.util.Map;
import java.util.ArrayList;
import java.util.List;

import Model.Board;
import Model.GameStatusEnum;
import Model.Space;


public class BoardService{

    private final static int BOARD_LIMIT = 9;
    
    private final Board board;

    public BoardService(final Map<String, String> gameConfig){
        board = new Board(initBoard(gameConfig)); 
    }

    public List<List<Space>> getSpaces(){
        return board.getSpaces();
    }

    public void reset(){
        board.reset();
    }

    public boolean hasErrors(){
        return board.hasErrors();
    }

    public GameStatusEnum getStatus(){
        return board.getStatus();
    }

    public boolean gameIsFinished(){
        return board.gameIsFinished();
    }

    private List<List<Space>> initBoard(final Map<String, String> gameConfig){

        List<List<Space>> spaces = new ArrayList<>();
        for (int i = 0; i < BOARD_LIMIT; i++){
            List<Space> row = new ArrayList<>();
            for (int j = 0; j < BOARD_LIMIT; j++){
                var cfg      = gameConfig.get("%s,%s".formatted(i, j));
                var parts    = cfg.split(",");
                var expected = Integer.parseInt(parts[0]);
                var fixed    = Boolean.parseBoolean(parts[1]);
                row.add(new Space(expected, fixed));
            }
            spaces.add(row);
        }
        return spaces;
    }
}
