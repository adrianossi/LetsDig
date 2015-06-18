package org.letsdig.app.models.util;

/**
 * Created by adrian on 6/17/15.
 */
public class GridUtils {

    public static int getColFromCoords(String userInput) {
        String delims = "[ ,]+";
        String[] squareCoords = userInput.split(delims);
        return Integer.valueOf(squareCoords[0]);
    }

    public static int getRowFromCoords(String userInput) {
        String delims = "[ ,]+";
        String[] squareCoords = userInput.split(delims);
        return Integer.valueOf(squareCoords[1]);
    }
}
