import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import javax.swing.JTextField;

public abstract class NodeGameAB {
    private static int player;
    private static int maxDepth;
    private static final int LIMIT_TIME = 50;
    public static int VICTORY = 1000000;
    public static int DEFEAT = -1000000;
    private Move bestMove = null;
    private static Date startTime;
    private int depth;

    public NodeGameAB(int depth) {
        this.depth = depth;
    }

    public abstract ArrayList<Move> expandAB();

    public abstract double getH();

    public static int getMaxDepth() {
        return maxDepth;
    }

    public Move getBestMove() {
        return this.bestMove;
    }

    public int getDepth() {
        return this.depth;
    }

    protected static void setPlayer(String st) {
        player = 0;

        try {
            player = Integer.parseInt(st);
        } catch (Exception var2) {
            var2.printStackTrace();
        }
    }

    protected static int getPlayer() {
        return player;
    }

    public int getSeconds() {
        return (int)((new Date()).getTime() - startTime.getTime()) / 1000;
    }

    public String processAB(JTextField tf) {
        ArrayList<Move> suc = expandAB();
        double largest = DEFEAT - 1;
        bestMove = null;
        maxDepth = 5;
        startTime = new Date();
        while (getSeconds() < LIMIT_TIME && maxDepth < 50 && largest < VICTORY) {
            Move bestOfDepth = null;
            largest = DEFEAT - 1;
            for (Move candidate : suc) {
                double vMin = candidate.getNode().minValue(-99999999, 99999999);
                if (vMin > largest || (vMin == largest && Math.random() > 0.5)) {
                    largest = vMin;
                    bestOfDepth = candidate;
                    if (tf != null)
                        tf.setText("Depth:" + maxDepth + "  " + getSeconds() + "s  " + largest + " : " + bestOfDepth.getAction());
                    else
                        System.out.println("Depth:" + maxDepth + "  " + getSeconds() + "s  " + largest + " : " + bestOfDepth.getAction());
                }
            }
            maxDepth++;
            if (bestOfDepth != null) {
                bestMove = bestOfDepth;
                if (tf != null)
                    tf.setText("Depth:" + maxDepth + "  " + getSeconds() + "s  " + largest + " : " + bestMove.getAction());
                else
                    System.out.println("Depth:" + maxDepth + "  " + getSeconds() + "s  " + largest + " : " + bestMove.getAction());
            }

            tf.repaint();
        }

        if (bestMove != null)
            return player + " " + bestMove.getAction();
        else
            return player + " 4 4";

    }

    public double maxValue(double alfa, double beta) {
        if (this.depth < maxDepth && this.getSeconds() <= LIMIT_TIME) {
            ArrayList<Move> suc = this.expandAB();
            if (suc.size() == 0) {
                return this.getH();
            } else {
                Iterator var7 = suc.iterator();

                do {
                    if (!var7.hasNext()) {
                        return alfa;
                    }

                    Move cand = (Move) var7.next();
                    double vMin = cand.getNode().minValue(alfa, beta);
                    if (vMin > alfa) {
                        alfa = vMin;
                    }
                } while (!(alfa >= beta));

                return beta;
            }
        } else {
            return this.getH();
        }
    }

    public double minValue(double alfa, double beta) {
        if (this.depth < maxDepth && this.getSeconds() <= LIMIT_TIME) {
            ArrayList<Move> suc = this.expandAB();
            if (suc.size() == 0) {
                return this.getH();
            } else {
                Iterator var7 = suc.iterator();

                do {
                    if (!var7.hasNext()) {
                        return beta;
                    }

                    Move cand = (Move) var7.next();
                    double vMax = cand.getNode().maxValue(alfa, beta);
                    if (vMax < beta) {
                        beta = vMax;
                    }
                } while (!(beta <= alfa));

                return alfa;
            }
        } else {
            return this.getH();
        }
    }
}
