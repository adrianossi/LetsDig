package org.letsdig.app.controllers;

import org.letsdig.app.models.Grid;
import org.letsdig.app.models.LatLong;
import org.letsdig.app.models.Project;
import org.letsdig.app.models.util.LatLongUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;

/**
 * Created by adrian on 6/11/15.
 */

@Controller
public class GridController extends AbstractLetsDigController {

    @RequestMapping(value = "/setgrid-viewform")
    public String setGrid(Model model, HttpSession session) {
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

        model.addAttribute("fullName", project.getFullName());

        return "set-grid";
    }

    @RequestMapping(value = "/setgrid-execute", method = RequestMethod.POST)
    public String setGrid(
            Model model,
            HttpSession session,
            String latitude,
            String longitude,
            String bgSqSize,
            String bgNumRows,
            String bgNumCols) {

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

        LatLong location;

        // validate lat/long input
        if (LatLongUtils.isValidLatLong(latitude, longitude)){
            location = this.lookupLatLong(Double.valueOf(latitude), Double.valueOf(longitude));
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

        if (project.getGrid().getUid() == newGrid.getUid() &&
                newGrid.getProject().getUid() == project.getUid()) {
            return "redirect:projectview";
        } else {
            model.addAttribute("message", "Error saving data.");
            return "error";
        }
    }
}
