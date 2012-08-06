package asakichy.architecture.web_presentation.front_controller;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;
import org.mockito.ArgumentCaptor;

public class ArtistCommandTest {
	@Test
	public void コマンド実行() throws Exception {
		/*
		 * 想定URL
		 * http://localhost:8080/isa/Music?name=Liszt&command=Artist
		 */

		// リクエスト・レスポンスはモック
		// getParameterをスタブ化
		HttpServletRequest request = mock(HttpServletRequest.class);
		HttpServletResponse response = mock(HttpServletResponse.class);
		ServletContext context = mock(ServletContext.class);
		when(request.getParameter("name")).thenReturn("Liszt");

		// コマンドは実オブジェクト
		// ただし、forwardのみスタブ化
		FrontCommand command = new ArtistCommand();
		command = spy(command);
		doNothing().when(command).forward("/artist.jsp");

		// リクエスト処理
		command.init(request, response, context);
		command.proccess();

		// JSPヘルパオブジェクトの確認
		ArgumentCaptor<String> key = ArgumentCaptor.forClass(String.class);
		ArgumentCaptor<ArtistHelper> helper = ArgumentCaptor.forClass(ArtistHelper.class);
		verify(request).setAttribute(key.capture(), helper.capture());
		assertThat(key.getValue(), is("helper"));
		assertThat(helper.getValue().getName(), is("Liszt"));

		// forward呼び出し確認
		verify(command).forward("/artist.jsp");
	}
}
