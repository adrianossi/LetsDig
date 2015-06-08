package org.letsdig.app.models;

import org.springframework.web.bind.annotation.RequestMapping;

import javax.persistence.*;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    public LatLong (double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
        users = new ArrayList<User>();
        projects = new ArrayList<Project>();
    }

    public LatLong () {}

    // static property for map redirect
    private static final String baseGoogleMapsUrl = "https://www.google.com/maps/?q=";

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

    public String gimmeLocationString() {
        return this.getLatitude() + ", " + this.getLongitude();
    }

    public String putOnMap() {
            return "redirect:" + baseGoogleMapsUrl + this.getLatitude() + "," + this.getLongitude();
    }

}
