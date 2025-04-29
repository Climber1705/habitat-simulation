package com.tomtrotter.habitatsimulation.ui;

import com.tomtrotter.habitatsimulation.ui.canvas.FieldCanvas;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
* Unit tests for the FieldCanvas class.
* These tests validate the initialization, scaling, drawing, and clearing functionality of the FieldCanvas.
*/
public class FieldCanvasTests {

    private FieldCanvas fieldCanvas;

    /**
    * Sets up a new FieldCanvas instance before each test.
    * Ensures that each test starts with a fresh instance of FieldCanvas.
    */
    @BeforeEach
    public void setUp() {
        fieldCanvas = new FieldCanvas(500, 500, 10, 10);
    }

    /**
    * Tests the initialization of the FieldCanvas.
    * Verifies that the FieldCanvas is created successfully and checks its initial scaling ratio.
    */
    @Test
    public void testFieldCanvasInitialization() {
        assertNotNull(fieldCanvas);
        assertEquals(1, fieldCanvas.getWidth() / fieldCanvas.getHeight());
    }

    /**
    * Tests the initialization of the FieldCanvas.
    * Verifies that the FieldCanvas is created successfully and checks its initial scaling ratio.
    */
    @Test
    public void testFieldCanvasScaling() {
        fieldCanvas.setScale();
        assertEquals(50, fieldCanvas.getWidth() / 10);
        assertEquals(50, fieldCanvas.getHeight() / 10);
    }

    /**
    * Tests whether drawing a mark on the canvas works correctly.
    * Ensures that calling the drawMark method does not throw any exceptions.
    */
    @Test
    public void testDrawMark() {
        assertDoesNotThrow(() -> fieldCanvas.drawMark(5, 5, Color.RED));
    }

    /**
    * Tests the clearing of the canvas.
    * Ensures that the clearCanvas method does not throw any exceptions when invoked.
    */
    @Test
    public void testClearCanvas() {
        assertDoesNotThrow(() -> fieldCanvas.clearCanvas());
    }

}
