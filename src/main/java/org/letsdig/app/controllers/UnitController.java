package org.letsdig.app.controllers;

import org.letsdig.app.models.*;
import org.letsdig.app.models.util.GridUtils;
import org.letsdig.app.models.util.UnitSorter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by adrian on 6/13/15.
 */

@Controller
public class UnitController extends AbstractLetsDigController {

    // TODO add GET vs POST methods, in case someone arrives here accidentally by get
    @RequestMapping(value="/unit-opennew")
    public String openNewUnit(
            String squareCoords,
            Model model,
            HttpServletRequest request){

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

        Grid grid = project.getGrid();

        if (grid == null) {
            model.addAttribute("message", "Error loading grid.");
            return "error";
        }

        Square square = grid.getOrCreateSquare(
                GridUtils.getColFromCoords(squareCoords),
                GridUtils.getRowFromCoords(squareCoords));

        if (square == null) {
            model.addAttribute("message", "Error loading square.");
            return "error";
        }

        squareDao.save(square);

        Unit newUnit = square.openNewUnit();

        if (newUnit == null) {
            model.addAttribute("message", "Error creating new unit.");
            return "error";
        }

        unitDao.save(newUnit);
        squareDao.save(square);
        gridDao.save(grid);
        projectDao.save(project);

        try {
            project = getActiveProject(request);

        } catch (ProjectAccessException e) {
            model.addAttribute("message", e.getMessage());
            return "error";
        }

        request.getSession().setAttribute(unitSessionKey, newUnit.getId());

        return "redirect:unit-sheet-edit";
    }

    @RequestMapping(value="/unit-continue")
    public String continueUnit(
            String unitId,
            Model model,
            HttpServletRequest request){

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

        Unit unit = unitDao.findById(Integer.valueOf(unitId));

        if (unit == null) {
            model.addAttribute("message", "Error loading unit.");
            return "error";
        }

        request.getSession().setAttribute(unitSessionKey, unit.getId());

        return "redirect:unit-sheet-edit";
    }

    @RequestMapping(value = "/unit-sheet-edit", method=RequestMethod.GET)
    public String editUnitSheet(
            HttpServletRequest request,
            Model model) {

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

        Unit unit = unitDao.findById((int)request.getSession().getAttribute(unitSessionKey));

        if (unit == null) {
            model.addAttribute("message", "Error loading unit.");
            return "error";
        }

        model.addAttribute("unitName", unit.gimmeName());
        model.addAttribute("openDate", unit.getOpenDate().toString());

        if (unit.getCloseDate() != null) {
            model.addAttribute("closeDate", unit.getCloseDate().toString());
        } else {
            model.addAttribute("closeDate", "Still open.");
        }

        if (unit.getDescription() != null) {
            model.addAttribute("description", unit.getDescription());
        } else {
            model.addAttribute("description", "Empty.");
        }

        return "unit-sheet-form";
    }

    @RequestMapping(value="/unit-sheet-edit", method=RequestMethod.POST)
    public String unitSheet(
            Model model,
            HttpServletRequest request,
            String description){

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

        Unit unit = unitDao.findById((int) request.getSession().getAttribute(unitSessionKey));

        if (unit == null) {
            model.addAttribute("message", "Error loading unit.");
            return "error";
        }

        if (description != "") {

            if (unit.getDescription() == null) {
                unit.setDescription(description);
            } else {
                unit.setDescription(unit.getDescription() + " " + description);
            }
        }

        unitDao.save(unit);

        model.addAttribute("message", "Unit sheet updated.");

        return "redirect:unit-sheet-show";
    }

    @RequestMapping(value="/unit-sheet-show", method=RequestMethod.GET)
    public String unitSheetGet(
            @RequestParam(required = false) String unitId,
            Model model,
            HttpServletRequest request){

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

        Unit unit;

        if (unitId != null) {

            try {
                unit = unitDao.findById(Integer.valueOf(unitId));

            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                model.addAttribute("message", "Error loading unit. Invalid input.");
                return "error";
            }

        } else {

            unit = unitDao.findById((int)request.getSession().getAttribute(unitSessionKey));
        }

        if (unit == null) {
            model.addAttribute("message", "Error loading unit from database.");
            return "error";
        }

        model.addAttribute("unitName", unit.gimmeName());
        model.addAttribute("openDate", unit.getOpenDate().toString());

        if (unit.getDescription() != null) {
            model.addAttribute("description", unit.getDescription());
        } else {
            model.addAttribute("description", "empty");
        }

        if (unit.getCloseDate() != null) {
            model.addAttribute("closeDate", unit.getCloseDate().toString());
        } else {
            model.addAttribute("closeDate", "Still open.");
        }

        request.getSession().setAttribute(unitSessionKey, unit.getId());

        return "unit-sheet-show";
    }

    @RequestMapping(value="/unit-sheet-show", method=RequestMethod.POST)
    public String unitSheetPost(
            String unitId,
            Model model,
            HttpServletRequest request){

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

        Unit unit = unitDao.findById(Integer.valueOf(unitId));

        if (unit == null) {
            model.addAttribute("message", "Error loading unit.");
        }

        request.getSession().setAttribute(unitSessionKey, unit.getId());

        return "redirect:unit-sheet-show";

    }

    @RequestMapping(value="/unit-list")
    public String listUnits(
            HttpServletRequest request,
            Model model,
            @RequestParam(required = false) String q){

        // remove unit from session, if there was one
        if (request.getSession().getAttribute(unitSessionKey) != null) {
            request.getSession().removeAttribute(unitSessionKey);
        }

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

        // get grid from project
        Grid grid = project.getGrid();

        // null check grid
        if (grid == null) {
            model.addAttribute("message", "Error loading units. Grid is not set.");
            return "error";
        }

        List<Unit> unsortedUnits;

        switch (q) {
            case "a":
                model.addAttribute("message", "Showing all units.");
                unsortedUnits = grid.gimmeAllUnits();
                break;

            case "o":
                model.addAttribute("message", "Showing open units.");
                unsortedUnits = grid.gimmeFilteredUnits("open");
                break;

            case "c":
                model.addAttribute("message", "Showing closed units.");
                unsortedUnits = grid.gimmeFilteredUnits("closed");
                break;

            default:
                model.addAttribute("message", "Query error. Showing all units.");
                unsortedUnits = grid.gimmeAllUnits();
                break;
        }

        // sorter to properly sort units for display
        UnitSorter unitSorter = new UnitSorter();

        // TreeSet to store units ordered by col, row, and unit number
        Set<Unit> requestedUnits = new TreeSet<>(unitSorter);

        // Sort the units from ArrayList into TreeSet
        for (Unit unit : unsortedUnits) {
            requestedUnits.add(unit);
        }

        model.addAttribute("requestedUnits", requestedUnits);
        return "unit-list";
    }

    @RequestMapping(value = "/unit-close")
    public String closeUnit(HttpServletRequest request, Model model) {

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

        Unit unit = unitDao.findById((int) request.getSession().getAttribute(unitSessionKey));

        if (unit == null) {
            model.addAttribute("message", "Error loading unit.");
            return "error";
        }

        unit.close();

        unitDao.save(unit);

        request.getSession().removeAttribute(unitSessionKey);

        return "redirect:project-summary";
    }
}
