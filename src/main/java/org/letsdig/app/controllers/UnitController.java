package org.letsdig.app.controllers;

import org.letsdig.app.models.*;
import org.letsdig.app.models.util.GridUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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

        // FIXME an exception happens in GridUtils.getColFromCoords
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

        // FIXME when project-summary reloads after a new
        // unit has been made, the "existing units" list does not
        // contain the new unit.
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

        request.getSession().setAttribute(unitSessionKey, newUnit.getUid());

        return "redirect:unit-sheet";
    }

    @RequestMapping(value="/unit-continue")
    public String continueUnit(
            String unitId,
            Model model,
            HttpServletRequest request){

        Unit unit = unitDao.findByUid(Integer.valueOf(unitId));

        if (unit == null) {
            model.addAttribute("message", "Error loading unit.");
            return "error";
        }

        request.getSession().setAttribute(unitSessionKey, unit.getUid());

        return "redirect:unit-sheet";
    }

    @RequestMapping(value="/unit-sheet", method=RequestMethod.GET)
    public String unitSheet(
            Model model,
            HttpServletRequest request){

        Unit unit = unitDao.findByUid((int)request.getSession().getAttribute(unitSessionKey));

        if (unit == null) {
            model.addAttribute("message", "Error loading unit.");
            return "error";
        }

        Square square = squareDao.findByUid(unit.getSquareId());

        if (square == null) {
            model.addAttribute("message", "Error loading square.");
            return "error";
        }

        model.addAttribute("unitName", "(" + square.toString() + ")" + unit.getNumber());
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

        return "unit-sheet";
    }

    @RequestMapping(value="/unit-sheet", method=RequestMethod.POST)
    public String unitSheet(
            Model model,
            HttpSession session,
            String description){

        //TODO implement updating unit sheet

        return "unit-sheet";
    }



    private Square parseGridIdAndSquareId(int gridId, String squareId){

        String delims = "[ ,]+";
        String[] squareCoords = squareId.split(delims);
        int col = Integer.valueOf(squareCoords[0]);
        int row = Integer.valueOf(squareCoords[1]);

        Square square = squareDao.findByGridIdAndColumnNumberAndRowNumber(gridId, col, row);

        if (square == null) {
            square = new Square(gridId, col, row);
            squareDao.save(square);
        }

        return square;
    }

}
