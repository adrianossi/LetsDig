package org.letsdig.app;

import org.letsdig.app.controllers.AbstractLetsDigController;
import org.letsdig.app.models.User;
import org.letsdig.app.models.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Adrian Ossi on 6/1/15.
 *
 * Based on a template by Chris Bay, except where indicated.
 */
public class AuthenticationInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    UserDao userDao;

    // method for Spring to catch all requests before passing them
    // to the appropriate controller
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {

        // allow open access to logout
        // TODO? Add "/about" page here
        if (request.getRequestURI().equals("/logout")) {
            return true;
        }

        // list of publicly accessible authentication pages
        List<String> authPages = Arrays.asList("/", "/login", "/register", "/login.html", "/register.html");

        // is requested page in public list? Save for future checks
        boolean isPublicRequestedUri = authPages.contains(request.getRequestURI());

        // get the user id from the session
        Integer userId = (Integer)request.getSession().getAttribute(AbstractLetsDigController.userSessionKey);

        // is user logged in? Save for future checks
        boolean isPresentValidUser = userId != null;

        // double check user validity by querying db
        if (isPresentValidUser) {
            User user = userDao.findById(userId);

            if (user == null) {
                isPresentValidUser = false;
            }
        }

        /*
         * Decide what to do with request based on
         * isPublicRequestedUri and isPresentValidUser
         * (4 possibilities: T/T, T/F, F/T, F/F)
         *
         * by Adrian Ossi
         */

        if (isPublicRequestedUri) {
        // requested page is PUBLIC, so check for authenticated user

            if (isPresentValidUser) {
            // authenticated=YES, so redirect to projects page

                response.sendRedirect("/projects");
                return false;

            } else {
            // authenticated=NO, so allow public page to be viewed

                return true;
            }

        } else {
        // requested page is PRIVATE, so check for authenticated user

            if (isPresentValidUser) {
            // authenticated=YES, so allow private page to be viewed

                return true;

            } else {
            // authenticated=NO, so redirect to login

                response.sendRedirect("/login");
                return false;
            }

        }
    }
}
