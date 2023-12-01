import java.util.Scanner;


public class MomentumGame {
    static final int BOARD_SIZE = 7;
    static final int WINNING_MARBLES = 8;


    int moveCounter;
    private char[][] board;
    char[][] newBoard;

    boolean gameFinish;
    Player player1,player2,currentPlayer;

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
            if(currentPlayer==player1){


               /* player1.getBestMove(board);
                if(isValidMove((player1.getBestMove(board).getRow()+1), (player1.getBestMove(board).getCol()+1))); {
                    System.out.println("AI 1 plays: "+(player1.getBestMove(board).getRow()+1) + (player1.getBestMove(board).getCol()+1));
                    pushMarbles((player1.getBestMove(board).getRow()+1), (player1.getBestMove(board).getCol()+1));
                    //moveCounter++;
                    if(checkWin() || moveCounter ==30){
                        gameFinish = true;
                    }
                    printBoard();
                    changePlayer();
                }

                 Player Vs AI*/
                System.out.println("\nPlayer " + currentPlayer.getSymbol() + "'s turn.");
                System.out.println("Column (1-7) ");
                int col = scanner.nextInt();
                System.out.println("Row (1-7) ");
                int row = scanner.nextInt();
                if(isValidMove(col,row)){
                    moveCounter++;
                    pushMarbles(col, row);
                    if(checkWin() || moveCounter ==30){
                        gameFinish = true;
                    }
                    printBoard();
                    changePlayer();

                }else {
                    System.out.println("Invalid move. Please try again.");
                }
            } else {

                player2.getBestMove(board);


                if(isValidMove((player1.getBestMove(board).getRow()+1), (player1.getBestMove(board).getCol()+1))); {
                    System.out.println("AI 1 plays: "+(player1.getBestMove(board).getRow()+1) + (player1.getBestMove(board).getCol()+1));
                    pushMarbles((player1.getBestMove(board).getRow()+1), (player1.getBestMove(board).getCol()+1));
                    //moveCounter++;
                    if(checkWin() || moveCounter ==30){
                        gameFinish = true;
                    }
                    printBoard();
                    changePlayer();
                }

            }
        }
        System.out.println("________________\nFinal Board\n");
        printBoard();
        if(moveCounter==30){
            System.out.println("\nThe game is a draw!");
        }else {
            System.out.println("\nPlayer "+currentPlayer.getSymbol()+" Wins!!");
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

    private boolean isValidMove(int col, int row) {
        if(col <1 || row < 1|| col> 7 || row >7){
            return false;
        }
        return board[row - 1][col - 1] == '-';

    }

    private void pushMarbles(int col, int row) {
        col = col - 1;
        row = row - 1;

        newBoard = new char[BOARD_SIZE][BOARD_SIZE];
        char temp = currentPlayer.getSymbol();

        for (int i = 0; i < BOARD_SIZE; i++) {
            System.arraycopy(board[i], 0, newBoard[i], 0, BOARD_SIZE);
        }

        // Mark the center as visited to avoid pushing marbles back to the center
        boolean[][] visited = new boolean[BOARD_SIZE][BOARD_SIZE];
        visited[row][col] = true;

        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i == 0 && j == 0) {
                    newBoard[row][col] = temp;
                } else {
                    pushMarblesInDirection(row, col, i, j, visited);
                }
            }
        }

        board = newBoard;
    }

    private void pushMarblesInDirection(int row, int col, int rowDirection, int colDirection, boolean[][] visited) {
        char temp = currentPlayer.getSymbol();

        for (int k = 1; k <= 6; k++) {
            int currentRow = row + rowDirection * k;
            int currentCol = col + colDirection * k;

            if (currentRow >= 0 && currentRow < BOARD_SIZE && currentCol >= 0 && currentCol < BOARD_SIZE && !visited[currentRow][currentCol]) {
                if (k == 1) {
                    newBoard[currentRow][currentCol] = '-';
                } else {
                    if (currentRow - rowDirection != row || currentCol - colDirection != col) {
                        newBoard[currentRow][currentCol] = board[currentRow - rowDirection][currentCol - colDirection];
                        visited[currentRow][currentCol] = true;
                    }
                }
            }
        }
    }



    private boolean checkWin() {

        int counter = 0;
        for (int i = 0; i < BOARD_SIZE; i++) {

            for (int j = 0; j < BOARD_SIZE; j++) {
                if(board[i][j]==currentPlayer.getSymbol()){
                    counter++;
                }
            }

        }
        currentPlayer.setMarbleCounter(counter);

        return currentPlayer.getMarbleCounter() == WINNING_MARBLES;
    }

    private void initializeBoard(char[][]newBoard) {
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
            System.out.print(rowCount+" ");
            for (int j = 0; j < BOARD_SIZE; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
    }




    public char[][] getBoard() {
        return board;
    }

    public void setBoard(char[][] board) {
        this.board = board;
    }


}


