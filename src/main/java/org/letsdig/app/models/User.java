package org.letsdig.app.models;

import org.letsdig.app.models.util.PasswordHash;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by adrian on 6/1/15.
 */

@Entity // each instance is stored as a persistent entity...
@Table(name = "users") // ...in a table called "users".
public class User extends AbstractLetsDigEntity {

    private String username;
    private String hash;
    private String firstName;
    private String lastName;
    private LatLong location;
    private List<Project> projects;

    public User(String username, String password) {
        this.username = username;
        this.hash = PasswordHash.getHash(password);
        this.firstName = null;
        this.lastName = null;
        this.location = null;
        this.projects = new ArrayList<Project>();
    }

    // Empty constructor for Spring to use
    public User() {}

    @NotNull // enforce notnull at object level (and at db level in @col props)
    @Column(name = "username", unique = true, nullable = false)
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Column(name = "firstName")
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Column(name = "lastName")
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @NotNull
    @Column(name = "hash", nullable = false)
    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    @ManyToOne
    public LatLong getLocation() {
        return location;
    }

    public void setLocation(LatLong location) {
        this.location = location;
    }

    @OneToMany(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "director_id")
    public List<Project> getProjects() {
        return projects;
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }

    @Override
    public String toString() {
        return this.firstName + " " + this.lastName;
    }

    public String gimmeDisplayName() {
        return (this.getFirstName() == null) ? this.getUsername() : this.getFirstName();
    }

}
