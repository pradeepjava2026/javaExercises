package karatExercises;

import java.util.*;

public class SnakeBoard {

    public static void findPassableLanes(char[][] board) {
        List<Integer> rows = new ArrayList<>();
        List<Integer> cols = new ArrayList<>();

        int r = board.length;
        int c = board[0].length;

        // Step 1: Check rows
        for (int i = 0; i < r; i++) {
            boolean allZero = true;

            for (int j = 0; j < c; j++) {
                if (board[i][j] != '0') {
                    allZero = false;
                    break;
                }
            }

            if (allZero) {
                rows.add(i);
            }
        }

        // Step 2: Check columns
        for (int j = 0; j < c; j++) {
            boolean allZero = true;

            for (int i = 0; i < r; i++) {
                if (board[i][j] != '0') {
                    allZero = false;
                    break;
                }
            }

            if (allZero) {
                cols.add(j);
            }
        }

        // Output
        System.out.println("Rows: " + rows);
        System.out.println("Columns: " + cols);
    }

    public static void main(String[] args) {
        char[][] board1 = {
            {'+', '+', '+', '0', '+', '0', '0'},
            {'0', '0', '+', '0', '0', '0', '0'},
            {'0', '0', '0', '0', '+', '0', '0'},
            {'+', '+', '+', '0', '0', '+', '0'},
            {'0', '0', '0', '0', '0', '0', '0'}
        };

        findPassableLanes(board1); // Rows: [4], Columns: [3, 6]
    }
    
    
}
