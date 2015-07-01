package com.oauth.example.consumer;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.oauth.commons.OAuth;

/**
 * Servlet implementation class UserAuthorizationStub
 */
@WebServlet({ "/UserAuthorizationStub", "/OAuth/UserAuthorizationStub/*" })
public class UserAuthorizationStub extends HttpServlet {
	public static final String PATH = "/OAuth/UserAuthorizationStub";

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        String callback = request.getParameter("oauth_callback");
        String token = request.getParameter("oauth_token");
        if (token != null) {
            callback = OAuth.addParameters(callback, "oauth_token", token);
        }
        response.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);
        response.setHeader("Location", callback);
    }

    private static final long serialVersionUID = 1L;

}
