package com.oauth.example.provider.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.oauth.commons.OAuthAccessor;
import com.oauth.commons.OAuthMessage;
import com.oauth.example.provider.SampleOAuthProvider;
import com.oauth.provider.server.OAuthServlet;

/**
 * Servlet implementation class AppCreationServlet
 */
@WebServlet(description = "Servelt used to create the Apps Authorized by the provider", urlPatterns = { "/AppCreationServlet" })
public class AppCreationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AppCreationServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		  try{
	            OAuthMessage requestMessage = OAuthServlet.getMessage(request, null);
	            
	            OAuthAccessor accessor = SampleOAuthProvider.getAccessor(requestMessage);
	           
	            if (Boolean.TRUE.equals(accessor.getProperty("authorized"))) {
	                // already authorized send the user back
	               // returnToConsumer(request, response, accessor);
	            } else {
	               // sendToAuthorizePage(request, response, accessor);
	            }
	        
	        } catch (Exception e){
	            SampleOAuthProvider.handleException(e, request, response, true);
	        }
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
