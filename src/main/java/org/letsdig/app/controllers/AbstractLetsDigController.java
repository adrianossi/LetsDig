package org.letsdig.app.controllers;

import org.letsdig.app.models.*;
import org.letsdig.app.models.dao.*;
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

    @Autowired
    protected GridDao gridDao;

    @Autowired
    protected SquareDao squareDao;

    @Autowired
    protected UnitDao unitDao;

    @Autowired
    protected UnitLevelDao unitLevelDao;

    @Autowired
    protected DatumDao datumDao;

    // static properties for error display
    private static final String errorTemplateIdentifier = "error";
    private static final String errorMessageIdentifier = "message";

    // static property for user identification
    public static final String userSessionKey = "user_id";
    public static final String projectSessionKey = "project_id";
    public static final String unitSessionKey = "unit_id";
    public static final String datumSessionKey = "datum_id";

    // method for any given controller in the app to display an error
    public String displayError(String message, Model model) {
        model.addAttribute(errorMessageIdentifier, message);
        return errorTemplateIdentifier;
    }

    //TODO combine all of these access methods by passing in the appropriate session key
    /**
     *     METHODS ON --USER-- IN SESSION
     */

    // method for any controller to get the current user's id
    public static int getUserIdFromSession(HttpServletRequest request) {
        return (int)request.getSession().getAttribute(userSessionKey);
    }

    // method for any controller to get the current User object
    public User getUserFromSession(HttpServletRequest request) {
        return userDao.findById(getUserIdFromSession(request));
    }

    /**
     *      METHODS ON --PROJECT-- IN SESSION
     */

    // method to check if a project is active
    public static boolean aProjectIsActive(HttpServletRequest request) {
        return request.getSession().getAttribute(projectSessionKey) != null;
    }

    // method for any controller to get the current project id
    public static int getProjectIdFromSession(HttpServletRequest request) {
        return (int)request.getSession().getAttribute(projectSessionKey);
    }

    // method for any controller to get the active Project object
    public Project getActiveProject(HttpServletRequest request) throws ProjectAccessException {

        // check if a project is active
        if (!aProjectIsActive(request)) {
            throw new ProjectAccessException("No active project found.");
        }

        // get the active project from the db
        Project project = projectDao.findById(getProjectIdFromSession(request));

        // validate project
        if (project == null) {
            throw new ProjectAccessException("Error loading project.");
        }

        return project;
    }

    /**
     *          METHODS ON --DATUM-- IN SESSION
     */

    // method to check if a datum is active
    public static boolean aDatumIsActive(HttpServletRequest request) {
        return request.getSession().getAttribute(datumSessionKey) != null;
    }

    // method for any controller to get the current datum id
    public static int getDatumIdFromSession(HttpServletRequest request) {
        return (int)request.getSession().getAttribute(datumSessionKey);
    }

    // method for any controller to get the active Datum object
    public Datum getActiveDatum(HttpServletRequest request) throws DatumAccessException {

        // check if a project is active
        if (!aDatumIsActive(request)) {
            throw new DatumAccessException("No active datum found.");
        }

        // get the active project from the db
        Datum datum = datumDao.findById(getDatumIdFromSession(request));

        // validate project
        if (datum == null) {
            throw new DatumAccessException("Error loading datum.");
        }

        return datum;
    }

    /**
     *          METHODS ON --UNIT-- IN SESSION
     */

    // method to check if a unit is active
    public static boolean aUnitIsActive(HttpServletRequest request) {
        return request.getSession().getAttribute(unitSessionKey) != null;
    }

    // method for any controller to get the current unit id
    public static int getUnitIdFromSession(HttpServletRequest request) {
        return (int)request.getSession().getAttribute(unitSessionKey);
    }

    // method for any controller to get the active Unit object
    public Unit getActiveUnit(HttpServletRequest request) throws UnitAccessException {

        // check if a unit is active
        if (!aUnitIsActive(request)) {
            throw new UnitAccessException("No active unit found.");
        }

        // get the active unit from the db
        Unit unit = unitDao.findById(getUnitIdFromSession(request));

        // validate unit
        if (unit == null) {
            throw new UnitAccessException("Error loading unit.");
        }

        return unit;
    }

    /**
     *          METHODS ON --LATLONG--
     */

    public LatLong getOrCreateLatLong(double latitude, double longitude) {

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
