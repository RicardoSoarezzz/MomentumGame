class Node {
    private char[][] board;
    private int score;
    private Action action;

    public Node(char[][] board, int score, Action action) {
        this.board = board;
        this.score = score;
        this.action = action;
    }

    public char[][] getBoard() {
        return board;
    }

    public int getScore() {
        return score;
    }

    public Action getAction() {
        return action;
    }
}
