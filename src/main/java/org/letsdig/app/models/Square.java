package org.letsdig.app.models;

import javax.persistence.*;
import javax.persistence.criteria.Fetch;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by adrian on 6/15/15.
 */

@Entity
@Table(name = "squares")
public class Square extends AbstractLetsDigEntity {

    private Grid grid;
    private int columnNumber;
    private int rowNumber;
    private int nextUnitNumber;
    private List<Unit> units;

    public Square(Grid grid, int columnNumber, int rowNumber) {
        this.grid = grid;
        this.columnNumber = columnNumber;
        this.rowNumber = rowNumber;
        this.nextUnitNumber = 1;
        this.units = new ArrayList<>();
    }

    public Square() {}

    /******************** GETTERS AND SETTERS **********************/

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    public Grid getGrid() {
        return grid;
    }

    public void setGrid(Grid grid) {
        this.grid = grid;
    }

    @NotNull
    @Column(name = "col_num")
    public int getColumnNumber() {
        return columnNumber;
    }


    public void setColumnNumber(int columnNumber) {
        this.columnNumber = columnNumber;
    }

    @NotNull
    @Column(name = "row_num")
    public int getRowNumber() {
        return rowNumber;
    }

    public void setRowNumber(int rowNumber) {
        this.rowNumber = rowNumber;
    }

    @Column(name = "next_unit_num")
    public int getNextUnitNumber() {
        return nextUnitNumber;
    }

    public void setNextUnitNumber(int nextUnitNumber) {
        this.nextUnitNumber = nextUnitNumber;
    }

    @OneToMany(mappedBy = "square")
    public List<Unit> getUnits() {
        return units;
    }

    public void setUnits(List<Unit> units) {
        this.units = units;
    }

    /***************** OTHER METHODS **************************/

    public Unit openNewUnit() {

        // generate new Unit that references this Square
        Unit newUnit = new Unit(this, this.nextUnitNumber);

        // increment number counter for next new Unit
        this.nextUnitNumber++;

        // return the new Unit
        return newUnit;
    }

    @Override
    public String toString() {
        return this.columnNumber + "." + this.rowNumber;
    }

    public List<Unit> gimmeOpenUnits() {
        List<Unit> openUnits = new ArrayList<>();
        for (Unit unit: this.getUnits()) {
            if (unit.getCloseDate() == null) {
                openUnits.add(unit);
            }
        }
        return openUnits;
    }

    public List<Unit> gimmeClosedUnits() {
        List<Unit> closedUnits = new ArrayList<>();
        for (Unit unit: this.getUnits()) {
            if (unit.getCloseDate() != null) {
                closedUnits.add(unit);
            }
        }
        return closedUnits;
    }
}
