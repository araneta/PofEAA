package asakichy.architecture.web_presentation.front_controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * コマンド基本クラス.
 */

public abstract class FrontCommand {
	protected HttpServletRequest request;
	protected HttpServletResponse response;
	protected ServletContext context;

	public void init(HttpServletRequest request, HttpServletResponse response, ServletContext context) {
		this.request = request;
		this.response = response;
		this.context = context;
	}

	protected void forward(String target) throws ServletException, IOException {
		RequestDispatcher rd = context.getRequestDispatcher(target);
		rd.forward(request, response);
	}

	abstract protected void proccess() throws ServletException, IOException;

}
