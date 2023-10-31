package Task1;

public class NodeAsRectanglable implements Rectanglable {
    private final Node<Rectanglable> node;

    public NodeAsRectanglable(Node<Rectanglable> node) {
        this.node = node;
    }

    @Override
    public Rectangle getRectangle() {
        return node.getBoundBox();
    }
}


