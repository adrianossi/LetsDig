package org.letsdig.app.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Created by adrian on 6/27/15.
 */


@Entity
@Table(name="unit_levels")
public class UnitLevel extends Level {

    public enum LevelType {
        OPENING("opening"), CLOSING("closing");
        private String type;

        LevelType(String type) {
            this.type = type;
        }
    }

    private LevelType type;
    private Datum datum;
    private Unit unit;

    @Column(name = "type")
    public LevelType getType() {
        return type;
    }

    public void setType(LevelType type) {
        this.type = type;
    }

    @ManyToOne
    public Datum getDatum() {
        return datum;
    }

    public void setDatum(Datum datum) {
        this.datum = datum;
    }

    @ManyToOne
    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public double masl() {
        return this.getDatum().getValue() - this.getValue();
    }
}
