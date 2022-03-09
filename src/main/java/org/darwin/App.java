package org.darwin;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.LinkedList;

public class App {
    private static final int SIZE = 20;
    private static final int WINDOW_SIZE = 640;
    private static Rect[][] rects;
    private static Rect start;
    private static Rect end;
    private static boolean erasing = false;

    /**
     * Find path from start to end point, using BFS.
     */
    private static void findPath() {
        // Keep track of visited nodes
        boolean[][] visited = new boolean[SIZE][SIZE];

        // Set the array to false and clear the board from the old path result (if any)
        for (int y = 0; y < SIZE; y++) {
            for (int x = 0; x < SIZE; x++) {
                visited[y][x] = false;

                if (rects[y][x].getType() == Rect.Type.PATH) {
                    rects[y][x].setType(Rect.Type.BLANK);
                }
            }
        }

        // Set starting node as visited
        visited[start.getY()][start.getX()] = true;

        // Create a new queue and add the starting node to it
        LinkedList<Node> queue = new LinkedList<>();
        Node startNode = new Node(start, null);
        queue.add(startNode);

        // The ending node may remain null if no path was found
        Node endNode = null;

        while (!queue.isEmpty()) {
            // Get the current node and rectangle associated with it
            Node currentNode = queue.pop();
            Rect currentRect = currentNode.getRect();
            final int currentX = currentRect.getX();
            final int currentY = currentRect.getY();

            // Offsets of neighbouring nodes
            final int DIRECTION_NUM = 4;
            final int[] xDirections = {-1, 1, 0, 0};
            final int[] yDirections = {0, 0, -1, 1};

            for (int i = 0; i < DIRECTION_NUM; i++) {
                // Calculate new position, relative to the current node
                final int newY = currentY + yDirections[i];
                final int newX = currentX + xDirections[i];

                // New position may be out of bounds,
                // so we skip the current iteration if it is so
                if (newX < 0 || newX >= SIZE || newY < 0 || newY >= SIZE) {
                    continue;
                }

                // If the node at the new position is not visited and it is "walkable"
                // i.e it is not a wall, then mark it as visited and add it to the queue
                // unless we found the end node, in which case we exit the loop
                if (!visited[newY][newX] && rects[newY][newX].getType() != Rect.Type.WALL) {
                    visited[newY][newX] = true;

                    Node newChild = new Node(rects[newY][newX], currentNode);

                    if (end == rects[newY][newX]) {
                        endNode = newChild;
                        break;
                    }

                    queue.add(newChild);
                }
            }
        }

        // If we found a path, then draw it
        if (endNode != null) {
            Node currentNode = endNode.getParent();

            while (currentNode != startNode) {
                currentNode.getRect().setType(Rect.Type.PATH);

                currentNode = currentNode.getParent();
            }
        }
    }

    /**
     * Initialize rectangle object.
     *
     * This method creates a new rectangle object and adds it to the drawing group.
     * The method also sets the rectangle's special type for the starting and end point.
     * @param x Rectangle X position
     * @param y Rectangle Y position
     */
    private static void initRect(int x, int y) {
        // Process out of bounds
        if (x < 0 || x >= SIZE || y < 0 || y >= SIZE) {
            return;
        }

        rects[y][x] = new Rect(x, y);

        // Set special type for start and end points
        if (x == 0 && y == SIZE - 1) {
            rects[y][x].setType(Rect.Type.START);
            start = rects[y][x];
        } else if (x == SIZE - 1 && y == 0) {
            rects[y][x].setType(Rect.Type.END);
            end = rects[y][x];
        }
    }

    public static void main(String[] args) {
        rects = new Rect[SIZE][SIZE];

        JFrame window = new JFrame("Pathfinder");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.getContentPane().add(new RectCanvas(rects));
        window.getContentPane().setPreferredSize(new Dimension(WINDOW_SIZE, WINDOW_SIZE));
        window.pack();

        for (int y = 0; y < SIZE; y++) {
            for (int x = 0; x < SIZE; x++) {
                initRect(x, y);
            }
        }

        // Find initial path
        findPath();

        // Set up event handling
        window.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);

                handleMouseEvent(e, window, rects, true);
            }
        });

        window.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);

                handleMouseEvent(e, window, rects, false);
            }
        });

        window.setResizable(false);
        window.setVisible(true);
    }

    /**
     * Handle mouse events
     * @param e Mouse event
     * @param window Current window
     * @param rects Rectangles that can be affected
     * @param drag Specifies if current event is a mouse drag
     */
    private static void handleMouseEvent(MouseEvent e, JFrame window, Rect[][] rects, boolean drag) {
        final int x1 = e.getX() / Rect.SIZE;
        final int y1 = e.getY() / Rect.SIZE - 1;

        if (x1 < 0 || x1 >= SIZE || y1 < 0 || y1 >= SIZE) {
            return;
        }

        if (drag) {
            handleDrag(window, rects, x1, y1);
        } else {
            handleClick(e, window, rects, x1, y1);
        }
    }

    /**
     * Handle mouse clicks
     * @param e Mouse event
     * @param window Current window
     * @param rects Rectangles that can be affected
     * @param x1 Click X position
     * @param y1 Click Y position
     */
    private static void handleClick(MouseEvent e, JFrame window, Rect[][] rects, int x1, int y1) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            if (rects[y1][x1].getType() == Rect.Type.BLANK
                    || rects[y1][x1].getType() == Rect.Type.PATH) {
                rects[y1][x1].setType(Rect.Type.WALL);
                findPath();
                window.getContentPane().repaint();
            }
            erasing = false;
        } else if (e.getButton() == MouseEvent.BUTTON3) {
            if (rects[y1][x1].getType() == Rect.Type.WALL) {
                rects[y1][x1].setType(Rect.Type.BLANK);
                findPath();
                window.getContentPane().repaint();
            }
            erasing = true;
        }
    }

    /**
     * Handle mouse dragging
     * @param window Current window
     * @param rects Rectangles that can be affected
     * @param x1 Drag X position
     * @param y1 Drag Y position
     */
    private static void handleDrag(JFrame window, Rect[][] rects, int x1, int y1) {
        if (!erasing) {
            if (rects[y1][x1].getType() == Rect.Type.BLANK
                    || rects[y1][x1].getType() == Rect.Type.PATH) {
                rects[y1][x1].setType(Rect.Type.WALL);
                findPath();
                window.getContentPane().repaint();
            }
        } else {
            if (rects[y1][x1].getType() == Rect.Type.WALL) {
                rects[y1][x1].setType(Rect.Type.BLANK);
                findPath();
                window.getContentPane().repaint();
            }
        }
    }

}