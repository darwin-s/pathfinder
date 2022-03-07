package org.darwin;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Rect {
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

    public void setType(Type type) {
        this.type = type;
        updateColor();
    }

    public Rectangle getShape() {
        return shape;
    }

    private void initShape() {
        shape = new Rectangle();

        shape.setX(SIZE * x);
        shape.setY(SIZE * y);
        shape.setWidth(SIZE);
        shape.setHeight(SIZE);

        updateColor();
    }

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
