package org.letsdig.app.models;

import org.letsdig.app.models.util.PasswordHash;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

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
    private double latitude;
    private double longitude;

    public User(String username, String password) {
        this.username = username;
        this.hash = PasswordHash.getHash(password);
        this.firstName = null;
        this.lastName = null;
        this.latitude = 0.0;
        this.longitude = 0.0;
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

    @Override
    public String toString() {
        return this.firstName + " " + this.lastName;
    }

    public String gimmeDisplayName() {
        return (this.getFirstName() == null) ? this.getUsername() : this.getFirstName();
    }

    @Column(name = "latitude")
    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    @Column(name = "longitude")
    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String gimmeLocation() {
        return this.getLatitude() + ", " + this.getLongitude();
    }

}
