package org.letsdig.app.models.util;

/**
 * Created by adrian on 6/17/15.
 */
abstract public class GridUtils {

    public static String coordDelims = "[ ,()]+";

    public static int getColFromCoords(String userInput) {
        String[] squareCoords = userInput.split(coordDelims);
        return Integer.valueOf(squareCoords[0]);
    }

    public static int getRowFromCoords(String userInput) {
        String[] squareCoords = userInput.split(coordDelims);
        return Integer.valueOf(squareCoords[1]);
    }
}
