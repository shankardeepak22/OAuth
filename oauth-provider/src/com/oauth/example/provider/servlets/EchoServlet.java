package com.oauth.example.provider.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

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
 * Servlet implementation class EchoServlet
 */
@WebServlet("/echo")
public class EchoServlet extends HttpServlet {
	 @Override
	    protected void doPost(HttpServletRequest request, HttpServletResponse response)
	            throws ServletException, IOException
	    {
	        doGet(request, response);
	    }

	    @Override
	    protected void doGet(HttpServletRequest request, HttpServletResponse response)
	            throws IOException, ServletException {
	        try{
	            OAuthMessage requestMessage = OAuthServlet.getMessage(request, null);
	            OAuthAccessor accessor = SampleOAuthProvider.getAccessor(requestMessage);
	            SampleOAuthProvider.VALIDATOR.validateMessage(requestMessage, accessor);
	            String userId = (String) accessor.getProperty("user");
	            
	            response.setContentType("text/plain");
	            PrintWriter out = response.getWriter();
	            out.println("[Your UserId:" + userId + "]");
	            for (Object item : request.getParameterMap().entrySet()) {
	                Map.Entry parameter = (Map.Entry) item;
	                String[] values = (String[]) parameter.getValue();
	                for (String value : values) {
	                    out.println(parameter.getKey() + ": " + value);
	                }
	            }
	            out.close();
	            
	        } catch (Exception e){
	            SampleOAuthProvider.handleException(e, request, response, false);
	        }
	    }

	    private static final long serialVersionUID = 1L;
}
