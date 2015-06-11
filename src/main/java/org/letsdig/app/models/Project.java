package org.letsdig.app.models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Created by adrian on 6/5/15.
 */

@Entity // each instance is stored as a persistent entity...
@Table(name = "projects")
public class Project extends AbstractLetsDigEntity {

    private int directorId;
    private String name;
    private String fullName;
    private LatLong location;

    public Project (int directorId, String name) {

        this.directorId = directorId;
        this.name = name;
        this.fullName = null;
        this.location = null;
    }

    public Project () {}

    @NotNull
    @Column(name = "director_id")
    public int getDirectorId() {
        return directorId;
    }

    public void setDirectorId(int directorId) {
        this.directorId = directorId;
    }

    @NotNull
    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "full_name")
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    @ManyToOne
    public LatLong getLocation() {
        return location;
    }

    public void setLocation(LatLong location) {
        this.location = location;
    }

}
