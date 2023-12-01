package src;

import java.util.ArrayList;

public class GameMomentumAB extends NodeGameAB {

    private int[][] board = new int[7][7];
    private int myColor;

    public GameMomentumAB(String node) {
        super(1);
        myColor = getPlayer();
        processNode(node);
    }

    public GameMomentumAB(int[][] p, int myColor, int depth) {
        super(depth);
        for (int l = 0; l < 7; l++)
            for (int c = 0; c < 7; c++)
                this.board[l][c] = p[l][c];
        this.myColor = myColor;
    }

    public void processNode(String node) {
        String[] v = node.trim().split(" ");
        for (int l = 0; l < 7; l++)
            for (int c = 0; c < 7; c++)
                try {
                    board[l][c] = Integer.parseInt(v[l * 7 + c]);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    System.out.println("board " + v + "  l " + l + "  c " + c);
                }
    }

    public ArrayList<Move> expandAB() {
        ArrayList<Move> suc = new ArrayList<Move>();
        // TODO: Implement the logic to generate possible moves and their corresponding nodes
        // You need to populate the 'suc' ArrayList with Move objects based on the possible moves in the current game state.
        // Example:
         for (int i = 0; i < 7; i++) {
             for (int j = 0; j < 7; j++) {
                 if (board[i][j] == 0) {
                     int[][] newBoard = makeCopy(board);
                     newBoard[i][j] = myColor; // Assuming myColor represents the player's color
                     int one = i +1;
                     int two = j +1;
                     suc.add(new Move("(" + one + "," + two + ")", new GameMomentumAB(newBoard, myColor, getDepth() + 1)));
                 }
             }
         }
        return suc;
    }

    public double getH() {
        // TODO: Implement the evaluation function for the current game state
        // You need to calculate and return a heuristic value that represents the desirability of the current game state.
        // Example:
         int score = 0;
         for (int i = 0; i < 7; i++) {
             for (int j = 0; j < 7; j++) {
                 if (board[i][j] == myColor) {
                     // Adjust the score based on the position or any other relevant factors
                     score += 1;
                 } else if (board[i][j] != 0) {
                     // Adjust the score for opponent's pieces
                     score -= 1;}
             }}
         return score;

    }

    private int[][] makeCopy(int[][] p) {
        int[][] np = new int[7][7];
        for (int l = 0; l < 7; l++)
            for (int c = 0; c < 7; c++)
                np[l][c] = p[l][c];
        return np;
    }

    public void setMyColor(int color) {
        myColor = color;
    }

    public String toString() {
        String st = "";
        for (int l = 0; l < 7; l++) {
            for (int c = 0; c < 7; c++) {
                st += " " + (board[l][c] == 0 ? "." : "" + (board[l][c]));
            }
            st += "\n";
        }
        st += "\n";
        return st;
    }



}
