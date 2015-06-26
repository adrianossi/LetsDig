package org.letsdig.app.models;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by adrian on 6/5/15.
 */


@Entity
@Table(name = "latlongs", uniqueConstraints = @UniqueConstraint(columnNames={"latitude", "longitude"}))
public class LatLong extends AbstractLetsDigEntity {
    private double latitude;
    private double longitude;
    private List<User> users;
    private List<Project> projects;
    private List<Grid> grids;

    public LatLong (double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
        users = new ArrayList<>();
        projects = new ArrayList<>();
        grids = new ArrayList<>();
    }

    public LatLong () {}

    // static property for map redirect
    private static final String googleMapsUrlPrefix = "https://www.google.com/maps/embed/v1/place?q=";
    private static final String googleMapsUrlSuffix = "&key=AIzaSyCieHY69Y7AJbcWyOaLIshtlTVao0TyhAE";

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

    @OneToMany(mappedBy = "location", cascade = CascadeType.PERSIST)
    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    @OneToMany(mappedBy = "location", cascade = CascadeType.PERSIST)
    public List<Project> getProjects() {
        return projects;
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }

    @OneToMany(mappedBy = "origin", cascade = CascadeType.PERSIST)
    public List<Grid> getGrids() {
        return grids;
    }

    public void setGrids(List<Grid> grids) {
        this.grids = grids;
    }

    @Override
    public String toString() {
        return this.getLatitude() + ", " + this.getLongitude();
    }

    public String gimmeEmbedUrl() {
        return googleMapsUrlPrefix + latitude + "," + longitude + googleMapsUrlSuffix;
    }

}
