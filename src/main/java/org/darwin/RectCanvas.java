package org.darwin;

import javax.swing.*;
import java.awt.*;

/**
 * Swing canvas, that draws the rectangles
 */
public class RectCanvas extends JComponent {
    private final Rect[][] rects;

    public RectCanvas(Rect[][] rects) {
        this.rects = rects;
    }

    @Override
    public void paint(Graphics g) {
        for (Rect[] row : rects) {
            for (Rect rect : row) {
                rect.draw(g);
            }
        }
    }
}
