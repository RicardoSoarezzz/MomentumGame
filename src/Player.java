class Player {

    private int marbleCounter;
    private char symbol;
    Player(char symbol){
        marbleCounter = 0;
        this.symbol = symbol;
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