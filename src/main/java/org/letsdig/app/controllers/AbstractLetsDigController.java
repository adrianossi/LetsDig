package org.letsdig.app.controllers;

import org.letsdig.app.models.LatLong;
import org.letsdig.app.models.Project;
import org.letsdig.app.models.User;
import org.letsdig.app.models.dao.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.SessionAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by adrian on 6/1/15.
 */

public abstract class AbstractLetsDigController {

    @Autowired
    protected UserDao userDao;

    @Autowired
    protected LatLongDao latLongDao;

    @Autowired
    protected ProjectDao projectDao;

    @Autowired
    protected GridDao gridDao;

    @Autowired
    protected SquareDao squareDao;

    @Autowired
    protected UnitDao unitDao;

//    @Autowired
  //  protected UnitDao unitDao;

    // static properties for error display
    private static final String errorTemplateIdentifier = "error";
    private static final String errorMessageIdentifier = "message";

    // static property for user identification
    public static final String userSessionKey = "user_id";

    // method for any given controller in the app to display an error
    public String displayError(String message, Model model) {
        model.addAttribute(errorMessageIdentifier, message);
        return errorTemplateIdentifier;
    }

    // method for any controller to get the current user's id
    public int getUserIdFromSession(HttpServletRequest request) {
        return (int)request.getSession().getAttribute(userSessionKey);
    }

    // method for any controller to get the current User object
    public User getUserFromSession(HttpServletRequest request) {
        return userDao.findByUid(getUserIdFromSession(request));
    }


/*  TODO delete this failed attempt to store project in session
    // method for any controller to get the current project id
    public int getProjectIdFromSession(HttpServletRequest request) {
        int key;

        try {
            key = (int) request.getSession().getAttribute(projectSessionKey);
            return key;
        } catch (NullPointerException e) {
            e.printStackTrace();
            return 0;
        }
    }

    // method for any controller to get the current Project object
    public Project getProjectFromSession(HttpServletRequest request) {
        return projectDao.findByUid(getProjectIdFromSession(request));
    }
*/

    public LatLong lookupLatLong(double latitude, double longitude) {

        LatLong newLocation;

        // lookup in db
        newLocation = latLongDao.findByLatitudeAndLongitude(latitude, longitude);

        // if not found in db, create new LatLong
        if (newLocation == null) {

            newLocation = new LatLong(latitude, longitude);
            latLongDao.save(newLocation);
        }

        // return the valid LatLong
        return newLocation;
    }
}
