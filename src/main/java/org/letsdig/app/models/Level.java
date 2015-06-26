package org.letsdig.app.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by adrian on 6/26/15.
 */

@Entity
@Table(name="levels")
public class Level extends AbstractLetsDigEntity {

    public enum LevelType {
        OPENING("opening"), CLOSING("closing"), DATUM("datum");
        private String type;

        LevelType(String type) {
            this.type = type;
        }
    }

    LevelType type;
    double value;
    Date dateStamp;
    Level datum;
    Unit unit;
    String description;

    public Level() {}

    public Level(LevelType type,
                 double value,
                 Level datum,
                 Unit unit,
                 String description)
            throws LevelParameterException {

        if (type == LevelType.DATUM) {
            throw new LevelParameterException("Level type conflict: DATUM not allowed here.");
        }

        this.type = type;
        this.value = value;
        this.dateStamp = new Date();
        this.datum = datum;
        this.unit = unit;
        this.description = description;
    }

    public Level(LevelType type,
                 double value,
                 String description)
            throws LevelParameterException {

        if (type != LevelType.DATUM) {
            throw new LevelParameterException("Level type conflict: DATUM is required here.");
        }

        this.type = type;
        this.value = value;
        this.dateStamp = new Date();
        this.datum = null;
        this.unit = null;
        this.description = description;
    }

    @Column(name="type")
    public LevelType getType() {
        return type;
    }

    public void setType(LevelType type) {
        this.type = type;
    }

    @Column(name="value")
    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    @Column(name="date_stamp")
    public Date getDateStamp() {
        return dateStamp;
    }

    public void setDateStamp(Date dateStamp) {
        this.dateStamp = dateStamp;
    }

    // TODO: make this a @ManyToOne relationship (like Project/User to LatLong)
    @Column(name="datum")
    public Level getDatum() {
        return datum;
    }

    public void setDatum(Level datum) {
        this.datum = datum;
    }

    // TODO: make this a @ManyToOne relationship (like Unit to Square)
    @Column(name="unit")
    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    @Column(name="description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // method to get the level's value above sea level
    public double gimmeMasl() {
        if (type == LevelType.DATUM) {
            return value;
        } else {
            return datum.getValue() - value;
        }
    }
}
