package org.letsdig.app.models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

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
    private Grid grid;

    public Project (int directorId, String name) {

        this.directorId = directorId;
        this.name = name;
        this.fullName = null;
        this.location = null;
        this.grid = null;
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

    @OneToOne(cascade = CascadeType.PERSIST)
    public Grid getGrid() {
        return grid;
    }

    public void setGrid(Grid grid) {
        this.grid = grid;
    }

    @Override
    public String toString() {
        if (fullName != null) {
            return fullName;
        }
        return name;
    }

}
