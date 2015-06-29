package org.letsdig.app.models;

import org.letsdig.app.models.util.DateUtils;

import javax.persistence.*;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by adrian on 6/11/15.
 */

@Entity
@Table(name = "units")
public class Unit extends AbstractLetsDigEntity {

    private Square square;
    private int number;
    private Date openDate;
    private Date closeDate;
    private String description;
    private List<UnitLevel> levels;

    public Unit(Square square, int number) {
        this.square = square;
        this.number = number;
        this.openDate = new Date();
        this.closeDate = null;
        this.description = null;
        this.levels = new ArrayList<>();
    }

    public Unit() {}

    /**
     *          -----   GETTERS AND SETTERS  -----
     */

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    public Square getSquare() {
        return square;
    }

    public void setSquare(Square square) {
        this.square = square;
    }

    @NotNull
    @Column(name = "number")
    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    @NotNull
    @Column(name = "open_date")
    public Date getOpenDate() {
        return openDate;
    }

    public void setOpenDate(Date openDate) {
        this.openDate = openDate;
    }

    @Column(name = "description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "close_date")
    public Date getCloseDate() {
        return closeDate;
    }

    public void setCloseDate(Date closeDate) {
        this.closeDate = closeDate;
    }

    @OneToMany(mappedBy = "unit", cascade = CascadeType.PERSIST)
    public List<UnitLevel> getLevels() {
        return levels;
    }

    public void setLevels(List<UnitLevel> levels) {
        this.levels = levels;
    }

    /**
     *              ----- OTHER METHODS ------
     */

    public void close() {
        this.closeDate = new Date();
    }

    @Override
    public String toString() {
        return this.getSquare().toString() + "." + this.getNumber();
    }

    public UnitLevel createLevel(double value, String description, UnitLevel.LevelType type, Datum datum) {
        return new UnitLevel(value, description, type, datum, this);
    }

    public String gimmeFormattedOpenDate() {
        return DateUtils.formatDate(this.getOpenDate());
    }

    public String gimmeFormattedCloseDate() {
        return DateUtils.formatDate(this.getCloseDate());
    }
}

