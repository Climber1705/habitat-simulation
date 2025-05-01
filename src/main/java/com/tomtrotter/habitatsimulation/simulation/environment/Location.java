package com.tomtrotter.habitatsimulation.simulation.environment;

/**
* Represents a location in a rectangular grid.
* This class stores a location's row and column position in a grid, providing basic operations for comparison, string representation, and hash code calculation.
*/

public class Location {

    private final int row;
    private final int column;

    /**
    * Represents a row and column in a rectangular grid.
    * Initializes a location object with a specific row and column.
    *
    * @param row The row in the grid.
    * @param col The column in the grid.
    */
    public Location(int row, int col) {
        this.row = row;
        this.column = col;
    }

    /**
    * @return The row value of the location.
    */
    public int getRow() {
        return row;
    }

    /**
    * @return The column value of the location.
    */
    public int getCol() {
        return column;
    }

}
