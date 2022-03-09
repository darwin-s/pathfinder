package org.darwin;

import java.awt.*;

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
    private Color fillColor;

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
     * Draw the rectangle
     * @param g Graphics context
     */
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(fillColor);
        g2d.fill(shape);
    }

    /**
     * Initialize the underlying shape of the rectangle.
     * Set the position and size of the shape.
     */
    private void initShape() {
        shape = new Rectangle();

        shape.x = SIZE * x;
        shape.y = SIZE * y;
        shape.width = SIZE;
        shape.height = SIZE;

        updateColor();
    }

    /**
     * Update fill color based on the rectangle type.
     */
    private void updateColor() {
        switch (type) {
            case BLANK:
                fillColor = Color.WHITE;
                break;
            case WALL:
                fillColor = Color.BLACK;
                break;
            case START:
                fillColor = Color.GREEN;
                break;
            case END:
                fillColor = Color.RED;
                break;
            case PATH:
                fillColor = Color.BLUE;
                break;
        }
    }
}
