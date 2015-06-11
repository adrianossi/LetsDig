package org.letsdig.app.models;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by adrian on 6/11/15.
 */

@Entity
@Table(name = "grids")
public class Grid extends AbstractLetsDigEntity {

    public enum LabelType {
        ALPHA("alphabetical"), NUM("numerical");

        private String type;

        LabelType(String type) {
            this.type = type;
        }
    }

    LatLong origin;
    int bigGridNumRows;
    int bigGridNumCols;
    LabelType bigGridRowLabelType;
    LabelType bigGridColLabelType;
    double bigGridSquareSize;
    int medGridNumRows;
    LabelType medGridRowLabelType;
    LabelType medGridColLabelType;
    // Lisa<Unit> units;


    public LatLong getOrigin() {
        return origin;
    }

    public void setOrigin(LatLong origin) {
        this.origin = origin;
    }

    public int getBigGridNumRows() {
        return bigGridNumRows;
    }

    public void setBigGridNumRows(int bigGridNumRows) {
        this.bigGridNumRows = bigGridNumRows;
    }

    public int getBigGridNumCols() {
        return bigGridNumCols;
    }

    public void setBigGridNumCols(int bigGridNumCols) {
        this.bigGridNumCols = bigGridNumCols;
    }

    public LabelType getBigGridRowLabelType() {
        return bigGridRowLabelType;
    }

    public void setBigGridRowLabelType(LabelType bigGridRowLabelType) {
        this.bigGridRowLabelType = bigGridRowLabelType;
    }

    public LabelType getBigGridColLabelType() {
        return bigGridColLabelType;
    }

    public void setBigGridColLabelType(LabelType bigGridColLabelType) {
        this.bigGridColLabelType = bigGridColLabelType;
    }

    public double getBigGridSquareSize() {
        return bigGridSquareSize;
    }

    public void setBigGridSquareSize(double bigGridSquareSize) {
        this.bigGridSquareSize = bigGridSquareSize;
    }

    public int getMedGridNumRows() {
        return medGridNumRows;
    }

    public void setMedGridNumRows(int medGridNumRows) {
        this.medGridNumRows = medGridNumRows;
    }

    public LabelType getMedGridRowLabelType() {
        return medGridRowLabelType;
    }

    public void setMedGridRowLabelType(LabelType medGridRowLabelType) {
        this.medGridRowLabelType = medGridRowLabelType;
    }

    public LabelType getMedGridColLabelType() {
        return medGridColLabelType;
    }

    public void setMedGridColLabelType(LabelType medGridColLabelType) {
        this.medGridColLabelType = medGridColLabelType;
    }

    public
}
