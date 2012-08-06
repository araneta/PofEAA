package asakichy.architecture.web_presentation.two_step_view;

import static asakichy.architecture.web_presentation.FileUtils.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.Test;

public class AlbumCommandTest {
	@Test
	public void コマンド実行() throws Exception {
		// リクエストはモック（getParameterをスタブ化）
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getParameter("title")).thenReturn("Liszt Best");

		// レスポンスはモック（getWriterをスタブ化）
		HttpServletResponse response = mock(HttpServletResponse.class);
		StringWriter actual = new StringWriter();
		when(response.getWriter()).thenReturn(new PrintWriter(actual, true));

		// コンテキストはモック
		ServletContext context = mock(ServletContext.class);

		// リクエスト処理
		FrontCommand command = new AlbumCommand();
		command.init(request, response, context);
		command.proccess();

		// HTML（文字列）を検証
		String expectedFile = "src/test/java/asakichy/architecture/web_presentation/two_step_view/album.html";
		String expected = file2string(expectedFile);
		XMLUnit.setIgnoreWhitespace(true);
		Diff diff = new Diff(expected, actual.toString());
		assertThat(diff.similar(), is(true));
	}

}
