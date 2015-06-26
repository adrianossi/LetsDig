package org.letsdig.app.models.util;


import java.util.Comparator;

/**
 * Created by adrian on 6/18/15.
 */
public class UnitNameSorter implements Comparator<String> {

    @Override
    public int compare(String a, String b) {

        // String inputs will be: "(col, row)unit"
        // like "(1,2)3" or "(45,3)91"

        // split out integers (ends up {"", "45", "3", "91"})
        String[] aSplit = a.split(GridUtils.coordDelims);
        String[] bSplit = b.split(GridUtils.coordDelims);

        int aCol = Integer.valueOf(aSplit[1]);
        int aRow = Integer.valueOf(aSplit[2]);
        int aUnit = Integer.valueOf(aSplit[3]);

        int bCol = Integer.valueOf(bSplit[1]);
        int bRow = Integer.valueOf(bSplit[2]);
        int bUnit = Integer.valueOf(bSplit[3]);

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
