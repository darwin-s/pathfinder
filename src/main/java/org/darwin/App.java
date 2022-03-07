package org.darwin;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.LinkedList;

public class App extends Application {
    private static final int SIZE = 20;
    private Rect[][] rects;
    private Rect start;
    private Rect end;

    /**
     * Find path from start to end point, using BFS.
     */
    private void findPath() {
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
     * @param root Drawing group
     */
    private void initRect(int x, int y, Group root) {
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

        root.getChildren().add(rects[y][x].getShape());
    }

    @Override
    public void start(Stage stage) {
        rects = new Rect[SIZE][SIZE];

        Group root = new Group();
        Scene scene = new Scene(root, 640.0, 640.0, Color.WHITE);

        for (int y = 0; y < SIZE; y++) {
            for (int x = 0; x < SIZE; x++) {
                initRect(x, y, root);
            }
        }

        // Find initial path
        findPath();

        // Set up event handling
        EventHandler<MouseEvent> eventHandler = mouseEvent -> {
            int x1 = (int) (mouseEvent.getX() / Rect.SIZE);
            int y1 = (int) (mouseEvent.getY() / Rect.SIZE);

            if (x1 < 0 || x1 >= SIZE || y1 < 0 || y1 >= SIZE) {
                return;
            }

            if (mouseEvent.isPrimaryButtonDown()) {
                if (rects[y1][x1].getType() == Rect.Type.BLANK
                        || rects[y1][x1].getType() == Rect.Type.PATH) {
                    rects[y1][x1].setType(Rect.Type.WALL);
                    findPath();
                }
            } else if (mouseEvent.isSecondaryButtonDown()) {
                if (rects[y1][x1].getType() == Rect.Type.WALL) {
                    rects[y1][x1].setType(Rect.Type.BLANK);
                    findPath();
                }
            }
        };

        scene.addEventFilter(MouseEvent.MOUSE_DRAGGED, eventHandler);
        scene.addEventFilter(MouseEvent.MOUSE_PRESSED, eventHandler);

        stage.setTitle("Pathfinder");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}