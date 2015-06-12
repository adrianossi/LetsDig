package org.letsdig.app.models;

import javax.persistence.*;

/**
 * Created by adrian on 6/11/15.
 */

@Entity
@Table(name = "grids")
public class Grid extends AbstractLetsDigEntity {

    private Project project;
    private LatLong origin;
    private int bigGridNumRows;
    private int bigGridNumCols;
    private double bigGridSquareSize;
//    private int medGridNumRows;
  //  private LabelType medGridRowLabelType;
    //private LabelType medGridColLabelType;
    // private List<Unit> units;


    public Grid(
            Project project,
            LatLong origin,
            int bigGridNumRows,
            int bigGridNumCols,
            double bigGridSquareSize
    //        int medGridNumRows,
      //      LabelType medGridRowLabelType,
        //    LabelType medGridColLabelType
    ){

        this.project = project;
        this.origin = origin;
        this.bigGridNumRows = bigGridNumRows;
        this.bigGridNumCols = bigGridNumCols;
        this.bigGridSquareSize = bigGridSquareSize;
    //    this.medGridNumRows = medGridNumRows;
      //  this.medGridRowLabelType = medGridRowLabelType;
        //this.medGridColLabelType = medGridColLabelType;
    }

    public Grid() {}

    @OneToOne(mappedBy = "grid")
    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    @ManyToOne
    public LatLong getOrigin() {
        return origin;
    }

    public void setOrigin(LatLong origin) {
        this.origin = origin;
    }

    @Column(name = "bg_num_rows")
    public int getBigGridNumRows() {
        return bigGridNumRows;
    }

    public void setBigGridNumRows(int bigGridNumRows) {
        this.bigGridNumRows = bigGridNumRows;
    }

    @Column(name = "bg_num_cols")
    public int getBigGridNumCols() {
        return bigGridNumCols;
    }

    public void setBigGridNumCols(int bigGridNumCols) {
        this.bigGridNumCols = bigGridNumCols;
    }

    @Column(name = "bg_sq_size")
    public double getBigGridSquareSize() {
        return bigGridSquareSize;
    }

    public void setBigGridSquareSize(double bigGridSquareSize) {
        this.bigGridSquareSize = bigGridSquareSize;
    }

/*
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
*/

    public String originToString() {
        return this.getOrigin().getLatitude() + ", " + this.getOrigin().getLongitude();
    }


}
