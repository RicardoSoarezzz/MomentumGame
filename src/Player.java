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

    public Action getBestMove(char[][] board) {
        Node bestNode;

        do {
            bestNode = minimax(new Node(board, 0, null), MAX_DEPTH, Integer.MIN_VALUE, Integer.MAX_VALUE, true);
        } while (!isValidMove(board, bestNode.getAction().getRow(), bestNode.getAction().getCol()));

        return bestNode.getAction();
    }

    private boolean isValidMove(char[][] board, int row, int col) {
        return row >= 0 && col >= 0 && row < MomentumGame.BOARD_SIZE && col < MomentumGame.BOARD_SIZE && board[row][col] == '-';
    }

    private Node minimax(Node node, int depth, int alpha, int beta, boolean maximizingPlayer) {
        List<Action> possibleMoves = getEmptyCells(node.getBoard());
        if (possibleMoves.size() == MomentumGame.BOARD_SIZE * MomentumGame.BOARD_SIZE) {
            return new Node(node.getBoard(), 1, new Action(3, 3));
        }

        if (depth == 0 || possibleMoves.isEmpty()) {
            int evaluation = evaluateBoard(node.getBoard());
            return new Node(node.getBoard(), evaluation, null);
        }

        Node bestNode = maximizingPlayer ? new Node(node.getBoard(), Integer.MIN_VALUE, null) : new Node(node.getBoard(), Integer.MAX_VALUE, null);


        possibleMoves.sort((m1, m2) -> {
            node.getBoard()[m1.getRow()][m1.getCol()] = maximizingPlayer ? 'O' : 'X';
            int eval1 = evaluateBoard(node.getBoard());
            node.getBoard()[m1.getRow()][m1.getCol()] = '-';

            node.getBoard()[m2.getRow()][m2.getCol()] = maximizingPlayer ? 'O' : 'X';
            int eval2 = evaluateBoard(node.getBoard());
            node.getBoard()[m2.getRow()][m2.getCol()] = '-';

            return maximizingPlayer ? Integer.compare(eval2, eval1) : Integer.compare(eval1, eval2);
        });

        for (Action move : possibleMoves) {
            int row = move.getRow();
            int col = move.getCol();

            char originalValue = node.getBoard()[row][col];
            node.getBoard()[row][col] = maximizingPlayer ? 'O' : 'X';

            Node childNode = minimax(new Node(node.getBoard(), 0, null), depth - 1, alpha, beta, !maximizingPlayer);
            node.getBoard()[row][col] = originalValue;

            if (maximizingPlayer) {
                if (childNode.getScore() > bestNode.getScore()) {
                    bestNode = new Node(node.getBoard(), childNode.getScore(), move);
                }
                alpha = Math.max(alpha, bestNode.getScore());
            } else {
                if (childNode.getScore() < bestNode.getScore()) {
                    bestNode = new Node(node.getBoard(), childNode.getScore(), move);
                }
                beta = Math.min(beta, bestNode.getScore());
            }

            if (beta <= alpha) {
                break;
            }
        }

        return bestNode;
    }

    private List<Action> getEmptyCells(char[][] board) {
        List<Action> emptyCells = new ArrayList<>();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] == '-') {
                    emptyCells.add(new Action(i, j));
                }
            }
        }
        return emptyCells;
    }

    private int evaluateBoard(char[][] board) {
        int score = 0;


        for (int i = 0; i < MomentumGame.BOARD_SIZE; i++) {
            score += evaluateLine(board[i]);
            score += evaluateLine(getColumn(board, i));
        }

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


            if (aiCount > 0 && opponentCount == 0 && i + 1 < line.length && line[i + 1] == '-') {
                return 100 - Math.abs(i - line.length / 2);
            }


            if (i == 0 && cell == '-') {
                score += 10;
                if (isNearBorder(i)) {
                    score -= 5;
                }
            } else if (i == line.length - 1 && cell == '-') {
                score += 10;
                if (isNearBorder(i)) {
                    score -= 5;
                }
            }
        }

        return score;
    }

    private boolean isNearBorder(int position) {
        int borderThreshold = 2;
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