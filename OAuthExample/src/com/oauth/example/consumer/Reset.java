package com.oauth.example.consumer;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Reset
 */
@WebServlet("/Reset")
public class Reset extends HttpServlet {
	 /** Clear all the OAuth accessor cookies and redirect to another page. */
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        try {
            CookieConsumer.removeAccessors(new CookieMap(request, response));
            String nextPage = request.getParameter("nextPage");
            if (nextPage == null) {
                nextPage = request.getHeader("Referer");
                if (nextPage == null) {
                    nextPage = request.getContextPath(); // home page
                }
            }
            throw new RedirectException(nextPage);
        } catch (Exception e) {
            CookieConsumer.handleException(e, request, response, null);
        }
    }

    private static final long serialVersionUID = 1L;

}
