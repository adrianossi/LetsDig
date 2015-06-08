package org.letsdig.app.controllers;

import org.letsdig.app.models.LatLong;
import org.letsdig.app.models.User;
import org.letsdig.app.models.dao.LatLongDao;
import org.letsdig.app.models.dao.UserDao;
import org.letsdig.app.models.util.PasswordHash;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by adrian on 6/2/15.
 */

@Controller
public class ProfileController extends AbstractLetsDigController {

    @RequestMapping(value = "/profile")
    public String profile(HttpServletRequest request, Model model) {

        // get user's data from db
        User user = getUserFromSession(request);

        // add new info to model
        model.addAttribute("username", user.getUsername());

        model.addAttribute("displayName", user.gimmeDisplayName());

        if (user.getFirstName() != null) {
            model.addAttribute("firstName", user.getFirstName());
        } else {
            model.addAttribute("firstName", "empty");
        }

        if (user.getLastName() != null) {
            model.addAttribute("lastName", user.getLastName());
        } else {
            model.addAttribute("lastName", "empty");
        }

        if (user.getLocation() != null) {
            model.addAttribute("latitude", user.getLocation().getLatitude());
            model.addAttribute("longitude", user.getLocation().getLongitude());
        } else {
            model.addAttribute("latitude", "empty");
            model.addAttribute("longitude", "empty");
        }

            /*
            LatLong location = latLongDao.findByUid(user.getLocationId());
            model.addAttribute("latitude", location.getLatitude());
            model.addAttribute("longitude", location.getLongitude());
        } else {
            model.addAttribute("latitude", "empty");
            model.addAttribute("longitude", "empty");
        }*/

        // display the profile template
        return "profile";
    }

    @RequestMapping(value = "/profileedit", method = RequestMethod.GET)
    public String profileEdit(HttpServletRequest request, Model model) {

        // get user's data from db
        User user = getUserFromSession(request);

        // put the data into the model
        model.addAttribute("displayName", user.gimmeDisplayName());
        model.addAttribute("username", user.getUsername());
        model.addAttribute("firstName", user.getFirstName());
        model.addAttribute("lastName", user.getLastName());

        // TODO add location to profile edit display

        /*
        if (user.getLocationId() != 0) {
            LatLong location = latLongDao.findByUid(user.getLocationId());
            model.addAttribute("latitude", location.getLatitude());
            model.addAttribute("longitude", location.getLongitude());
        } else {
            model.addAttribute("latitude", "empty");
            model.addAttribute("longitude", "empty");
        }*/

        // display the profileedit template
        return "profileedit";
    }

    @RequestMapping(value = "/profileedit", method = RequestMethod.POST)
    public String profileEdit(
            String username,
            String firstName,
            String lastName,
            String latitude,
            String longitude,
            HttpServletRequest request,
            Model model) {

        // get user's data from db
        User user = getUserFromSession(request);
        
        // check each field for user input; if empty-->skip, else-->set
        if (username != "" && !username.equals(user.getUsername())) {
            user.setUsername(username);
        }

        if (firstName != "" && !firstName.equals(user.getFirstName())) {
            user.setFirstName(firstName);
        }

        if (lastName != "" && !lastName.equals(user.getLastName())) {
            user.setLastName(lastName);
        }

        if (latitude != "" && user.getLocation().getLatitude() != Double.valueOf(latitude)) {
            user.getLocation().setLatitude(Double.valueOf(latitude));
        }

        if (longitude != "" && user.getLocation().getLongitude() != Double.valueOf(longitude)) {
            user.getLocation().setLongitude(Double.valueOf(longitude));
        }

        /*
        if (latitude != "" || longitude != "") {
            LatLong location = latLongDao.findByUid(user.getLocationId());
            if (location != null) {
                location.setLatitude(Double.valueOf(latitude));
                location.setLongitude(Double.valueOf(longitude));
                latLongDao.save(location);
                model.addAttribute("latitude", location.getLatitude());
                model.addAttribute("longitude", location.getLongitude());
            } else {
                location = new LatLong(Double.valueOf(latitude), Double.valueOf(longitude));
                latLongDao.save(location);
                user.setLocationId(location.getUid());
                model.addAttribute("latitude", location.getLatitude());
                model.addAttribute("longitude", location.getLongitude());
            }
        }*/

        /* OLD LAT/LONG UPDATER
        && Double.valueOf(latitude) != location.getLatitude()) {
            user.setLatitude(Double.valueOf(latitude));
        }

        if (longitude != "" && Double.valueOf(longitude) != user.getLongitude()) {
            user.setLongitude(Double.valueOf(longitude));
        }*/

/*
        if (newPassword.equals(null)) {
            model.addAttribute("passwordmessage", "(hidden)");
        } else if (!newPassword.equals(newPasswordConfirm)) {
            model.addAttribute("passwordmessage", "Password and confirmation do not match.");
        } else {
            user.setHash(PasswordHash.getHash(newPassword));
            model.addAttribute("passwordmessage", "Password updated");
        }
*/
        // save updated user data to db
        userDao.save(user);

        // add new info to model
        model.addAttribute("username", user.getUsername());
        model.addAttribute("firstName", user.getFirstName());
        model.addAttribute("lastName", user.getLastName());


        // display the profile template
        return "profile";

    }

    @RequestMapping(value = "home")
    public String home(HttpServletRequest request, Model model) {

        // get user's data from db
        User user = getUserFromSession(request);

        // add name to model
        model.addAttribute("displayName", user.gimmeDisplayName());

        return "home";
    }

    @RequestMapping(value = "/changepwd", method = RequestMethod.GET)
    public String changPwd (HttpServletRequest request, Model model) {

        // get user's data from db
        User user = getUserFromSession(request);

        // add name to model
        model.addAttribute("displayName", user.gimmeDisplayName());

        return "changepwd";
    }

    @RequestMapping(value = "/changepwd", method = RequestMethod.POST)
    public String changePwd(String oldPassword, String newPassword, String confirmNewPassword, HttpServletRequest request, Model model) {

        // get user's data from db
        User user = getUserFromSession(request);

        // check for validity of old password
        if (!PasswordHash.isValidPassword(oldPassword, user.getHash())) {

            // TODO: delete this line, or implement cs50 error message display method
            // return this.displayError("Invalid password.", model);

            model.addAttribute("message", "Invalid password");
            return "error";

        // verify match btw new pwd and confirmation
        } else if (!newPassword.equals(confirmNewPassword)) {

            return this.displayError("Password and confirmation don't match.", model);

        } else {

            // create and set the hash
            String hash = PasswordHash.getHash(newPassword);
            user.setHash(hash);

            // save changes
            userDao.save(user);

            model.addAttribute("message", "Password updated.");
            return "confirm";
        }
    }

    @RequestMapping(value = "/map")
    public String map(HttpServletRequest request, Model model) {

        // get user from db
        User user = getUserFromSession(request);

        // get lat long info
        // LatLong location = latLongDao.findByUid(user.getLocationId());

        if (user.getLocation() != null) {
            return user.getLocation().putOnMap();
        } else {
            model.addAttribute("message", "Location data not found.");
            return "error";
        }
    }
}
