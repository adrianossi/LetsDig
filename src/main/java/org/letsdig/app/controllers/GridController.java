package org.letsdig.app.controllers;

import org.letsdig.app.models.*;
import org.letsdig.app.models.util.LatLongUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created by adrian on 6/11/15.
 */

@Controller
public class GridController extends AbstractLetsDigController {

    @RequestMapping(value = "/setgrid-viewform")
    public String setGrid(Model model,
                          HttpServletRequest request) {

        User user = getUserFromSession(request);

        if (user == null) {
            model.addAttribute("message", "Error loading user data.");
            return "error";
        }

        model.addAttribute("user", user);

        Project project;

        try {
            project = getActiveProject(request);

        } catch (ProjectAccessException e) {
            model.addAttribute("message", e.getMessage());
            return "error";
        }

        model.addAttribute("project", project);

        model.addAttribute("fullName", project.getFullName());

        return "set-grid";
    }

    @RequestMapping(value = "/setgrid-execute", method = RequestMethod.POST)
    public String setGrid(
            Model model,
            HttpServletRequest request,
            String latitude,
            String longitude,
            String bgSqSize,
            String bgNumRows,
            String bgNumCols) {

        User user = getUserFromSession(request);

        if (user == null) {
            model.addAttribute("message", "Error loading user data.");
            return "error";
        }

        model.addAttribute("user", user);

        Project project;

        try {
            project = getActiveProject(request);
        } catch (ProjectAccessException e) {
            model.addAttribute("message", e.getMessage());
            return "error";
        }

        LatLong location;

        // validate lat/long input
        if (LatLongUtils.isValidLatLong(latitude, longitude)){
            location = this.getOrCreateLatLong(Double.valueOf(latitude), Double.valueOf(longitude));
            latLongDao.save(location);
        } else {
            model.addAttribute("message", "Invalid location. Please try again.");
            return "error";
        }

        Grid newGrid = new Grid(project, location, Integer.valueOf(bgNumRows), Integer.valueOf(bgNumCols), Double.valueOf(bgSqSize));

        // kedesh test data 33.1104216 35.5304653

        project.setGrid(newGrid);

        gridDao.save(newGrid);

        projectDao.save(project);

        if (project.getGrid().getId() == newGrid.getId() &&
                newGrid.getProject().getId() == project.getId()) {
            return "redirect:project-summary";
        } else {
            model.addAttribute("message", "Error saving data.");
            return "error";
        }
    }
}
