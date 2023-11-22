import java.util.Scanner;

public class MomentumGame {
    static final int BOARD_SIZE = 7;
    static final int WINNING_MARBLES = 8;

    int moveCounter;
    private char[][] board;
    char[][] newBoard;

    boolean gameFinish;
    Player player1, player2, currentPlayer;

    public static void main(String[] args) {
        MomentumGame game = new MomentumGame();
    }

    public MomentumGame() {
        board = new char[BOARD_SIZE][BOARD_SIZE];
        player1 = new Player('X');
        player2 = new Player('O');
        currentPlayer = player1;
        moveCounter = 0;
        gameFinish = false;
        initializeBoard(board);

        play();
    }

    public void play() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Game Started\n________________");
        printBoard();
        while (!gameFinish) {
            if (currentPlayer == player1) {
                player1.getBestMove(board);
                Action bestMove = player1.getBestMove(board);

                if (isValidMove(bestMove.getRow() + 1, bestMove.getCol() + 1)) {
                    System.out.println("AI 1 plays: " + (bestMove.getRow() + 1) + "," + (bestMove.getCol() + 1));
                    pushMarbles(bestMove.getRow() + 1, bestMove.getCol() + 1);

                    if (checkWin() || moveCounter == 30) {
                        gameFinish = true;
                    }
                    printBoard();
                    changePlayer();
                }
            } else {
                player2.getBestMove(board);
                Action bestMove = player2.getBestMove(board);

                if (isValidMove(bestMove.getRow() + 1, bestMove.getCol() + 1)) {
                    System.out.println("\nAI plays: " + (bestMove.getRow() + 1) + "," + (bestMove.getCol() + 1));
                    pushMarbles(bestMove.getRow() + 1, bestMove.getCol() + 1);

                    if (checkWin() || moveCounter == 30) {
                        gameFinish = true;
                    }
                    printBoard();
                    changePlayer();
                }
            }
        }
        System.out.println("________________\nFinal Board\n");
        printBoard();
        if (moveCounter == 30) {
            System.out.println("\nThe game is a draw!");
        } else {
            System.out.println("\nPlayer " + currentPlayer.getSymbol() + " Wins!!");
        }
        System.out.println("Game Ended!");
    }

    private void changePlayer() {
        if (currentPlayer == player1) {
            currentPlayer = player2;
        } else {
            currentPlayer = player1;
        }
    }

    private boolean isValidMove(int row, int col) {
        if (row < 1 || col < 1 || row > BOARD_SIZE || col > BOARD_SIZE) {
            return false;
        }

        return board[row-1][col-1] == '-';
    }

    private void pushMarbles(int col, int row) {
        int newRow = row - 1;
        int newCol = col - 1;

        newBoard = new char[BOARD_SIZE][BOARD_SIZE];
        char temp = currentPlayer.getSymbol();

        for (int i = 0; i < BOARD_SIZE; i++) {
            System.arraycopy(board[i], 0, newBoard[i], 0, BOARD_SIZE);
        }

        boolean[][] visited = new boolean[BOARD_SIZE][BOARD_SIZE];
        visited[newRow][newCol] = true;

        newBoard[newRow][newCol] = temp;

        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i == 0 && j == 0) {
                }
                else if((newRow+i)<BOARD_SIZE && (newRow +i) >= 0 && (newCol+j) >=0 && (newCol+j) <BOARD_SIZE) {
                    if(board[newRow+i][newCol+j]!='-'){
                        pushMarblesInDirection(newRow, newCol, i, j, visited);
                    }
                }
            }
        }

        board = newBoard;
    }

    private void pushMarblesInDirection(int row, int col, int rowDirection, int colDirection, boolean[][] visited) {
        int emptyRow = 0;
        int emptyCol = 0;
        int currentRow = 0;
        int currentCol = 0;
        for (int k = 1; k <= 6; k++) {
            currentRow = row + rowDirection * k;
            currentCol = col + colDirection * k;


            if (currentRow >= 0 && currentRow < BOARD_SIZE && currentCol >= 0 && currentCol < BOARD_SIZE && !visited[currentRow][currentCol]) {
                if(board[currentRow][currentCol]=='-'){
                    emptyRow = currentRow;
                    emptyCol = currentCol;
                    break;
                }
            }
        }
        if (emptyRow ==0){
            if (emptyRow - rowDirection != row || emptyCol - colDirection != col) {
                newBoard[emptyRow - rowDirection][emptyCol - colDirection] = '-';
                newBoard[emptyRow][emptyCol] = board[emptyRow - rowDirection][emptyCol - colDirection];
                visited[currentRow][currentCol] = true;
            }
        }
    }

    /*private void pushMarbles(int row, int col) {
        row = row - 1;
        col = col - 1;

        newBoard = new char[BOARD_SIZE][BOARD_SIZE];
        char temp = currentPlayer.getSymbol();

        for (int i = 0; i < BOARD_SIZE; i++) {
            System.arraycopy(board[i], 0, newBoard[i], 0, BOARD_SIZE);
        }

        boolean[][] visited = new boolean[BOARD_SIZE][BOARD_SIZE];
        visited[row][col] = true;

        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i == 0 && j == 0) {
                    newBoard[row][col] = temp;
                }

                for (int k = 1; k <= 6; k++) {
                    int currentRow = row + i * k;
                    int currentCol = col + j * k;

                    if (currentRow >= 0 && currentRow < BOARD_SIZE && currentCol >= 0 && currentCol < BOARD_SIZE && !visited[currentRow][currentCol]) {
                        if (k == 1) {
                            newBoard[currentRow][currentCol] = '-';
                        } else {
                            if (currentRow - i != row || currentCol - j != col) {
                                newBoard[currentRow][currentCol] = board[currentRow - i][currentCol - j];
                                visited[currentRow][currentCol] = true;
                            }
                        }
                    }
                }
            }
        }

        board = newBoard;
        moveCounter++;
    }*/

    private boolean checkWin() {
        int counter = 0;
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (board[i][j] == currentPlayer.getSymbol()) {
                    counter++;
                }
            }
        }

        currentPlayer.setMarbleCounter(counter);

        return currentPlayer.getMarbleCounter() == WINNING_MARBLES;
    }

    private void initializeBoard(char[][] newBoard) {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                newBoard[i][j] = '-';
            }
        }
    }

    private void printBoard() {
        int rowCount = 0;
        System.out.println("  1 2 3 4 5 6 7");
        for (int i = 0; i < BOARD_SIZE; i++) {
            rowCount++;
            System.out.print(rowCount + " ");
            for (int j = 0; j < BOARD_SIZE; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.print(rowCount);
            System.out.println();
        }
        System.out.println("  1 2 3 4 5 6 7");
    }

    public char[][] getBoard() {
        return board;
    }

    public void setBoard(char[][] board) {
        this.board = board;
    }
}
