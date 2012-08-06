package asakichy.architecture.web_presentation.page_controller;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;
import org.mockito.ArgumentCaptor;

public class ArtistControllerTest {
	@Test
	public void アーチスト表示要求() throws Exception {
		/*
		 * 想定URL
		 * http://localhost:8080/isa/Artist?name=Liszt
		 */

		// リクエスト・レスポンスはモック
		// getParameterをスタブ化
		HttpServletRequest request = mock(HttpServletRequest.class);
		HttpServletResponse response = mock(HttpServletResponse.class);
		when(request.getParameter("name")).thenReturn("Liszt");

		// コントローラは実オブジェクト
		// ただし、forwardのみスタブ化
		ArtistController artistController = new ArtistController();
		artistController = spy(artistController);
		doNothing().when(artistController).forward("/artist.jsp", request, response);

		// リクエスト実行
		artistController.doGet(request, response);

		// JSPヘルパオブジェクトの確認
		ArgumentCaptor<String> key = ArgumentCaptor.forClass(String.class);
		ArgumentCaptor<ArtistHelper> helper = ArgumentCaptor.forClass(ArtistHelper.class);
		verify(request).setAttribute(key.capture(), helper.capture());
		assertThat(key.getValue(), is("helper"));
		assertThat(helper.getValue().getName(), is("Liszt"));

		// forward呼び出し確認
		verify(artistController).forward("/artist.jsp", request, response);
	}
}
