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

        // TODO have to load datum from session in EVERY project, unit, grid, etc controller method
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

        model.addAttribute("unit", unit);

        return "unit-sheet-form";
    }

    @RequestMapping(value="/unit-sheet-edit", method=RequestMethod.POST)
    public String unitSheet(
            Model model,
            HttpServletRequest request,
            String description,
            String level_1_type,
            String level_1_description,
            String level_1_value,
            String level_2_type,
            String level_2_description,
            String level_2_value,
            String level_3_type,
            String level_3_description,
            String level_3_value,
            String level_4_type,
            String level_4_description,
            String level_4_value,
            String level_5_type,
            String level_5_description,
            String level_5_value,
            String level_6_type,
            String level_6_description,
            String level_6_value) {

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

        Datum datum;

        try {
            datum = getActiveDatum(request);
        } catch (DatumAccessException e) {
            e.printStackTrace();
            model.addAttribute("message", e.getMessage());
            return "error";
        }

        model.addAttribute("datum", datum);

        if (description != "") {

            if (unit.getDescription() == null) {
                unit.setDescription(description);
            } else {
                unit.setDescription(unit.getDescription() + " " + description);
            }
        }

        if (level_1_type != null) {
            if (level_1_type.equals("opening")) {
                UnitLevel level_1 = unit.createLevel(Double.valueOf(level_1_value), level_1_description, UnitLevel.LevelType.OPENING, datum);
                unitLevelDao.save(level_1);
            } else if (level_1_type.equals("closing")) {
                UnitLevel level_1 = unit.createLevel(Double.valueOf(level_1_value), level_1_description, UnitLevel.LevelType.CLOSING, datum);
                unitLevelDao.save(level_1);
            }
        }

        if (level_2_type != null) {
            if (level_2_type.equals("opening")) {
                UnitLevel level_2 = unit.createLevel(Double.valueOf(level_2_value), level_2_description, UnitLevel.LevelType.OPENING, datum);
                unitLevelDao.save(level_2);
            } else if (level_2_type.equals("closing")) {
                UnitLevel level_2 = unit.createLevel(Double.valueOf(level_2_value), level_2_description, UnitLevel.LevelType.CLOSING, datum);
                unitLevelDao.save(level_2);
            }
        }

        if (level_3_type != null) {
            if (level_3_type.equals("opening")) {
                UnitLevel level_3 = unit.createLevel(Double.valueOf(level_3_value), level_3_description, UnitLevel.LevelType.OPENING, datum);
                unitLevelDao.save(level_3);
            } else if (level_3_type.equals("closing")) {
                UnitLevel level_3 = unit.createLevel(Double.valueOf(level_3_value), level_3_description, UnitLevel.LevelType.CLOSING, datum);
                unitLevelDao.save(level_3);
            }
        }

        if (level_4_type != null) {
            if (level_4_type.equals("opening")) {
                UnitLevel level_4 = unit.createLevel(Double.valueOf(level_4_value), level_4_description, UnitLevel.LevelType.OPENING, datum);
                unitLevelDao.save(level_4);
            } else if (level_4_type.equals("closing")) {
                UnitLevel level_4 = unit.createLevel(Double.valueOf(level_4_value), level_4_description, UnitLevel.LevelType.CLOSING, datum);
                unitLevelDao.save(level_4);
            }
        }

        if (level_5_type != null) {
            if (level_5_type.equals("opening")) {
                UnitLevel level_5 = unit.createLevel(Double.valueOf(level_5_value), level_5_description, UnitLevel.LevelType.OPENING, datum);
                unitLevelDao.save(level_5);
            } else if (level_5_type.equals("closing")) {
                UnitLevel level_5 = unit.createLevel(Double.valueOf(level_5_value), level_5_description, UnitLevel.LevelType.CLOSING, datum);
                unitLevelDao.save(level_5);
            }
        }

        if (level_6_type != null) {
            if (level_6_type.equals("opening")) {
                UnitLevel level_6 = unit.createLevel(Double.valueOf(level_6_value), level_6_description, UnitLevel.LevelType.OPENING, datum);
                unitLevelDao.save(level_6);
            } else if (level_6_type.equals("closing")) {
                UnitLevel level_6 = unit.createLevel(Double.valueOf(level_6_value), level_6_description, UnitLevel.LevelType.CLOSING, datum);
                unitLevelDao.save(level_6);
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

        model.addAttribute("unit", unit);

        model.addAttribute("unitName", unit.toString());
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
