package org.letsdig.app.controllers;

import org.letsdig.app.models.LatLong;
import org.letsdig.app.models.Project;
import org.letsdig.app.models.ProjectAccessException;
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

        User user = getUserFromSession(request);

        if (user == null) {
            model.addAttribute("message", "Error loading user data.");
            return "error";
        }

        model.addAttribute("user", user);

        Project project;

        try {
            project = getActiveProject(request);
            model.addAttribute("project", project);

        } catch (ProjectAccessException e) {
            e.printStackTrace();
        }

        if (user.getLocation() != null) {
            model.addAttribute("location", user.getLocation());
        }

        // display the profile template
        return "profile";
    }

    @RequestMapping(value = "/profileedit", method = RequestMethod.GET)
    public String profileEdit(HttpServletRequest request, Model model) {

        User user = getUserFromSession(request);

        if (user == null) {
            model.addAttribute("message", "Error loading user data.");
            return "error";
        }

        model.addAttribute("user", user);

        Project project;

        try {
            project = getActiveProject(request);
            model.addAttribute("project", project);

        } catch (ProjectAccessException e) {
            e.printStackTrace();
        }

        if (user.getLocation() != null) {
            model.addAttribute("location", user.getLocation());
        }

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

        User user = getUserFromSession(request);

        if (user == null) {
            model.addAttribute("message", "Error loading user data.");
            return "error";
        }

        model.addAttribute("user", user);

        Project project;

        try {
            project = getActiveProject(request);
            model.addAttribute("project", project);

        } catch (ProjectAccessException e) {
            e.printStackTrace();
        }

        // check each field for user input; if empty-->skip, else-->set
        // USERNAME
        if (!username.equals("") && !username.equals(user.getUsername())) {
            user.setUsername(username);
        }

        // FIRSTNAME
        if (!firstName.equals("") && !firstName.equals(user.getFirstName())) {
            user.setFirstName(firstName);
        }

        // LASTNAME
        if (!lastName.equals("") && !lastName.equals(user.getLastName())) {
            user.setLastName(lastName);
        }

        // LOCATION: LAT/LONG
        if (!(latitude.equals("") && longitude.equals(""))) {

            // check if user's input is valid for lat/long
            if (!LatLongUtils.isValidLatLong(latitude, longitude)) {
                model.addAttribute("message", "Invalid location. Please try again.");
                return "error";
            }

            // get the LatLong from the db
            LatLong newLocation = this.getOrCreateLatLong(Double.valueOf(latitude), Double.valueOf(longitude));

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

        User user = getUserFromSession(request);

        if (user == null) {
            model.addAttribute("message", "Error loading user data.");
            return "error";
        }

        model.addAttribute("user", user);

        Project project;

        try {
            project = getActiveProject(request);
            model.addAttribute("project", project);

        } catch (ProjectAccessException e) {
            e.printStackTrace();
        }

        // add name to model
        model.addAttribute("displayName", user.toString());

        return "changepwd";
    }

    @RequestMapping(value = "/changepwd", method = RequestMethod.POST)
    public String changePwd(String oldPassword, String newPassword, String confirmNewPassword, HttpServletRequest request, Model model) {

        User user = getUserFromSession(request);

        if (user == null) {
            model.addAttribute("message", "Error loading user data.");
            return "error";
        }

        model.addAttribute("user", user);

        Project project;

        try {
            project = getActiveProject(request);
            model.addAttribute("project", project);

        } catch (ProjectAccessException e) {
            e.printStackTrace();
        }

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
}
