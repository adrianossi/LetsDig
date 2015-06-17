package org.letsdig.app.models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by adrian on 6/15/15.
 */

@Entity
@Table(name = "squares")
public class Square extends AbstractLetsDigEntity {

    private int gridId;
    private int columnNumber;
    private int rowNumber;
    private int nextUnitNumber;
    private List<Unit> units;

    public Square(int gridId, int columnNumber, int rowNumber) {
        this.gridId = gridId;
        this.columnNumber = columnNumber;
        this.rowNumber = rowNumber;
        this.nextUnitNumber = 1;
        this.units = new ArrayList<>();
    }

    public Square() {}

    /******************** GETTERS AND SETTERS **********************/

    @NotNull
    @Column(name = "grid_id")
    public int getGridId() {
        return gridId;
    }

    public void setGridId(int gridId) {
        this.gridId = gridId;
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

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "square_id")
    public List<Unit> getUnits() {
        return units;
    }

    public void setUnits(List<Unit> units) {
        this.units = units;
    }

    /***************** OTHER METHODS **************************/

    public Unit openNewUnit() {

        // generate new Unit that references this Square
        Unit newUnit = new Unit(this.getUid(), this.nextUnitNumber);

        // increment number counter for next new Unit
        this.nextUnitNumber++;

        // return the new Unit
        return newUnit;
    }

    @Override
    public String toString() {
        return this.columnNumber + "," + this.rowNumber;
    }
}
