package com.guygrigsby.jvarm.web;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.guygrigsby.jvarm.core.ArmProgram;
import com.guygrigsby.jvarm.core.ArmSourceCompiler;
import com.guygrigsby.jvarm.core.CompilerInfoCollector;
import com.guygrigsby.jvarm.core.InfoCollector;
import com.guygrigsby.jvarm.core.Registers;

/**
 * Servlet implementation class Jvarm
 */
@WebServlet("/jvarm")
public class Jvarm extends HttpServlet {
	
	private static Logger logger = LogManager.getLogger();

	private static final long serialVersionUID = 1L;
	public static final String PROGRAM = "com.guygrigsby.jvarm.web.program";
	public static final String REGISTERS = "com.guygrigsby.jvarm.web.registers";

       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Jvarm() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Map<String, HttpSession> activeSessions = (Map<String, HttpSession>) request.getServletContext().getAttribute("activeSessions");
        Cookie sessionIdCookie = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("sessionId")) {
                	sessionIdCookie = cookie;
                    break;
                }
            }
        }
        if (sessionIdCookie == null) {
			response.setContentType("text/plain");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write("no cookie set for source code");
			return;
        }
        
        String sessionId = sessionIdCookie.getValue();
		HttpSession session = activeSessions.get(sessionId);
		if (session == null) {
			return;
		}
		logger.debug("Session ID = {}", session.getId());
		ArmProgram program = (ArmProgram) session.getAttribute(Jvarm.PROGRAM);
		if (program != null) {
			try {
				Registers registers = (Registers) session.getAttribute(Jvarm.REGISTERS);

				program.step(registers);
				JSONObject json = new JSONObject();
				JSONArray regArray = new JSONArray();
				json.put("registers", regArray);
				for (Map.Entry<String, Integer> entry : registers.entrySet()) {
					JSONObject o = new JSONObject();
					o.put(entry.getKey(), entry.getValue());
					regArray.put(o);
				}
				response.setContentType("json");
				response.setCharacterEncoding("UTF-8");
				logger.debug("response being sent after stepping is {}", json.toString());
				response.getWriter().write(json.toString());
			} catch (Exception e) {
				//meant to catch all so an error in the core does not crash the GUI
				logger.error(e);
				response.setContentType("text/plain");
				response.setCharacterEncoding("UTF-8");
				response.getWriter().write(e.getMessage());
			}
		} else {
			logger.error("ARM Progam stored in session is null. session = " + session);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		logger.debug("Session ID = {}", session.getId());
		String sourceCode = request.getParameter("ARMsource");
		logger.trace("ARM source for post is {}", sourceCode);
		InputStream stream = new ByteArrayInputStream(
				sourceCode.getBytes(StandardCharsets.UTF_8));

		InfoCollector infoCollector = new CompilerInfoCollector();

		ArmProgram program = new ArmSourceCompiler().compile(stream,
				infoCollector);

		String errors = infoCollector.getErrorsAsString();
		if (errors.isEmpty()) {
			response.setContentType("text/plain");
			response.setCharacterEncoding("UTF-8");
			Comparator<String> comp = (o1, o2) -> {
				if (isFlag(o1) && isFlag(o2)) {
					return o1.compareTo(o2);
				}
				if (isFlag(o1)) {
					return 1;
				} else if (isFlag(o2)) {
					return -1;
				}
				Integer i1 = Integer.parseInt(o1.substring(1));
				Integer i2 = Integer.parseInt(o2.substring(1));
				return i1.compareTo(i2);
			};
			Map<String, Integer> regMap = new TreeMap<String, Integer>(comp);
			Registers registers = new Registers(regMap);
			session.setAttribute(Jvarm.PROGRAM, program);
			session.setAttribute(Jvarm.REGISTERS, registers);
	        Cookie sessionIdCookie = new Cookie("sessionId", session.getId());
            // setting cookie to expiry in 60 mins
	        sessionIdCookie.setMaxAge(60 * 60);
            response.addCookie(sessionIdCookie);
			response.getWriter().write("Compiled Successfully. You may step now.");
		} else {
			response.setContentType("text/plain");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(errors);
		}
	}
	
	private boolean isFlag(String name) {
		return name.equals("N") || name.equals("Z") || name.equals("C") || name.equals("V");
	}

}
