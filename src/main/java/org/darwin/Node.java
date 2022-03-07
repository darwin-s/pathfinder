package org.darwin;

/**
 * Node class. Used to represent the path during the BFS search. It holds a rectangle
 * associated with this node and a parent, so we can traverse the path from end to beginning.
 */
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
