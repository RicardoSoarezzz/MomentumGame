import java.util.ArrayList;
import java.util.List;

class AIPlayer {

    Player aiPlayer;
    public AIPlayer(Player player2) {
        this.aiPlayer = player2;
    }

    public int[] getBestMove(char[][] board) {
        int[] bestMove = minimax(board, 4, Integer.MIN_VALUE, Integer.MAX_VALUE, true);
        return new int[]{bestMove[1], bestMove[2]}; // Return the column and row of the best move
    }

    private int[] minimax(char[][] board, int depth, int alpha, int beta, boolean maximizingPlayer) {
        List<int[]> possibleMoves = getEmptyCells(board);

        if (depth == 0 || possibleMoves.isEmpty()) {
            int evaluation = evaluateBoard(board);
            return new int[]{evaluation, -1, -1};
        }

        int[] bestMove = maximizingPlayer ? new int[]{Integer.MIN_VALUE, -1, -1} : new int[]{Integer.MAX_VALUE, -1, -1};

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

        // Evaluate rows
        for (int i = 0; i < MomentumGame.BOARD_SIZE; i++) {
            score += evaluateLine(board[i]);
        }

        // Evaluate columns
        for (int j = 0; j < MomentumGame.BOARD_SIZE; j++) {
            char[] column = new char[MomentumGame.BOARD_SIZE];
            for (int i = 0; i < MomentumGame.BOARD_SIZE; i++) {
                column[i] = board[i][j];
            }
            score += evaluateLine(column);
        }

        // Evaluate diagonals
        char[] diagonal1 = new char[MomentumGame.BOARD_SIZE];
        char[] diagonal2 = new char[MomentumGame.BOARD_SIZE];
        for (int i = 0; i < MomentumGame.BOARD_SIZE; i++) {
            diagonal1[i] = board[i][i];
            diagonal2[i] = board[i][MomentumGame.BOARD_SIZE - 1 - i];
        }
        score += evaluateLine(diagonal1);
        score += evaluateLine(diagonal2);

        return score;
    }

    private int evaluateLine(char[] line) {
        int aiCount = 0;
        int opponentCount = 0;

        for (char cell : line) {
            if (cell == 'O') {
                aiCount++;
            } else if (cell == 'X') {
                opponentCount++;
            }
        }

        if (aiCount == MomentumGame.WINNING_MARBLES) {
            return 1000;
        } else if (opponentCount == MomentumGame.WINNING_MARBLES) {
            return -1000;
        } else {
            return aiCount - opponentCount;
        }
    }

}
