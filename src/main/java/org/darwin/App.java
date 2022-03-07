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

    private void findPath() {
        boolean[][] visited = new boolean[SIZE][SIZE];

        for (int y = 0; y < SIZE; y++) {
            for (int x = 0; x < SIZE; x++) {
                visited[y][x] = false;

                if (rects[y][x].getType() == Rect.Type.PATH) {
                    rects[y][x].setType(Rect.Type.BLANK);
                }
            }
        }

        visited[start.getY()][start.getX()] = true;

        LinkedList<Node> queue = new LinkedList<>();
        Node startNode = new Node(start, null);
        Node endNode = null;
        queue.add(startNode);

        while (!queue.isEmpty()) {
            Node currentNode = queue.pop();
            Rect currentRect = currentNode.getRect();
            final int currentX = currentRect.getX();
            final int currentY = currentRect.getY();


            if (currentRect == end) {
                break;
            }

            final int DIRECTION_NUM = 4;
            final int[] xDirections = {-1, 1, 0, 0};
            final int[] yDirections = {0, 0, -1, 1};

            for (int i = 0; i < DIRECTION_NUM; i++) {
                final int newY = currentY + yDirections[i];
                final int newX = currentX + xDirections[i];

                if (newX < 0 || newX >= SIZE || newY < 0 || newY >= SIZE) {
                    continue;
                }

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

        if (endNode != null) {
            Node currentNode = endNode.getParent();

            while (currentNode != startNode) {
                currentNode.getRect().setType(Rect.Type.PATH);

                currentNode = currentNode.getParent();
            }
        }
    }

    private void initRect(int x, int y, Group root) {
        if (x < 0 || x >= SIZE || y < 0 || y >= SIZE) {
            return;
        }

        rects[y][x] = new Rect(x, y);

        if (x == 0 && y == SIZE - 1) {
            rects[y][x].setType(Rect.Type.START);
            start = rects[y][x];
        } else if (x == SIZE - 1 && y == 0) {
            rects[y][x].setType(Rect.Type.END);
            end = rects[y][x];
        }

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

        rects[y][x].getShape().addEventFilter(MouseEvent.MOUSE_DRAGGED, eventHandler);
        rects[y][x].getShape().addEventFilter(MouseEvent.MOUSE_PRESSED, eventHandler);

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

        findPath();

        stage.setTitle("Pathfinder");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}