
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.Stream;

import ui.custom.screen.MainScreen;

import static java.util.stream.Collectors.toMap;

public class UIMain {

    public static void main(String[] args) {
        Map<String,String> gameConfig = Stream.of(args).collect(toMap(k -> k.split(";")[0], v -> v.split(";")[1]));
            if (args.length > 0) {
                gameConfig = Stream.of(args)
                .collect(toMap(
                    k -> k.split(";")[0],
                    v -> v.split(";")[1]
                ));
            } else {
                gameConfig = generatePuzzle(40);
            }

        var mainsScreen = new MainScreen(gameConfig);
        mainsScreen.buildMainScreen();
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
