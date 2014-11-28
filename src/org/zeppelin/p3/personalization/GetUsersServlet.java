package org.zeppelin.p3.personalization;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

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
 * Servlet implementation class GetUsersServlet
 */
@WebServlet("/GetUsersServlet")
public class GetUsersServlet extends HttpServlet {
       
    /**
	 * 
	 */
	private static final long serialVersionUID = -2206594279863538527L;

	/**
     * @see HttpServlet#HttpServlet()
     */
    public GetUsersServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		HttpSession session = request.getSession(true);
		response.setContentType("text/plain");
		
		PrintWriter out = response.getWriter();
	
		List<User> userList = new ArrayList<User>();
		MySQLAccess dao = new MySQLAccess();
		try {
			JSONObject obj = new JSONObject();
			JSONArray jsonResults = new JSONArray();
			userList = dao.getUsers();
			for (int i = 0; i < userList.size(); i++) {
				jsonResults
					.add(userList.get(i));
			}
			obj.put ("users", jsonResults);
			out.println (obj.toJSONString());
			out.close ();
			return;
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return;
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet (request, response);
	}

}
