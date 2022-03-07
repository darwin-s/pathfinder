package org.darwin;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Class representing the rectangles, from which the "board" is made.
 * A rectangle is the smallest unit in this application. The color of the
 * rectangle is determined by it's type (Blank - white, Wall - black, Start - green,
 * End - red and Path - blue).
 */
public class Rect {
    // Size of a rectangle in pixels
    public static final int SIZE = 32;

    public enum Type {
        BLANK,
        WALL,
        START,
        END,
        PATH
    }

    private int x;
    private int y;
    private Type type;
    private Rectangle shape;

    public Rect() {
        this.x = 0;
        this.y = 0;
        this.type = Type.BLANK;

        initShape();
    }

    public Rect(int x, int y) {
        this.x = x;
        this.y = y;
        this.type = Type.BLANK;

        initShape();
    }

    public Rect(int x, int y, Type type) {
        this.x = x;
        this.y = y;
        this.type = type;

        initShape();
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Type getType() {
        return type;
    }

    /**
     * Set type and update color
     * @param type New type of the rectangle
     */
    public void setType(Type type) {
        this.type = type;
        updateColor();
    }

    public Rectangle getShape() {
        return shape;
    }

    /**
     * Initialize the underlying shape of the rectangle.
     * Set the position and size of the shape.
     */
    private void initShape() {
        shape = new Rectangle();

        shape.setX(SIZE * x);
        shape.setY(SIZE * y);
        shape.setWidth(SIZE);
        shape.setHeight(SIZE);

        updateColor();
    }

    /**
     * Update fill color based on the rectangle type.
     */
    private void updateColor() {
        switch (type) {
            case BLANK:
                shape.setFill(Color.WHITE);
                break;
            case WALL:
                shape.setFill(Color.BLACK);
                break;
            case START:
                shape.setFill(Color.GREEN);
                break;
            case END:
                shape.setFill(Color.RED);
                break;
            case PATH:
                shape.setFill(Color.BLUE);
                break;
        }
    }
}
