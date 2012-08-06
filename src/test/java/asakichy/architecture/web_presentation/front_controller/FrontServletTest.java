package asakichy.architecture.web_presentation.front_controller;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import javax.servlet.http.HttpServletRequest;

import org.junit.Test;

public class FrontServletTest {
	@Test
	public void コマンド取得() throws Exception {
		/*
		 * 想定URL
		 * http://localhost:8080/isa/Music?name=Liszt&command=Artist
		 */

		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getParameter("command")).thenReturn("Artist");

		FrontServlet frontServlet = new FrontServlet();
		FrontCommand command = frontServlet.getCommand(request);
		assertThat(command, instanceOf(ArtistCommand.class));
	}
}
