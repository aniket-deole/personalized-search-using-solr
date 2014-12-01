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
		// Fetch the Logged In userId from the session
		Integer loggedInUserId = (Integer) request.getSession().getAttribute(
				"loggedInUserId");
		String categoriesAndLikeScoreMapS = request
				.getHeader("categoriesAndLikeScoreMap");
		String updatedSourceWithCheckValuesS = request
				.getHeader("updatedSourceWithCheckValues");
		// First split all the strings based on comma
		if (categoriesAndLikeScoreMapS != null
				&& !categoriesAndLikeScoreMapS.isEmpty()) {
			String[] mapArray = categoriesAndLikeScoreMapS.split("[,]");
			String categoryName = null;
			MySQLAccess dao = new MySQLAccess();
			Integer likeScore = 0;
			if (mapArray.length > 0) {
				// Split category and the like score
				for (String map : mapArray) {
					String[] split = map.split("[:]");
					if (split.length == 2) {
						categoryName = split[0].trim();
						split[1] = split[1].trim();
						likeScore = Integer.parseInt(split[1]);
						try {
							dao.updateUserCategoryPreferences(loggedInUserId,
									categoryName, likeScore);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
		}
		// Do the same for news sources
		// First split all the strings based on comma
		if (updatedSourceWithCheckValuesS != null
				&& !updatedSourceWithCheckValuesS.isEmpty()) {
			String[] mapArray = updatedSourceWithCheckValuesS.split("[,]");
			String sourceName = null;
			Boolean checked = false;
			MySQLAccess dao = new MySQLAccess();
			if (mapArray.length > 0) {
				// Split category and the like score
				for (String map : mapArray) {
					String[] split = map.split("[:]");
					if (split.length == 2) {
						sourceName = split[0].trim();
						split[1] = split[1].trim();
						checked = Boolean.parseBoolean(split[1]);
						try {
							dao.updatePreferredSources(loggedInUserId,
									sourceName, checked);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
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
