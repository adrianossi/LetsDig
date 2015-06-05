package org.letsdig.app.controllers;

import org.letsdig.app.models.User;
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

    // static proerties for map redirect
    private static final String baseGoogleMapsUrl = "https://www.google.com/maps/?q=";
    //private static final String googleZoomLevel = ",8z";

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

        if (user.gimmeLocation() != null) {
            model.addAttribute("latitude", user.getLatitude());
            model.addAttribute("longitude", user.getLongitude());
        } else {
            model.addAttribute("latitude", "empty");
            model.addAttribute("longitude", "empty");
        }

        return "profile";
    }

    /*
        @RequestMapping(value = "/redirect", method = RequestMethod.GET)
        public void method(HttpServletResponse httpServletResponse) {
            httpServletResponse.setHeader("Location", projectUrl);
        }
    */
    // display the profile template

    @RequestMapping(value = "/map")
    public String map(HttpServletRequest request) {

        // get user's data from db
        User user = getUserFromSession(request);

        return "redirect:" + baseGoogleMapsUrl + user.getLatitude() + "," + user.getLongitude();
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
        model.addAttribute("latitude", user.getLatitude());
        model.addAttribute("longitude", user.getLongitude());

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
        
        if (username != "" && !username.equals(user.getUsername())) {
            user.setUsername(username);
        }

        if (firstName != "" && !firstName.equals(user.getFirstName())) {
            user.setFirstName(firstName);
        }

        if (lastName != "" && !lastName.equals(user.getLastName())) {
            user.setLastName(lastName);
        }

        if (latitude != "" && Double.valueOf(latitude) != user.getLatitude()) {
            user.setLatitude(Double.valueOf(latitude));
        }

        if (longitude != "" && Double.valueOf(longitude) != user.getLongitude()) {
            user.setLongitude(Double.valueOf(longitude));
        }

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
        model.addAttribute("latitude", user.getLatitude());
        model.addAttribute("longitude", user.getLongitude());

        // display the profile template
        return "profile";

    }

    @RequestMapping(value = "home")
    public String home(HttpServletRequest request, Model model) {
        User user = getUserFromSession(request);
        String displayName;

        if (user.getFirstName() == null) {
            displayName = user.getUsername();
        } else {
            displayName = user.toString();
        }

        model.addAttribute("displayName", displayName);

        return "home";
    }
}
