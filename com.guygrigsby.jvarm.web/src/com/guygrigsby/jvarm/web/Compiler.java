package com.guygrigsby.jvarm.web;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.guygrigsby.jvarm.core.ArmProgram;
import com.guygrigsby.jvarm.core.ArmSourceCompiler;
import com.guygrigsby.jvarm.core.CompilerInfoCollector;
import com.guygrigsby.jvarm.core.InfoCollector;
import com.guygrigsby.jvarm.core.Registers;

/**
 * Servlet implementation class Compiler
 */
@WebServlet("/Jvarm/Compile")
public class Compiler extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Compiler() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String sourceCode = request.getParameter("ARMsource");
		InputStream stream = new ByteArrayInputStream(
				sourceCode.getBytes(StandardCharsets.UTF_8));

		InfoCollector infoCollector = new CompilerInfoCollector();
		HttpSession session = request.getSession();

		ArmProgram program = new ArmSourceCompiler().compile(stream,
				infoCollector);

		String errors = infoCollector.getErrorsAsString();
		if (errors.isEmpty()) {
			response.setContentType("text/plain");
			response.setCharacterEncoding("UTF-8");
			Map<String, Integer> regMap = new HashMap<String, Integer>();
			Registers registers = new Registers(regMap);
			session.setAttribute(Jvarm.PROGRAM, program);
			session.setAttribute(Jvarm.REGISTERS, registers);
		} else {
			response.setContentType("text/plain");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(errors);
		}

	}

}
