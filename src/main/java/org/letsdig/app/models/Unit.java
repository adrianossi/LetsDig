package org.letsdig.app.models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Created by adrian on 6/11/15.
 */

@Entity
@Table(name = "units")
public class Unit extends AbstractLetsDigEntity {

    private int squareId;
    private int number;
    private Date openDate;
//    private Date closeDate;
    private String description;

    public Unit(int squareId, int number) {
        this.squareId = squareId;
        this.number = number;
        this.openDate = new Date();
    //    this.closeDate = null;
        this.description = null;
    }

    public Unit() {}

    /**
     *          -----   GETTERS AND SETTERS  -----
     */

    @NotNull
    @Column(name = "square_id")
    public int getSquareId() {
        return squareId;
    }

    public void setSquareId(int squareId) {
        this.squareId = squareId;
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

    /**
     *              ----- OTHER METHODS ------
     */


}

