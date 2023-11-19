import java.util.ArrayList;
import java.util.List;

class Player {

    private static final int MAX_DEPTH = 4;

    private int marbleCounter;
    private char symbol;

    Player(char symbol) {
        marbleCounter = 0;
        this.symbol = symbol;
    }

    int aiCount;
    int opponentCount;

    public int[] getBestMove(char[][] board) {
        int[] bestMove;

        do {
            bestMove = minimax(board, MAX_DEPTH, Integer.MIN_VALUE, Integer.MAX_VALUE, true);
        } while (!isValidMove(board, bestMove[1], bestMove[2]));

        return new int[]{bestMove[1], bestMove[2]};
    }

    private boolean isValidMove(char[][] board, int col, int row) {
        return col >= 0 && row >= 0 && col < MomentumGame.BOARD_SIZE && row < MomentumGame.BOARD_SIZE && board[row][col] == '-';
    }

    private int[] minimax(char[][] board, int depth, int alpha, int beta, boolean maximizingPlayer) {
        List<int[]> possibleMoves = getEmptyCells(board);
        if (possibleMoves.size() == MomentumGame.BOARD_SIZE * MomentumGame.BOARD_SIZE) {
            return new int[]{1, 3, 3}; // Starting move if the board is empty
        }

        if (depth == 0 || possibleMoves.isEmpty()) {
            int evaluation = evaluateBoard(board);
            return new int[]{evaluation, -1, -1};
        }

        int[] bestMove = maximizingPlayer ? new int[]{Integer.MIN_VALUE, -1, -1} : new int[]{Integer.MAX_VALUE, -1, -1};

        // Sort moves based on the evaluation to improve alpha-beta pruning
        possibleMoves.sort((m1, m2) -> {
            board[m1[0]][m1[1]] = maximizingPlayer ? 'O' : 'X';
            int eval1 = evaluateBoard(board);
            board[m1[0]][m1[1]] = '-';

            board[m2[0]][m2[1]] = maximizingPlayer ? 'O' : 'X';
            int eval2 = evaluateBoard(board);
            board[m2[0]][m2[1]] = '-';

            return maximizingPlayer ? Integer.compare(eval2, eval1) : Integer.compare(eval1, eval2);
        });

        for (int[] move : possibleMoves) {
            int row = move[0];
            int col = move[1];

            char originalValue = board[row][col];
            board[row][col] = maximizingPlayer ? 'O' : 'X';

            int[] currentMove = minimax(board, depth - 1, alpha, beta, !maximizingPlayer);

            board[row][col] = originalValue;

            currentMove[1] = col;
            currentMove[2] = row;

            if (maximizingPlayer) {
                if (currentMove[0] > bestMove[0]) {
                    bestMove = currentMove;
                }
                alpha = Math.max(alpha, bestMove[0]);
            } else {
                if (currentMove[0] < bestMove[0]) {
                    bestMove = currentMove;
                }
                beta = Math.min(beta, bestMove[0]);
            }

            if (beta <= alpha) {
                break;
            }
        }

        return bestMove;
    }

    private List<int[]> getEmptyCells(char[][] board) {
        List<int[]> emptyCells = new ArrayList<>();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] == '-') {
                    emptyCells.add(new int[]{i, j});
                }
            }
        }
        return emptyCells;
    }

    private int evaluateBoard(char[][] board) {
        int score = 0;

        // Evaluate rows, columns, and diagonals
        for (int i = 0; i < MomentumGame.BOARD_SIZE; i++) {
            score += evaluateLine(board[i]); // Evaluate row
            score += evaluateLine(getColumn(board, i)); // Evaluate column
        }

        char[] diagonal1 = new char[MomentumGame.BOARD_SIZE];
        char[] diagonal2 = new char[MomentumGame.BOARD_SIZE];
        for (int i = 0; i < MomentumGame.BOARD_SIZE; i++) {
            diagonal1[i] = board[i][i];
            diagonal2[i] = board[i][MomentumGame.BOARD_SIZE - 1 - i];
        }
        score += evaluateLine(diagonal1); // Evaluate diagonal
        score += evaluateLine(diagonal2); // Evaluate diagonal

        return score;
    }

    private char[] getColumn(char[][] board, int col) {
        char[] column = new char[MomentumGame.BOARD_SIZE];
        for (int i = 0; i < MomentumGame.BOARD_SIZE; i++) {
            column[i] = board[i][col];
        }
        return column;
    }

    private int evaluateLine(char[] line) {
        aiCount = 0;
        opponentCount = 0;

        int score = 0;

        for (int i = 0; i < line.length; i++) {
            char cell = line[i];

            if (cell == 'O') {
                aiCount++;
            } else if (cell == 'X') {
                opponentCount++;
            }

            // Check for potential winning configuration in all directions
            // Penalize moves closer to the border
            if (aiCount > 0 && opponentCount == 0 && i + 1 < line.length && line[i + 1] == '-') {
                return 100 - Math.abs(i - line.length / 2); // Adjust the penalty based on the distance from the center
            }

            // Add more criteria based on the current position
            if (i == 0 && cell == '-') {
                score += 10; // Favor moves closer to the beginning of the line
                if (isNearBorder(i)) {
                    score -= 5; // Penalize moves near the border
                }
            } else if (i == line.length - 1 && cell == '-') {
                score += 10; // Favor moves closer to the end of the line
                if (isNearBorder(i)) {
                    score -= 5; // Penalize moves near the border
                }
            }
        }

        return score;
    }

    private boolean isNearBorder(int position) {
        int borderThreshold = 2; // Adjust this threshold based on your preference
        return position < borderThreshold || position >= MomentumGame.BOARD_SIZE - borderThreshold;
    }

    public int getMarbleCounter() {
        return marbleCounter;
    }

    public void setMarbleCounter(int marbleCounter) {
        this.marbleCounter = marbleCounter;
    }

    public char getSymbol() {
        return symbol;
    }

    public void setSymbol(char symbol) {
        this.symbol = symbol;
    }
}