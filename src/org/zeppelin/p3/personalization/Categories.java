package org.zeppelin.p3.personalization;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.zeppelin.p3.db.MySQLAccess;

/**
 * Servlet implementation class Categories
 */
@WebServlet(urlPatterns = { "/Categories" })
public class Categories extends HttpServlet {
	private static final long serialVersionUID = 1L;
	int count = 1;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Categories() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// Set a cookie for the user, so that the counter does not increate
		// every time the user press refresh
		HttpSession session = request.getSession(true);
		// Set the session valid for 5 secs
		session.setMaxInactiveInterval(5);
		response.setContentType("text/plain");
		PrintWriter out = response.getWriter();
		if (session.isNew()) {
			count++;
		}

		// Fetch the Logged In userId from the session
		Integer userId = (Integer) request.getSession().getAttribute(
				"loggedInUserId");
		// Retrieve all the categories available
		Map<String, Integer> categories = new HashMap<String, Integer>();
		MySQLAccess dao = new MySQLAccess();
		try {
			categories = dao
					.fetchPreferredCategoriesWithTheirLikingScores(userId);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		JSONObject obj = new JSONObject();
		obj.put("resultCount", categories.size());
		JSONArray jsonResults = new JSONArray();
		if (!categories.isEmpty()) {
			// TODO - Show all results in a page view and asynchronously
			for (String category : categories.keySet()) {
				jsonResults.add(new PreferredCategoriesWithLikeScore(category,
						categories.get(category)));
			}
			obj.put("results", jsonResults);
		}
		out.println(obj);
		out.close();
		return;

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
