package org.letsdig.app.controllers;

import org.letsdig.app.models.LatLong;
import org.letsdig.app.models.Project;
import org.letsdig.app.models.User;
import org.letsdig.app.models.dao.GridDao;
import org.letsdig.app.models.dao.LatLongDao;
import org.letsdig.app.models.dao.ProjectDao;
import org.letsdig.app.models.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.SessionAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by adrian on 6/1/15.
 */
@SessionAttributes("project_id")
public abstract class AbstractLetsDigController {

    @Autowired
    protected UserDao userDao;

    @Autowired
    protected LatLongDao latLongDao;

    @Autowired
    protected ProjectDao projectDao;

    @Autowired
    protected GridDao gridDao;

    // static properties for error display
    private static final String errorTemplateIdentifier = "error";
    private static final String errorMessageIdentifier = "message";

    // static property for user identification
    public static final String userSessionKey = "user_id";

    // TODO delete this unused sessionKey
    public static final String projectSessionKey = "project_id";

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

      /* DB LOOKUP USING TRY/CATCH
        try {
            newLocation = latLongDao.findByLatitudeAndLongitude(latitude, longitude);
        } catch (NullPointerException e) {
            newLocation = new LatLong(latitude, longitude);
            latLongDao.save(newLocation); FIXME null pointer exception happens here
        }
      */

        // DB LOOKUP WITH NULL CHECK
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
