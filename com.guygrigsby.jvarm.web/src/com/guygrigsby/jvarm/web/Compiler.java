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
