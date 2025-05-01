package com.tomtrotter.habitatsimulation.ui.canvas;


import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
* Provides a graphical representation of a field using a canvas.
* This class extends the JavaFX Canvas and allows for drawing grid-based elements.
*/

public class FieldCanvas extends Canvas {

    private final int gridWidth;
    private final int gridHeight;
    private double cellWidth, cellHeight;
    private final GraphicsContext gc;

    /**
    * Constructs a new FieldCanvas with the given dimensions.
    *
    * @param canvasWidth The width of the canvas in pixels.
    * @param canvasHeight The height of the canvas in pixels.
    * @param gridWidth The number of cells in the grid's width.
    * @param gridHeight The number of cells in the height of the grid.
    */
    public FieldCanvas(double canvasWidth, double canvasHeight, int gridWidth, int gridHeight) {
        super(canvasHeight, canvasWidth);
        gc = getGraphicsContext2D();
        this.gridWidth = gridWidth;
        this.gridHeight = gridHeight;

        setScale();
        updateSize();
    }

    /**
    * Updates the canvas size by recalculating the cell dimensions
    * and clearing the canvas.
    */
    public void updateSize() {
        setScale();
        clearCanvas();
    }

    /**
    * Adjusts the scale of the grid cells based on the canvas size.
    * This determines the width and height of each cell.
    */
    public void setScale() {
        cellWidth = getWidth() / (double) gridWidth;
        cellHeight = getHeight() / (double) gridHeight;
    }

    /**
    * Draws a coloured rectangle on the canvas at the specified grid position.
    * A black border is also drawn around the rectangle for visibility.
    *
    * @param x The column index in the grid.
    * @param y The row index in the grid.
    * @param colour The colour of the rectangle to be drawn.
    */
    public void drawMark(int x, int y, Color colour) {
        gc.setFill(colour);
        gc.fillRect(x * cellWidth, y * cellHeight, cellWidth + 1,cellHeight + 1);
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(0.5); // Set border thickness
        gc.strokeRect(x * cellWidth, y * cellHeight, cellWidth + 1,cellHeight + 1);
    }

    /**
    * Clears the entire canvas by removing all drawings.
    */
    public void clearCanvas() {
        gc.clearRect(0, 0, getWidth(), getHeight());
    }

}
