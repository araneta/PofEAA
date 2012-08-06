package asakichy.architecture.web_presentation.page_controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Artistページコントローラー.
 */

public class ArtistController extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String name = request.getParameter("name");
		Artist artist = Artist.findByName(name);

		if (artist == null) {
			forward("/MissingArtistError.jsp", request, response);
		} else {
			request.setAttribute("helper", new ArtistHelper(artist));
			forward("/artist.jsp", request, response);
		}
	}

	protected void forward(String target, HttpServletRequest request, HttpServletResponse response) throws ServletException,
			IOException {
		ServletContext sc = getServletContext();
		RequestDispatcher rd = sc.getRequestDispatcher(target);
		rd.forward(request, response);
	}

}
