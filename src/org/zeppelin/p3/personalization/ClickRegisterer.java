package org.zeppelin.p3.personalization;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.zeppelin.p3.db.MySQLAccess;

/**
 * Servlet implementation class ClickRegisterer
 */
@WebServlet("/ClickRegisterer")
public class ClickRegisterer extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ClickRegisterer() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String docId = request.getHeader("docId");
		MySQLAccess dao = new MySQLAccess();
		// Fetch the Logged In userId from the session
		Integer loggedInUserId = (Integer) request.getSession().getAttribute(
				"loggedInUserId");
		try {
			dao.updateClickCountForDocument(loggedInUserId, docId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(docId);
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
