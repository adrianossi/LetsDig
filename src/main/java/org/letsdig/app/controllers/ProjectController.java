package org.letsdig.app.controllers;

import org.letsdig.app.models.LatLong;
import org.letsdig.app.models.Project;
import org.letsdig.app.models.User;
import org.letsdig.app.models.util.LatLongUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

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
    public String projects(HttpServletRequest request, Model model) {

        // get user's data from db
        User user = getUserFromSession(request);

        // add user's name to model
        model.addAttribute("displayName", user.gimmeDisplayName());

        // check for projects owned by user
        List<Project> projects = user.getProjects();

        // get project names
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
        request.getSession().setAttribute(projectSessionKey, currentProject.getUid());

        // prep model
        model.addAttribute("projectName", currentProject.getName());

        return "projectview";
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
        //session.setAttribute("project", currentProject);

        request.getSession().setAttribute(projectSessionKey, currentProject.getUid());

        // prep model
        model.addAttribute("projectName", currentProject.getName());

        if (currentProject.getLocation() != null) {
            model.addAttribute("latitude", currentProject.getLocation().getLatitude());
            model.addAttribute("longitude", currentProject.getLocation().getLongitude());
        } else {
            model.addAttribute("latitude", "empty");
            model.addAttribute("longitude", "empty");
        }

        return "projectview";
    }

    @RequestMapping(name = "/unloadproject")
    public String unloadProject(
            HttpServletRequest request) {

        request.getSession().setAttribute(projectSessionKey, 0);

        return "projects";
    }

    @RequestMapping(name = "/projectedit", method = RequestMethod.GET)
    public String projectEdit (
            HttpServletRequest request,
            Model model) {
        model.addAttribute("message", "Forwarded to projectedit via get.");
        return "error";
    }
/*    @RequestMapping(name = "/projectedit", method = RequestMethod.GET)
    public String projectEdit (
            HttpServletRequest request,
            Model model) {

        // FIXME Why does this code run when / is requested after the whole app first starts????

        // get the project
        Project project = getProjectFromSession(request);

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
        return "projectedit";
    }
*/
    @RequestMapping(name = "/projectedit", method = RequestMethod.POST)
    public String projectEdit(
            String latitude,
            String longitude,
            Model model,
            HttpServletRequest request) {

        // get project from session
        Project project = getProjectFromSession(request);

        // TODO create ability to add displayName for each project

        // check if lat/long input is present
        if (!(latitude == "" && longitude == "")) {

            // check if user's input is valid for lat/long
            if (!LatLongUtils.isValidLatLong(latitude, longitude)) {
                model.addAttribute("message", "Invalid location. Please try again.");
                return "error";
            }

            // get the LatLong from the db
            LatLong newLocation = LatLongUtils.lookup(Double.valueOf(latitude), Double.valueOf(longitude));

            // set LatLong as project's location and save both
            // latLongDao.save(newLocation); NOTE: LatLongUtils.lookup saves a new LatLong via dao
            project.setLocation(newLocation);

            // test data 40.3496462, -74.6596824
        }

        projectDao.save(project);

        model.addAttribute("projectName", project.getName());
        model.addAttribute("latitude", project.getLocation().getLatitude());
        model.addAttribute("longitude", project.getLocation().getLongitude());

        return "projectview";
    }

}
