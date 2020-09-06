package com.obss.humanresources.configuration;

import com.obss.humanresources.model.User;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;

public class SessionInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle (HttpServletRequest request,
                              HttpServletResponse response,
                              Object handler) throws Exception {
        HttpSession session = request.getSession(true);
        if (session.getAttribute("user") == null) {
            session.setAttribute("user", new User("-1", null, User.USER_TYPE.VISITOR, null));
        }
        return true;
    }

}
