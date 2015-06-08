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
    private LatLong location;

    public Project (int directorId, String name) {

        // WORKING HERE. In the process of changing director as User to
        // director as int directorId.  Will need to do another ManyToOne
        // mapping based on cs50 finance java.

        this.directorId = directorId;
        this.name = name;
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
    @ManyToOne
    public LatLong getLocation() {
        return location;
    }

    public void setLocation(LatLong location) {
        this.location = location;
    }

    @NotNull
    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
