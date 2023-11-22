import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import javax.swing.JTextField;

public abstract class NodeGameAB {
    private static int player;
    private static int maxDepth;
    private static int LIMIT_TIME = 5;
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
        ArrayList<Move> suc = this.expandAB();
        double largest = (double)(DEFEAT - 1);
        this.bestMove = null;
        maxDepth = 5;
        startTime = new Date();

        label49:
        while(this.getSeconds() < LIMIT_TIME && maxDepth < 50 && largest < (double)VICTORY) {
            Move bestOfDepth = null;
            largest = (double)(DEFEAT - 1);
            Iterator var7 = suc.iterator();

            while(true) {
                int var10001;
                Move candidate;
                double vMin;
                do {
                    if (!var7.hasNext()) {
                        ++maxDepth;
                        if (bestOfDepth != null) {
                            this.bestMove = bestOfDepth;
                            if (tf != null) {
                                var10001 = maxDepth;
                                tf.setText("Depth:" + var10001 + "  " + this.getSeconds() + "s  " + largest + " : " + this.bestMove.getAction());
                            } else {
                                var10001 = maxDepth;
                                System.out.println("Depth:" + var10001 + "  " + this.getSeconds() + "s  " + largest + " : " + this.bestMove.getAction());
                            }
                        }

                        tf.repaint();
                        continue label49;
                    }

                    candidate = (Move)var7.next();
                    vMin = candidate.getNode().minValue(-9.9999999E7, 9.9999999E7);
                } while(!(vMin > largest) && (vMin != largest || !this.maybe()));

                largest = vMin;
                bestOfDepth = candidate;
                if (tf != null) {
                    var10001 = maxDepth;
                    tf.setText("Depth:" + var10001 + "  " + this.getSeconds() + "s  " + vMin + " : " + candidate.getAction());
                } else {
                    var10001 = maxDepth;
                    System.out.println("Depth:" + var10001 + "  " + this.getSeconds() + "s  " + vMin + " : " + candidate.getAction());
                }
            }
        }

        return "2 2";
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

                    Move cand = (Move)var7.next();
                    double vMin = cand.getNode().minValue(alfa, beta);
                    if (vMin > alfa) {
                        alfa = vMin;
                    }
                } while(!(alfa >= beta));

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

                    Move cand = (Move)var7.next();
                    double vMax = cand.getNode().maxValue(alfa, beta);
                    if (vMax < beta) {
                        beta = vMax;
                    }
                } while(!(beta <= alfa));

                return alfa;
            }
        } else {
            return this.getH();
        }
    }

    private boolean maybe() {
        return Math.random() > 0.5;
    }
}
