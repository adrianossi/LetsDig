package org.letsdig.app.models;

import javax.persistence.*;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.util.Date;

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

    public Unit(Square square, int number) {
        this.square = square;
        this.number = number;
        this.openDate = new Date();
        this.closeDate = null;
        this.description = null;
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

    /**
     *              ----- OTHER METHODS ------
     */

    public void close() {
        this.closeDate = new Date();
    }

    public String gimmeName() {
        return "(" + this.getSquare().toString() + ")" + this.getNumber();
    }
}
