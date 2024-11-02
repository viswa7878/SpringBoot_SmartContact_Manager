package com.springboot.helper;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpSession;

@Component
public class SessionHelper {

	public void removeMessageFromSession() {        
	    try {
	        System.out.println("Removing message from session");

	        // Get the current HTTP session
	        ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
	        if (sra != null) {
	            HttpSession session = sra.getRequest().getSession();
	            
	            // Remove the 'message' attribute from the session
	            session.removeAttribute("message");
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        System.out.println("Error removing message from session: " + e.getMessage());
	    }
	}


}
