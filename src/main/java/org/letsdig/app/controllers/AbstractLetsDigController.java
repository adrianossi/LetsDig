package org.letsdig.app.controllers;

import org.letsdig.app.models.User;
import org.letsdig.app.models.dao.LatLongDao;
import org.letsdig.app.models.dao.ProjectDao;
import org.letsdig.app.models.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;

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

    // method for any given controller in the app to identify the current user
    public User getUserFromSession(HttpServletRequest request) {

        // get the user id from the session
        int userId = (int)request.getSession().getAttribute(userSessionKey);

        // find the user in the db
        return userDao.findByUid(userId);

    }

}
