package org.letsdig.app.controllers;

import org.letsdig.app.models.LatLong;
import org.letsdig.app.models.User;
import org.letsdig.app.models.util.LatLongUtils;
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
        // USERNAME
        if (username != "" && !username.equals(user.getUsername())) {
            user.setUsername(username);
        }

        // FIRSTNAME
        if (firstName != "" && !firstName.equals(user.getFirstName())) {
            user.setFirstName(firstName);
        }

        // LASTNAME
        if (lastName != "" && !lastName.equals(user.getLastName())) {
            user.setLastName(lastName);
        }

        // LOCATION: LAT/LONG
        if (!(latitude == "" && longitude == "")) {

            // check if user's input is valid for lat/long
            if (!LatLongUtils.isValidLatLong(latitude, longitude)) {
                model.addAttribute("message", "Invalid location. Please try again.");
                return "error";
            }

            // get the LatLong from the db
            LatLong newLocation = LatLongUtils.lookup(Double.valueOf(latitude), Double.valueOf(longitude));

            // set LatLong as user's location and save both
            latLongDao.save(newLocation);
            user.setLocation(newLocation);

            // test data 40.3496462, -74.6596824
        }

        // save updated user data to db
        userDao.save(user);

        // redirect to (updated) profile
        return "redirect:profile";

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
