package org.letsdig.app.controllers;

import org.letsdig.app.models.LatLong;
import org.letsdig.app.models.Project;
import org.letsdig.app.models.User;
import org.letsdig.app.models.util.LatLongUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
            HttpSession session,
            Model model) {

        // get user's data from db
        User user = getUserFromSession(request);

        // get user's uid
        int director_id = user.getUid();

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
        session.setAttribute("project", currentProject);

        return "redirect:projectview";
    }

    @RequestMapping(value = "/loadproject", method = RequestMethod.POST)
    public String loadProject(
            String projectName,
            HttpServletRequest request,
            HttpSession session,
            Model model) {

        // query db for project
        Project currentProject = projectDao.findByDirectorIdAndName(getUserIdFromSession(request), projectName);

        // if project NOT found, display error
        if (currentProject == null) {
            model.addAttribute("message", "Project not found.");
            return "error";
        }

        // load project into session
        session.setAttribute("project", currentProject);

        return "redirect:projectview";
    }

    @RequestMapping(value = "/projectview")
    public String projectView (HttpSession session, Model model) {

        // check if session has a project
        if (session.getAttribute("project") == null) {
            model.addAttribute("message", "No project is currently active.");
            return "redirect:projects";
        }

        // get and verify project from session
        Project project = (Project)session.getAttribute("project");

        if (project == null) {
            model.addAttribute("message", "Error loading project.");
            return "error";
        }

        // prep model
        model.addAttribute("projectName", project.getName());

        if (project.getFullName() != null) {
            model.addAttribute("fullName", project.getFullName());
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

        return "project-view";
    }

    @RequestMapping(value = "/unloadproject")
    public String unloadProject(HttpSession session) {

        session.removeAttribute("project");

        return "redirect:projects";
    }

    @RequestMapping(value = "/mapproject")
    public String map(
            HttpSession session,
            Model model) {

        // get project from db
        Project project = (Project)session.getAttribute("project");

        if (project == null) {
            model.addAttribute("message", "Error loading project.");
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

    @RequestMapping(value = "/projectedit", method = RequestMethod.POST)
    public String projectEdit(
            String fullName,
            String latitude,
            String longitude,
            Model model,
            HttpSession session) {

        // check if session has a project
        if (session.getAttribute("project") == null) {
            return "projects";
        }

        // get and verify project from session
        Project project = (Project)session.getAttribute("project");

        if (project == null) {
            model.addAttribute("message", "Error loading project.");
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
            LatLong newLocation = this.lookupLatLong(Double.valueOf(latitude), Double.valueOf(longitude));

            // set LatLong as project's location and save both
            latLongDao.save(newLocation);
            project.setLocation(newLocation);

            // test data 40.3496462, -74.6596824
        }

        projectDao.save(project);

        return "redirect:projectview";
    }

    @RequestMapping(value = "/projectedit", method = RequestMethod.GET)
    public String projectEdit(
            HttpSession session,
            Model model) {

        // FIXME Why does this code run when it is not called????

        // check if session has a projects
        if (session.getAttribute("project") == null) {
            return "projects";
        }

        // get and verify the project
        Project project = (Project)session.getAttribute("project");

        if (project != null) {

            // prep the model
            model.addAttribute("projectName", project.getName());

            if (project.getLocation() != null) {
                model.addAttribute("latitude", project.getLocation().getLatitude());
                model.addAttribute("longitude", project.getLocation().getLongitude());
            } else {
                model.addAttribute("latitude", "empty");
                model.addAttribute("longitude", "empty");
            }
        }

        return "dig-edit";
    }

}
