package org.letsdig.app.models.util;

import org.letsdig.app.models.LatLong;
import org.letsdig.app.models.dao.LatLongDao;

/**
 * Created by adrian on 6/8/15.
 */
public class LatLongUtils {

    static LatLongDao latLongDao;

    public static boolean isValidLatLong(String latitude, String longitude) {

        // user input validation 1: must submit lat AND long
        if (latitude == "" || longitude == "") {
            return false;
        }

        // user input validation 2: input must be castable as double
        double latitudeAsDouble;
        double longitudeAsDouble;

        try {
            latitudeAsDouble = Double.valueOf(latitude);
            longitudeAsDouble = Double.valueOf(longitude);
        } catch (IllegalArgumentException e){
            return false;
        }

        return true;
    }

    public static LatLong lookup(double latitude, double longitude) {

        // query db with input data
        LatLong newLocation;
        try {
            newLocation = latLongDao.findByLatitudeAndLongitude(latitude, longitude);
        } catch (NullPointerException e) {
            newLocation = new LatLong(latitude, longitude);
            latLongDao.save(newLocation);
        }
        // if not found in db, create new LatLong
//        if (newLocation == null) {
//
  //          newLocation = new LatLong(latitude, longitude);
    //    }

        // return the new LatLong
        return newLocation;
    }
}
