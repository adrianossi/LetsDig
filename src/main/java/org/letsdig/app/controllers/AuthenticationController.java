package org.letsdig.app.controllers;

import org.letsdig.app.models.User;
import org.letsdig.app.models.util.PasswordHash;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by adrian on 6/1/15.
 */

@Controller
public class AuthenticationController extends AbstractLetsDigController {

    @RequestMapping(value = "/")
    public String index() {
        return "redirect:login";
    }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String register(Model model) {
        model.addAttribute("title", "Create account");
        return "register";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String register(String username, String password, String confirmPassword, HttpServletRequest request, Model model) {

        // check if the username is taken
        User existingUser = userDao.findByUsername(username);

        if (existingUser != null) {

            // db found that username, so display error
            return this.displayError("The username " + username + " is already taken.", model);

        } else if (!password.equals(confirmPassword)) {

            // user mistyped password & confirmation, so display error
            return this.displayError("Password and confirmation don't match.", model);
        }

        // Validation succeeded. Make a new user and save it to the db.
        User newUser = new User(username, password);
        userDao.save(newUser);

        // Log the user into the session
        request.getSession().setAttribute(userSessionKey, newUser.getId());

        // Store user's name
        model.addAttribute("displayName", newUser.toString());

        // display login landing page
        return "redirect:projects";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(Model model) {
        model.addAttribute("title", "Login");
        return "login";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(String username, String password, HttpServletRequest request, Model model) {

        // look up user in db
        User existingUser = userDao.findByUsername(username);

        // verify that the db returned a user; if not, display error
        if (existingUser == null) {

            // return this.displayError("Invalid username.", model);

            model.addAttribute("message", "Invalid username.");
            return "error";

        // verify that user entered valid password; if not, display error
        } else if (!PasswordHash.isValidPassword(password, existingUser.getHash())) {

            model.addAttribute("message", "Invalid password");
            return "error";
        }

        // User and password are verified, so log the user into the session
        request.getSession().setAttribute(userSessionKey, existingUser.getId());

        // add user's name to model
        model.addAttribute("displayName", existingUser.toString());

        // display login landing page
        return "redirect:projects";
    }

    @RequestMapping(value = "/logout")
    public String logout(HttpServletRequest request) {
        request.getSession().invalidate();
        return "redirect:login";
    }
}
