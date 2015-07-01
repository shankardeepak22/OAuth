/**
 * 
 */
package com.oauth.example.consumer;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class CookieMap {
	
	 public CookieMap(HttpServletRequest request, HttpServletResponse response) {
	        this.response = response;
	        this.path = request.getContextPath();
	        Cookie[] cookies = request.getCookies();
	        if (cookies != null) {
	            for (Cookie cookie : cookies) {
	                if (cookie != null) {
	                    name2value.put(cookie.getName(), cookie.getValue());
	                }
	            }
	        }
	    }

	    private final HttpServletResponse response;

	    private final String path;

	    private final Map<String, String> name2value = new HashMap<String, String>();

	    public String get(String name) {
	        return name2value.get(name);
	    }

	    public void put(String name, String value) {
	        if (value == null) {
	            remove(name);
	        } else if (!value.equals(name2value.get(name))) {
	            Cookie c = new Cookie(name, value);
	            c.setPath(path);
	            response.addCookie(c);
	            name2value.put(name, value);
	        }
	    }

	    public void remove(String name) {
	        if (name2value.containsKey(name)) {
	            Cookie c = new Cookie(name, "");
	            c.setMaxAge(0);
	            c.setPath(path);
	            response.addCookie(c);
	            name2value.remove(name);
	        }
	    }

	    public Set<String> keySet() {
	        Set<String> set = Collections.unmodifiableSet(name2value.keySet());
	        return set;
	    }

	    public String toString() {
	        return name2value.toString();
	    }

}
