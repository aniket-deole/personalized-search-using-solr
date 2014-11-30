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
@WebServlet("/LikingScoreRegisterer")
public class LikingScoreRegisterer extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public LikingScoreRegisterer() {
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
		String likingScore = request.getHeader("docRating");
		MySQLAccess dao = new MySQLAccess();
		// Fetch the Logged In userId from the session
		Integer loggedInUserId = (Integer) request.getSession().getAttribute(
				"loggedInUserId");
		try {
			// We assume that the click count is already updated and hence
			// we don't touch it.
			dao.updateLikingScoreForDocument(loggedInUserId, docId,
					Integer.parseInt(likingScore));
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
