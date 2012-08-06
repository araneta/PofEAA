package asakichy.architecture.web_presentation.front_controller;

import java.io.IOException;

import javax.servlet.ServletException;

/**
 * Artistコマンドクラス.
 */

public class ArtistCommand extends FrontCommand {
	@Override
	protected void proccess() throws ServletException, IOException {
		String name = request.getParameter("name");
		Artist artist = Artist.findByName(name);
		request.setAttribute("helper", new ArtistHelper(artist));
		forward("/artist.jsp");
	}
}
