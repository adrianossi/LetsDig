package org.letsdig.app.controllers;

import org.letsdig.app.models.*;
import org.letsdig.app.models.util.GridUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

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

        Project project;

        try {
            project = getActiveProject(request);

        } catch (ProjectAccessException e) {
            model.addAttribute("message", e.getMessage());
            return "error";
        }

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
    public String unitSheet(
            Model model,
            HttpServletRequest request){

        Unit unit = unitDao.findById((int) request.getSession().getAttribute(unitSessionKey));

        if (unit == null) {
            model.addAttribute("message", "Error loading unit.");
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

        return "unit-sheet-show";
    }

    @RequestMapping(value="/unit-sheet-show", method=RequestMethod.POST)
    public String unitSheet(
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

    @RequestMapping(value = "/unit-close")
    public String closeUnit(HttpServletRequest request, Model model) {

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
