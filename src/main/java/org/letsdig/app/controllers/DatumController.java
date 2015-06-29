package org.letsdig.app.controllers;

import org.letsdig.app.models.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by adrian on 6/28/15.
 */

@Controller
public class DatumController extends AbstractLetsDigController {

    @RequestMapping(value = "/datum", method = RequestMethod.GET)
    public String setDatum(Model model,
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

        return "datum";

    }

    @RequestMapping(value = "/datum", method = RequestMethod.POST)
    public String setDatum(Model model,
                           HttpServletRequest request,
                           double datum_value,
                           String datum_description) {

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

        Datum datum = new Datum(datum_value, datum_description);

        datumDao.save(datum);

        request.getSession().setAttribute(datumSessionKey, datum.getId());

        model.addAttribute("message", "Datum set.");

        return "redirect:project-summary";
    }
}
