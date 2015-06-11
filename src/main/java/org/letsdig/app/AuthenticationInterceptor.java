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
 * Created by adrian on 6/1/15.
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
            User user = userDao.findByUid(userId);

            if (user == null) {
                isPresentValidUser = false;
            }
        }

        /*
         * Decide what to do with request based on
         * isPublicRequestedUri and isPresentValidUser
         * (4 possibilities: T/T, T/F, F/T, F/F)
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

    /*
        TODO: delete this old preHandle code?

        // check if requested page requires authentication...
        if (!authPages.contains(request.getRequestURI())) {

            // get the user id from the session
            Integer userId = (Integer)request.getSession().getAttribute(AbstractLetsDigController.userSessionKey);

            // verify that the session had a user id in it...
            if (userId == null) {

                // ...if not, redirect to login page
                response.sendRedirect("/login");
                return false;
            }

            // a user was present in the session, so find it in the db
            User user = userDao.findByUid(userId);

            // verify the db's response
            if (user == null) {

                // db responded that user doesn't exist, so redirect to login
                response.sendRedirect("/login");
                return false;
            }

        }

        // both checks pass, so a valid user is present,
        // so continue to process the request
        return true;

    */

    }
}
