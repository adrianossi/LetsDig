package org.letsdig.app.models.util;

/**
 * Created by adrian on 6/8/15.
 */
public class LatLongUtils {

    public static boolean isValidLatLong(String latitude, String longitude) {

        // user input validation 1: must submit lat AND long
        if (latitude == "" || longitude == "") {
            return false;
        }

        // user input validation 2: input must be castable as double
        try {
            double latitudeAsDouble = Double.valueOf(latitude);
            double longitudeAsDouble = Double.valueOf(longitude);
        } catch (IllegalArgumentException e){
            return false;
        }

        return true;
    }
}
