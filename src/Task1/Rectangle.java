package Task1;

public class Rectangle implements Rectanglable {
    private final double x1, y1, x2, y2;

    public Rectangle(double x1, double y1, double x2, double y2) {
        this.x1 = Math.min(x1, x2);
        this.y1 = Math.min(y1, y2);
        this.x2 = Math.max(x1, x2);
        this.y2 = Math.max(y1, y2);
    }

    public boolean intersects(Rectangle otherRectangle) {
        return x1 < otherRectangle.x2 && x2 > otherRectangle.x1 && y1 < otherRectangle.y2 && y2 > otherRectangle.y1;
    }

    public double square() {
        return (x2 - x1) * (y2 - y1);
    }

    public double calculateDifference(Rectangle otherRectangle) {
        double currentSquare = square();
        double otherSquare = otherRectangle.square();
        return Math.abs(currentSquare - otherSquare);
    }

    public double enlargement(Rectangle other) {
        return combine(this, other).square() - this.square();
    }

    public static Rectangle combine(Rectangle rect1, Rectangle rect2) {
        double minX = Math.min(rect1.x1, rect2.x1);
        double minY = Math.min(rect1.y1, rect2.y1);
        double maxX = Math.max(rect1.x2, rect2.x2);
        double maxY = Math.max(rect1.y2, rect2.y2);

        return new Rectangle(minX, minY, maxX, maxY);
    }

    @Override
    public Rectangle getRectangle() {
        return this;
    }

    @Override
    public void display() {
        System.out.println("Rectangle: (" + x1 + ", " + y1 + ")  (" + x2 + ", " + y2 + ")");
    }
}