
import java.util.ArrayList;
import java.util.Iterator;

public class GameMomentumAB extends NodeGameAB {
    private int[][] board = new int[7][7];
    private int myColor;

    public GameMomentumAB(String node) {
        super(1);
        this.myColor = getPlayer();
        this.processNode(node);
    }

    public GameMomentumAB(int[][] p, int myColor, int depth) {
        super(depth);

        for(int l = 0; l < 7; ++l) {
            for(int c = 0; c < 7; ++c) {
                this.board[l][c] = p[l][c];
            }
        }

        this.myColor = myColor;
    }

    public void processNode(String node) {
        String[] v = node.trim().split(" ");

        for(int l = 0; l < 7; ++l) {
            for(int c = 0; c < 7; ++c) {
                try {
                    this.board[l][c] = Integer.parseInt(v[l * 7 + c]);
                } catch (Exception var6) {
                    var6.printStackTrace();
                    System.out.println("board " + String.valueOf(v) + "  l " + l + "  c " + c);
                }
            }
        }

    }

    public ArrayList<Move> expandAB() {
        ArrayList<Move> suc = new ArrayList();
        return suc;
    }

    public double getH() {
        double h = 0.0;
        return h;
    }

    private int[][] makeCopy(int[][] p) {
        int[][] np = new int[7][7];

        for(int l = 0; l < 7; ++l) {
            for(int c = 0; c < 7; ++c) {
                np[l][c] = p[l][c];
            }
        }

        return np;
    }

    public void setMyColor(int color) {
        this.myColor = color;
    }

    public String toString() {
        String st = "";

        for(int l = 0; l < 7; ++l) {
            for(int c = 0; c < 7; ++c) {
                st = st + " " + (this.board[l][c] == 0 ? "." : "" + this.board[l][c]);
            }

            st = st + "\n";
        }

        st = st + "\n";
        return st;
    }

    public static void main(String[] args) {
        GameMomentumAB jogo = new GameMomentumAB("0 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0 1 0 0 0 0 0 0 0 2 2 2 2 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 ");
        jogo.setMyColor(1);
        ArrayList<Move> suc = jogo.expandAB();
        Iterator var4 = suc.iterator();

        while(var4.hasNext()) {
            Move j = (Move)var4.next();
            System.out.println(j);
        }

    }
}
