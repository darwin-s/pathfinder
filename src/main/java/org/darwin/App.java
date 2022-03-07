package org.darwin;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class App extends Application {
    private static final int SIZE = 20;
    private Rect[][] rects;
    private Rect start;
    private Rect end;

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

        EventHandler<MouseEvent> eventHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                int x = (int) (mouseEvent.getX() / Rect.SIZE);
                int y = (int) (mouseEvent.getY() / Rect.SIZE);

                if (x < 0 || x >= SIZE || y < 0 || y >= SIZE) {
                    return;
                }

                if (rects[y][x].getType() == Rect.Type.BLANK) {
                    rects[y][x].setType(Rect.Type.WALL);
                }
            }
        };

        rects[y][x].getShape().addEventFilter(MouseEvent.MOUSE_DRAGGED, eventHandler);
        rects[y][x].getShape().addEventFilter(MouseEvent.MOUSE_CLICKED, eventHandler);

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

        stage.setTitle("Pathfinder");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}