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
public class NavigationController extends AbstractLetsDigController {

    @RequestMapping(value = "/profileedit", method = RequestMethod.GET)
    public String profileEdit(HttpServletRequest request, Model model) {

        // get user's data from db
        User user = getUserFromSession(request);

        // put the data into the model
        model.addAttribute("displayName", user.gimmeDisplayName());
        model.addAttribute("username", user.getUsername());
        model.addAttribute("firstName", user.getFirstName());
        model.addAttribute("lastName", user.getLastName());

        // display the profileedit template
        return "profileedit";
    }

    @RequestMapping(value = "/profileedit", method = RequestMethod.POST)
    public String profileEdit(String username, String firstName, String lastName, String newPassword, String newPasswordConfirm, HttpServletRequest request, Model model) {
        // username, firstName, lastName,
        // newPassword, newPasswordConfirm, request, model

        // get user's data from db
        User user = getUserFromSession(request);
        
        if (!username.equals(user.getUsername())) {
            user.setUsername(username);
        }

        if (!firstName.equals(user.getFirstName())) {
            user.setFirstName(firstName);
        }

        if (!lastName.equals(user.getLastName())) {
            user.setLastName(lastName);
        }

        if (newPassword.equals(null)) {
            model.addAttribute("passwordmessage", "(hidden)");
        } else if (!newPassword.equals(newPasswordConfirm)) {
            model.addAttribute("passwordmessage", "Password and confirmation do not match.");
        } else {
            user.setHash(PasswordHash.getHash(newPassword));
            model.addAttribute("passwordmessage", "Password updated");
        }

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
