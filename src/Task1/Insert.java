package Task1;

import java.util.List;

public class Insert {
    private static final int m = 2;

    public Node<Rectanglable> chooseLeaf(Node<Rectanglable> root, Rectanglable rect) {
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

            N = selectedChild;
        }

        return N;
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

    public void pickSeeds(Node<Rectanglable> node1, Node<Rectanglable> node2) { //переделать
        double maxInefficiency = Double.MIN_VALUE;
        Rectanglable seed1 = null;
        Rectanglable seed2 = null;
        for (int i = 0; i < node1.rectanglables.size(); i++) {
            for (int j = i + 1; j < node1.rectanglables.size(); j++) {
                Rectanglable rect1 = node1.rectanglables.get(i);
                Rectanglable rect2 = node1.rectanglables.get(j);
                Rectangle combinedRectangle = node1.getBoundBox().getRectangle().combine(rect1.getRectangle(), rect2.getRectangle());
                double inefficiency = combinedRectangle.square() - rect1.getRectangle().square() - rect2.getRectangle().square();
                if (inefficiency > maxInefficiency) {
                    maxInefficiency = inefficiency;
                    seed1 = rect1;
                    seed2 = rect2;
                }
            }
        }
        if (seed1 != null && seed2 != null) {
            node1.rectanglables.remove(seed1);
            node1.rectanglables.remove(seed2);
            node1.addRectanglable(seed1);
            node2.addRectanglable(seed2);
        }
    }

    public int pickNext(List<Rectanglable> rectanglables, Node<Rectanglable> node1, Node<Rectanglable> node2) {
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
