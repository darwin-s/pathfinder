package org.darwin;

public class Node {
    private Node parent;
    private Rect rect;

    public Node(Rect rect, Node parent) {
        this.rect = rect;
        this.parent = parent;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public Rect getRect() {
        return rect;
    }

    public void setRect(Rect rect) {
        this.rect = rect;
    }
}
