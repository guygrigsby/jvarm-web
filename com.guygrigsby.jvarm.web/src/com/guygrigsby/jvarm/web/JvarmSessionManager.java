package com.guygrigsby.jvarm.web;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * Application Lifecycle Listener implementation class JvarmSessionManager
 *
 */
@WebListener
public class JvarmSessionManager implements HttpSessionListener {

	/**
	 * @see HttpSessionListener#sessionCreated(HttpSessionEvent)
	 */
	public void sessionCreated(HttpSessionEvent event) {
		HttpSession session = event.getSession();
		ServletContext context = session.getServletContext();
		Map<String, HttpSession> activeSessions = (Map<String, HttpSession>) context.getAttribute("activeSessions");
		activeSessions.put(session.getId(), session);
	}

	/**
	 * @see HttpSessionListener#sessionDestroyed(HttpSessionEvent)
	 */
	public void sessionDestroyed(HttpSessionEvent event) {
		HttpSession session = event.getSession();
		ServletContext context = session.getServletContext();
		Map<String, HttpSession> activeSessions = (Map<String, HttpSession>) context.getAttribute("activeSessions");
		activeSessions.remove(session.getId());
	}
}
