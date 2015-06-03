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
        List<String> authPages = Arrays.asList("/login", "/register", "/logout");

        // check if requested page requires authentification...
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
    }
}
