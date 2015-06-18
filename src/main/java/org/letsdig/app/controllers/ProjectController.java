package org.letsdig.app.controllers;

import org.letsdig.app.models.*;
import org.letsdig.app.models.util.LatLongUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * Created by adrian on 6/8/15.
 */

@Controller
public class ProjectController extends AbstractLetsDigController {

    // add prehandle method to check for projectid in session, redirect to
    // select proj page if not found? Call it at the head of every method in
    // this controller.

    @RequestMapping(value = "/projects")
    public String projects(
            HttpServletRequest request,
            Model model) {

        // get user's data from db
        User user = getUserFromSession(request);

        // add user's name to model
        model.addAttribute("displayName", user.gimmeDisplayName());

        // check for projects owned by user
        List<Project> projects = user.getProjects();

        // get and store project names
        ArrayList<String> projectNames = new ArrayList<>();

        if (projects != null) {
            for (Project currentProj : projects) {
                projectNames.add(currentProj.getName());
            }
            Collections.sort(projectNames);
            model.addAttribute("projectNames", projectNames);
        }

        return "projects";
    }

    @RequestMapping(value = "/createproject", method = RequestMethod.POST)
    public String createProject(
            String projectName,
            HttpServletRequest request,
            Model model) {

        // get user's uid
        int director_id = getUserIdFromSession(request);

        // search for project in db
        Project currentProject = projectDao.findByDirectorIdAndName(director_id, projectName);

        // if project found, display error
        if (currentProject != null) {
            model.addAttribute("message", "You already have a project by that name.");
            return "error";
        }

        // create and save new project
        currentProject = new Project(director_id, projectName);
        projectDao.save(currentProject);

        // load project into session
        request.getSession().setAttribute(projectSessionKey, currentProject.getId());

        return "redirect:project-settings";
    }

    @RequestMapping(value = "/project-settings", method = RequestMethod.GET)
    public String projectSettings (
            HttpServletRequest request,
            Model model) {

        Project project;

        try {
            project = getActiveProject(request);

        } catch (ProjectAccessException e) {
            model.addAttribute("message", e.getMessage());
            return "error";
        }

        // prep model
        model.addAttribute("projectName", project.getName());

        if (project.getFullName() != null) {
            model.addAttribute("projectFullName", project.getFullName());
        }

        if (project.getLocation() != null) {
            model.addAttribute("latitude", project.getLocation().getLatitude());
            model.addAttribute("longitude", project.getLocation().getLongitude());
        } else {
            model.addAttribute("latitude", "empty");
            model.addAttribute("longitude", "empty");
        }

        if (project.getGrid() != null) {
            model.addAttribute("gridStatus", "Grid is set (origin: " + project.getGrid().originToString() + ")");
        }

        return "project-settings";
    }

    @RequestMapping(value = "/project-settings", method = RequestMethod.POST)
    public String projectSettingsEdit(
            String fullName,
            String latitude,
            String longitude,
            Model model,
            HttpServletRequest request) {

        Project project;

        try {
            project = getActiveProject(request);

        } catch (ProjectAccessException e) {
            model.addAttribute("message", e.getMessage());
            return "error";
        }

        // check if fullName input is present
        if (!fullName.equals("")) {
            project.setFullName(fullName);
        }

        // check if lat/long input is present
        if (!(latitude.equals("") && longitude.equals(""))) {

            // check if user's input is valid for lat/long
            if (!LatLongUtils.isValidLatLong(latitude, longitude)) {
                model.addAttribute("message", "Invalid location. Please try again.");
                return "error";
            }

            // get the LatLong from the db
            LatLong newLocation = this.getOrCreateLatLong(Double.valueOf(latitude), Double.valueOf(longitude));

            // set LatLong as project's location and save both
            latLongDao.save(newLocation);
            project.setLocation(newLocation);

            // test data 40.3496462, -74.6596824
        }

        projectDao.save(project);

        model.addAttribute("message", "Settings saved.");

        return "redirect:project-settings";
    }

    @RequestMapping(value = "project-settings-edit")
    public String editProjectSettings(
            HttpServletRequest request,
            Model model) {

        Project project;

        try {
            project = getActiveProject(request);

        } catch (ProjectAccessException e) {
            model.addAttribute("message", e.getMessage());
            return "error";
        }

        model.addAttribute("fullName", project.gimmeDisplayName());

        return "project-settings-edit";
    }

    @RequestMapping(value = "/loadproject", method = RequestMethod.POST)
    public String loadProject(
            String projectName,
            HttpServletRequest request,
            Model model) {

        // query db for project
        Project currentProject = projectDao.findByDirectorIdAndName(getUserIdFromSession(request), projectName);

        // if project NOT found, display error
        if (currentProject == null) {
            model.addAttribute("message", "Project not found.");
            return "error";
        }

        // load project into session
        request.getSession().setAttribute(projectSessionKey, currentProject.getId());

        return "redirect:project-summary";
    }

    @RequestMapping(value="project-summary")
    public String projectSummary(HttpServletRequest request, Model model){

        // remove unit from session, if there was one
        if (request.getSession().getAttribute(unitSessionKey) != null) {
            request.getSession().removeAttribute(unitSessionKey);
        }

        Project project;

        try {
            project = getActiveProject(request);

        } catch (ProjectAccessException e) {
            model.addAttribute("message", e.getMessage());
            return "error";
        }

        // get grid from project
        Grid grid = project.getGrid();

        // null check grid, set message in model
        if (grid == null) {
            model.addAttribute("gridMessage", "Not set");
            model.addAttribute("numSquares", "0");
            model.addAttribute("totalUnits", "0");
            model.addAttribute("openUnits", "0");
            model.addAttribute("closedUnits", "0");

        } else {

            model.addAttribute("gridMessage", "Origin " + grid.originToString());

            // FIXME: returns too many squares-- one square for every unit in the existing squares
            // get squares from the grid
            List<Square> squares = grid.getSquares();

            // put number of squares into model
            if (squares.isEmpty()) {
                model.addAttribute("numSquares", "0");
            } else {
                model.addAttribute("numSquares", squares.size());
            }

            // variables and map to count Units and create uid => name map
            int closedUnits = 0;
            int openUnits = 0;
            Map<Integer, String> unitIdsAndNames = new HashMap<>();

            // iterate over squares
            for (Square square : squares) {

                // store Square name (for use in Unit name below)
                String squareName = square.toString();

                // get Units held in this Square
                List<Unit> units = square.getUnits();

                // iterate over Units in this Square
                for (Unit unit : units) {

                    // count open vs. closed Units
                    if (unit.getCloseDate() == null) {
                        openUnits++;
                    } else {
                        closedUnits++;
                    }

                    // put Unit uid and name into map
                    unitIdsAndNames.put(unit.getId(), "(" + squareName + ")" + unit.getNumber());
                }
            }

            model.addAttribute("totalUnits", openUnits + closedUnits);
            model.addAttribute("openUnits", openUnits);
            model.addAttribute("closedUnits", closedUnits);
            model.addAttribute("availableUnits", unitIdsAndNames);
        }

        return "project-summary";
    }

    @RequestMapping(value = "/unloadproject")
    public String unloadProject(HttpServletRequest request) {

        request.getSession().removeAttribute(projectSessionKey);

        return "redirect:projects";
    }

    @RequestMapping(value = "/mapproject")
    public String map(
            HttpServletRequest request,
            Model model) {

        Project project;

        try {
            project = getActiveProject(request);

        } catch (ProjectAccessException e) {
            model.addAttribute("message", e.getMessage());
            return "error";
        }

        LatLong location = project.getLocation();

        if (location != null) {
            return location.putOnMap();
        } else {
            model.addAttribute("message", "Location data not found.");
            return "error";
        }
    }

}
