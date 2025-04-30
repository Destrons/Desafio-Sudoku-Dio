
import Model.Board;
import java.util.Scanner;
import java.util.Set;

import Model.Space;
import java.util.stream.Stream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toMap;
import static util.BoardTemplate.BOARD_TEMPLATE;

public class Main {

    private final static Scanner scanner = new Scanner(System.in);

    private static Board board;

    private final static int BOARD_LIMIT = 9;

    public static void main(String[] args) {
        
        final Map<String,String> positions;
            if (args.length > 0) {
                positions = Stream.of(args)
                    .collect(toMap(
                        k -> k.split(";")[0],
                        v -> v.split(";")[1]
                    ));
            } else {
                positions = generatePuzzle(40);
            }

        var option = -1;
        while (true){
            System.out.println("Selecione uma das opções a seguir:");
            System.out.println("1 - Iniciar um novo jogo.");
            System.out.println("2 - Colocar um novo número.");
            System.out.println("3 - Remover um número.");
            System.out.println("4 - Visualizar jogo atual.");
            System.out.println("5 - Verificar status do jogo.");
            System.out.println("6 - Limpar jogo.");
            System.out.println("7 - Finalizar jogo.");
            System.out.println("8 - Sair.");

            option = scanner.nextInt();

            switch (option) {
                case 1:
                    startGame(positions);
                    break;
                case 2:
                    imputNumber();
                    break;
                case 3:
                    removeNumber();
                    break;
                case 4:
                    showCurrentGame();
                    break;
                case 5:
                    showGameStatus();
                    break;
                case 6:
                    clearGame();
                    break;
                case 7:
                    finishGame();
                    break;
                case 8:
                    System.exit(0);
                    break;
            
                default:
                System.out.println("Opção inválida.");
                    break;
            }
        }
    }

    private static void startGame(final Map<String, String> positions) {
        if(nonNull(board)){
            System.out.println("O jogo já foi iniciado!");
            return;
        }

        List<List<Space>> spaces = new ArrayList<>();
        for (int i = 0; i < BOARD_LIMIT; i++){
            List<Space> row = new ArrayList<>();
            for (int j = 0; j < BOARD_LIMIT; j++){
                var cfg      = positions.get("%s,%s".formatted(i, j));
                var parts    = cfg.split(",");
                var expected = Integer.parseInt(parts[0]);
                var fixed    = Boolean.parseBoolean(parts[1]);
                row.add(new Space(expected, fixed));
            }
            spaces.add(row);
        }
        board = new Board(spaces);
        System.out.println("O jogo está pronto para começar");
        
    }

    private static void imputNumber() {
        if(isNull(board)){
            System.out.println("O jogo ainda não foi iniciado!");
            return;
        }
        System.out.println("Informe a coluna que deseja colocar o número:");
        var col = runUntilGetValidNumber(0, 8);
        System.out.println("Informe a linha que deseja colocar o número:");
        var row = runUntilGetValidNumber(0, 8);
        System.out.printf("Informe o número que vai entrar na posição [%s,%s]:\n", col, row);
        var value = runUntilGetValidNumber(1, 9);

        if(!board.changeValue(col, row, value)){
            System.out.printf("A posição [%s,%s] já está preenchida ou e fixo!", col, row);
        }

    }
    private static void removeNumber() {
        if(isNull(board)){
            System.out.println("O jogo ainda não foi iniciado!");
            return;
        }
        System.out.println("Informe a coluna que deseja Remover o número:");
        var col = runUntilGetValidNumber(0, 8);
        System.out.println("Informe a linha que deseja Remover o número:");
        var row = runUntilGetValidNumber(0, 8);

        if(!board.clearValue(col, row)){
            System.out.printf("A posição [%s,%s] já está preenchida ou e fixo!", col, row);
        }

    }
    private static void showCurrentGame() {
        if(isNull(board)){
            System.out.println("O jogo ainda não foi iniciado!");
            return;
        }

        var args = new Object[BOARD_LIMIT * BOARD_LIMIT];
        var argsPos = 0;
        for (int i = 0; i < BOARD_LIMIT; i++){
            for (var col: board.getSpaces()){
                    args[argsPos ++] = " " + (isNull(col.get(i).getActual()) ? " " : col.get(i).getActual());
            }
        }
        
        System.out.println("Seu jogo se encontra da seguinte forma:");
        System.out.printf((BOARD_TEMPLATE) + "$n", args);

    }

    private static void showGameStatus() {
        if(isNull(board)){
            System.out.println("O jogo ainda não foi iniciado!");
            return;
        }

        System.out.printf("O jogo atualmente se encontra no status %s\n", board.getStatus().getLabel());
        if(board.hasErrors()){
            System.out.println("O jogo contém erros!");
        } else {
            System.out.println("O jogo não contém erros!");
        }
    }
    private static void clearGame() {
        if(isNull(board)){
            System.out.println("O jogo ainda não foi iniciado!");
            return;
        }

        System.out.println("Tem certeza que deseja limpar o jogo e perder o seu progresso? (S/N)");
        var confirm = scanner.next();
        while (!confirm.equalsIgnoreCase("S") || !confirm.equalsIgnoreCase("N")){
            System.out.println("Informe S para sim ou N para não");
            confirm = scanner.next();
        }
        if(confirm.equalsIgnoreCase("S")){
            board.reset();
        }
            
    }
    
    private static void finishGame() {
        if(isNull(board)){
            System.out.println("O jogo ainda não foi iniciado!");
            return;
        }

        if (board.gameIsFinished()){
            System.out.println("Parabens, você concluiu o jogo!");
            showCurrentGame();
            board = null;
        } else if (board. hasErrors()){
            System.out.println("Seu jogo contém erros, verifique seu board!");
        } else {
            System.out.println("Você aindaprecisa preencher todos os espaços!");
        }

    }

    private static int runUntilGetValidNumber(final int min, final int max){
        var current = scanner.nextInt();
        while (current < min || current > max){
            System.out.printf("Informe um número entre %s e %s\n", min, max);
            current = scanner.nextInt();
        }
        return current;
    }

    private static Map<String,String> generatePuzzle(int removeCount) {
        final int N = 9;
        int[][] solution = new int[N][N];
        fillCell(solution, 0, 0);

        List<Integer> all = new ArrayList<>();
        for (int i = 0; i < N*N; i++) all.add(i);
        Collections.shuffle(all, new Random());
        Set<Integer> toRemove = Set.copyOf(all.subList(0, removeCount));

        Map<String,String> map = new HashMap<>();
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                int idx = i * N + j;
                boolean fixed = !toRemove.contains(idx);
                int val = fixed ? solution[i][j] : 0;
                map.put(i + "," + j, val + "," + fixed);
            }
        }
        return map;
    }

    private static boolean fillCell(int[][] b, int row, int col) {
        final int N = 9;
        if (row == N) return true;
        int nextRow = (col == N-1) ? row+1 : row;
        int nextCol = (col == N-1) ? 0 : col+1;
        List<Integer> nums = new ArrayList<>();
        for (int i = 1; i <= N; i++) nums.add(i);
        Collections.shuffle(nums, new Random());
        for (int n : nums) {
            if (canPlace(b, row, col, n)) {
                b[row][col] = n;
                if (fillCell(b, nextRow, nextCol)) return true;
                b[row][col] = 0;
            }
        }
        return false;
    }

    private static boolean canPlace(int[][] b, int r, int c, int num) {
        final int N = 9;
        for (int i = 0; i < N; i++)
            if (b[r][i] == num || b[i][c] == num) return false;
        int br = (r/3)*3, bc = (c/3)*3;
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                if (b[br+i][bc+j] == num) return false;
        return true;
    }

}
