package org.letsdig.app.models.util;

import org.letsdig.app.models.Unit;

import java.util.Comparator;

/**
 * Created by adrian on 6/19/15.
 */
public class UnitSorter implements Comparator<Unit> {

    @Override
    public int compare(Unit a, Unit b) {

        int aCol = a.getSquare().getColumnNumber();
        int aRow = a.getSquare().getRowNumber();
        int aUnit = a.getNumber();

        int bCol = b.getSquare().getColumnNumber();
        int bRow = b.getSquare().getRowNumber();
        int bUnit = b.getNumber();

        // check if cols are equal
        if (aCol == bCol) {

            // they are, so compare rows
            if (aRow == bRow) {

                // they are, so compare units
                if (aUnit == bUnit) {

                    // all three integers are equal, so the strings are equal
                    return 0;

                } else {

                    // all but units are equal, so return unit comparison
                    return aUnit - bUnit;
                }
            } else {

                // cols are equal but rows are not, so return row comparison
                return aRow - bRow;
            }
        } else {

            // cols are not equal, so return col comparison
            return aCol - bCol;
        }
    }
}
