package org.letsdig.app.models;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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
    private List<Square> squares;

    public Grid(
            Project project,
            LatLong origin,
            int bigGridNumRows,
            int bigGridNumCols,
            double bigGridSquareSize){

        this.project = project;
        this.origin = origin;
        this.bigGridNumRows = bigGridNumRows;
        this.bigGridNumCols = bigGridNumCols;
        this.bigGridSquareSize = bigGridSquareSize;
        this.squares = new ArrayList<>();
    }

    public Grid() {}

    /**
     *          -----   GETTERS AND SETTERS  -----
     */

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

    @OneToMany(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "grid_id")
    public List<Square> getSquares() {
        return squares;
    }

    public void setSquares(List<Square> squares) {
        this.squares = squares;
    }

    /**
     *              ----- OTHER METHODS ------
     */

    public String originToString() {
        return this.getOrigin().getLatitude() + ", " + this.getOrigin().getLongitude();
    }

}
