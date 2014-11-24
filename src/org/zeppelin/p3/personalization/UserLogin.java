package org.zeppelin.p3.personalization;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class UserLogin
 */
@WebServlet(urlPatterns = { "/UserLogin" })
public class UserLogin extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public UserLogin() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try {
			Integer id = Integer.parseInt(request.getHeader("userId"));
			System.out.println("Logged In User:" + id);
		} catch (NumberFormatException e) {
			/*
			 * We have a new user. Add to db.
			 */
			String userName = request.getHeader("userId");
			if (userName.equals("null")) {

			} else
				System.out.println("New User: " + userName);
		} catch (Exception e) {

		}

		// Return stuff.
		PrintWriter out = response.getWriter();
		out.println("");
		out.close();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
