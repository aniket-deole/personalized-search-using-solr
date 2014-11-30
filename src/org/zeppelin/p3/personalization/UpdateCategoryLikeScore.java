package org.zeppelin.p3.personalization;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.zeppelin.p3.db.MySQLAccess;

/**
 * Servlet implementation class ClickRegisterer
 */
@WebServlet("/UpdateCategoryLikeScore")
public class UpdateCategoryLikeScore extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public UpdateCategoryLikeScore() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		 String categoriesAndLikeScoreMapS =
		 request.getHeader("categoriesAndLikeScoreMap");
		MySQLAccess dao = new MySQLAccess();
		// Fetch the Logged In userId from the session
		Integer loggedInUserId = (Integer) request.getSession().getAttribute(
				"loggedInUserId");
		// TODO fetch it from User for each category
		Map<String, Integer> categoriesAndLikeScoreMap = new HashMap<String, Integer>();
		try {
			for (String category : categoriesAndLikeScoreMap.keySet()) {
				dao.updateUserCategoryPreferences(loggedInUserId, category,
						categoriesAndLikeScoreMap.get(category));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
