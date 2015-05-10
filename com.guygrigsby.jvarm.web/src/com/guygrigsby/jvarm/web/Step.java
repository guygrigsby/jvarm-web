package com.guygrigsby.jvarm.web;

import java.io.IOException;

import javafx.application.Platform;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;

import com.guygrigsby.jvarm.core.ArmProgram;
import com.guygrigsby.jvarm.core.Registers;

/**
 * Servlet implementation class Step
 */
@WebServlet("/Jvarm/Step")
public class Step extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Step() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		ArmProgram program = (ArmProgram) session.getAttribute(Jvarm.PROGRAM);
		if (program != null) {
			try {
				Registers registers = (Registers) session.getAttribute(Jvarm.REGISTERS);

				program.step(registers);
				JSONObject json = new JSONObject(registers);
				String registersJson = json.toString();
				response.setContentType("json");
				response.setCharacterEncoding("UTF-8");
				response.getWriter().write(registersJson);
			} catch (Exception e) {
				//meant to catch all so an error in the core does not crash the GUI
				response.setContentType("text/plain");
				response.setCharacterEncoding("UTF-8");
				response.getWriter().write(e.getMessage());
			}
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
