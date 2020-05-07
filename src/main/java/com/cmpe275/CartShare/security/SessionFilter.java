package com.cmpe275.CartShare.security;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

public class SessionFilter implements Filter {

    private boolean isCookieAdded = false;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        Cookie[] allCookies = req.getCookies();
        if (allCookies != null) {
            Cookie session = Arrays.stream(allCookies).filter(x -> x.getName().equals("JSESSIONID")).findFirst().orElse(null);

            if (session != null && !isCookieAdded) {
                session.setHttpOnly(true);
                session.setSecure(true);
                res.addCookie(session);
                isCookieAdded = true;
            }
        }
        chain.doFilter(req, res);
    }
}