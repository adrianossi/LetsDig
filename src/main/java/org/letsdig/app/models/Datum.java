package org.letsdig.app.models;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by adrian on 6/27/15.
 */

@Entity
@Table(name="datums")
public class Datum extends Level {

    // TODO Datum should probably have a Project property too...
    private List<UnitLevel> unitLevels;

    public Datum() {}

    public Datum(double value, String description) {

        super(value, description);
        this.unitLevels = new ArrayList<>();
    }

    @OneToMany(mappedBy = "datum", cascade = CascadeType.PERSIST)
    public List<UnitLevel> getUnitLevels() {
        return unitLevels;
    }

    public void setUnitLevels(List<UnitLevel> unitLevels) {
        this.unitLevels = unitLevels;
    }
}
