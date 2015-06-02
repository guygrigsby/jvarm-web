package com.guygrigsby.jvarm.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;

/**
 * Application Lifecycle Listener implementation class JvarmServletContextListener
 *
 */
@WebListener
public class JvarmServletContextListener implements ServletContextListener {

    /**
     * Default constructor. 
     */
    public JvarmServletContextListener() {
        // TODO Auto-generated constructor stub
    }

	/**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent event)  { 
         // TODO Auto-generated method stub
    }

	/**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent event)  { 
		ServletContext context = event.getServletContext();
		Map<String, HttpSession> activeSessions = new HashMap<String, HttpSession>();
		context.setAttribute("activeSessions", activeSessions);
    }
	
}
