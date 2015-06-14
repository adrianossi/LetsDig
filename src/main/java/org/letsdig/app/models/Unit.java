package org.letsdig.app.models;

import org.springframework.ui.Model;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by adrian on 6/11/15.
 */

@Entity
@Table(name = "units")
public class Unit extends AbstractLetsDigEntity {

    private int gridId;
    private String squareId;
    private int number;
    private Date openDate;
    private Date closeDate;
    private HashMap<String, Double> openingLevels;
    private HashMap<String, Double> closingLevels;
    private String description;

    public Unit(
            int gridId,
            String squareId,
            Date openDate) {
        this.gridId = gridId;
        this.squareId = squareId;
        this.openDate = new Date();
        this.closeDate = null;
        this.openingLevels = new HashMap<>();
        this.closingLevels = new HashMap<>();
        this.description = null;
    }

    public Unit() {}

    /*
     *          -----   GETTERS AND SETTERS  -----
     */

    public int getGridId() {
        return gridId;
    }

    public void setGridId(int gridId) {
        this.gridId = gridId;
    }

    public String getSquareId() {
        return squareId;
    }

    public void setSquareId(String squareId) {
        this.squareId = squareId;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public Date getOpenDate() {
        return openDate;
    }

    public void setOpenDate(Date openDate) {
        this.openDate = openDate;
    }

    public Date getCloseDate() {
        return closeDate;
    }

    public void setCloseDate(Date closeDate) {
        this.closeDate = closeDate;
    }

    public HashMap<String, Double> getOpeningLevels() {
        return openingLevels;
    }

    public void setOpeningLevels(HashMap<String, Double> openingLevels) {
        this.openingLevels = openingLevels;
    }

    public HashMap<String, Double> getClosingLevels() {
        return closingLevels;
    }

    public void setClosingLevels(HashMap<String, Double> closingLevels) {
        this.closingLevels = closingLevels;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /*
     *              ----- OTHER METHODS ------
     */

    public String close(Model model) {
        if (!this.openingLevels.isEmpty()) {

            if (this.closingLevels.isEmpty()) {
                model.addAttribute("message", "Since you took opening levels, you must enter at least one closing level.");
                return "unit-sheet";
            }
        } else {
            if (this.closingLevels.isEmpty()) {
                model.addAttribute("message", "You entered closing levels, but no opening levels. Please go back and add at least one opening level.");
                return "unit-sheet";
            }
        }

        if (this.description.equals(null)) {
            model.addAttribute("message", "You must add a description before closing the unit");
            return "unit-sheet";
        }

        this.closeDate = new Date();
    }

    public boolean isOpen() {
        return !this.closeDate.equals(null);
    }
}

