package org.letsdig.app.models;

import org.letsdig.app.models.util.DateUtils;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by adrian on 6/26/15.
 */

@MappedSuperclass
public abstract class Level extends AbstractLetsDigEntity {

    private double value;
    private Date dateStamp;
    private String description;

    public Level() {}

    public Level(double value,
                 String description) {

        this.value = value;
        this.dateStamp = new Date();
        this.description = description;
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

    @Column(name="description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String gimmeFormattedDateStamp() {
        return DateUtils.formatDate(dateStamp);
    }
}
