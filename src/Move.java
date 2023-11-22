public class Move {
    private String action;
    private NodeGameAB node;

    public Move(String action, NodeGameAB node) {
        this.action = action;
        this.node = node;
    }

    public String getAction() {
        return this.action;
    }

    public NodeGameAB getNode() {
        return this.node;
    }

    public String toString() {
        String var10000 = this.action;
        return "Move: " + var10000 + "\n" + this.node.toString();
    }
}
