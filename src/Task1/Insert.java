package Task1;

import java.util.List;
import java.util.Stack;

public class Insert {
    private static final int m = 2;

    public Stack<Node<Rectanglable>> chooseLeaf(Node<Rectanglable> root, Rectanglable rect) {
        Stack<Node<Rectanglable>> leafStack = new Stack<>();
        Node<Rectanglable> N = root;

        while (!N.isLeaf()) {
            double minDelta = Double.MAX_VALUE;
            Node<Rectanglable> selectedChild = null;

            for (Rectanglable child : N.rectanglables) {
                Rectangle childRect = child.getRectangle();
                Rectangle deltaRect = childRect.combine(childRect, rect.getRectangle());
                double delta = deltaRect.square() - childRect.square();

                if (delta < minDelta) {
                    minDelta = delta;
                    selectedChild = (Node<Rectanglable>) child;
                } else if (delta == minDelta) {
                    if (childRect.square() < selectedChild.getBoundBox().square()) {
                        selectedChild = (Node<Rectanglable>) child;
                    }
                }
            }

            leafStack.push(N);
            N = selectedChild;
        }

        leafStack.push(N);

        return leafStack;
    }

    public void quadraticSplit(Node<Rectanglable> node1, Node<Rectanglable> node2) {
        pickSeeds(node1, node2);
        while (!node1.rectanglables.isEmpty() && !node2.rectanglables.isEmpty()) {
            pickNext(node1, node2);
        }
        if (!node1.rectanglables.isEmpty()) {
            while (node1.rectanglables.size() < m) {
                pickNext(node1, node2);
            }
        } else if (!node2.rectanglables.isEmpty()) {
            while (node2.rectanglables.size() < m) {
                pickNext(node1, node2);
            }
        }
    }

    public int[] pickSeeds(List<Rectanglable> rectanglables) {
        double maxInefficiency = Double.MIN_VALUE;
        int seedIndex1 = -1;
        int seedIndex2 = -1;

        for (int i = 0; i < rectanglables.size(); i++) {
            for (int j = i + 1; j < rectanglables.size(); j++) {
                Rectanglable rect1 = rectanglables.get(i);
                Rectanglable rect2 = rectanglables.get(j);
                Rectangle combinedRectangle = combine(rect1.getRectangle(), rect2.getRectangle());
                double inefficiency = combinedRectangle.square() - rect1.getRectangle().square() - rect2.getRectangle().square();
                if (inefficiency > maxInefficiency) {
                    maxInefficiency = inefficiency;
                    seedIndex1 = i;
                    seedIndex2 = j;
                }
            }
        }

        return new int[] { seedIndex1, seedIndex2 };
    }


    public int pickNext(List<Rectanglable> rectanglables, Node<Rectanglable> node1, Node<Rectanglable> node2) { // по поводу node вопросы?????
        double maxDifference = Double.MIN_VALUE;
        int selectedIndex = -1;

        for (int i = 0; i < rectanglables.size(); i++) {
            Rectanglable rectanglable = rectanglables.get(i);
            double d1 = node1.getBoundBox().calculateDifference(rectanglable.getRectangle());
            double d2 = node2.getBoundBox().calculateDifference(rectanglable.getRectangle());
            double difference = Math.abs(d1 - d2);

            if (difference > maxDifference) {
                maxDifference = difference;
                selectedIndex = i;
            }
        }

        if (selectedIndex != -1) {
            Rectanglable selectedRectanglable = rectanglables.get(selectedIndex);
            rectanglables.remove(selectedIndex);

            if (node1.getBoundBox().calculateDifference(selectedRectanglable.getRectangle()) <=
                    node2.getBoundBox().calculateDifference(selectedRectanglable.getRectangle())) {
                node1.addRectanglable(selectedRectanglable);
            } else {
                node2.addRectanglable(selectedRectanglable);
            }
        }

        return selectedIndex;
    }
}
