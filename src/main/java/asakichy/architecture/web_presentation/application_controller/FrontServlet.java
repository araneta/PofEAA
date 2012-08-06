package asakichy.architecture.web_presentation.application_controller;

import java.io.IOException;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * フロントコントローラー.
 */

public class FrontServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ApplicationController applicationController = getApplicationController(request);
		String commandString = request.getParameter("command");
		Map<String, String[]> params = request.getParameterMap();
		DomainCommand command = applicationController.getDomainCommand(commandString, params);
		command.run(params);

		String view = applicationController.getView(commandString, params);
		String viewPage = "/" + view + ".jsp";
		forward(viewPage, request, response);
	}

	private ApplicationController getApplicationController(HttpServletRequest request) {
		String typeString = request.getParameter("type");
		if ("asset".equals(typeString)) {
			return AssetApplicationController.getInstance();
		} else {
			// etc...
			return null;
		}
	}

	private void forward(String target, HttpServletRequest request, HttpServletResponse response) throws ServletException,
			IOException {
		ServletContext sc = getServletContext();
		RequestDispatcher rd = sc.getRequestDispatcher(target);
		rd.forward(request, response);
	}

}
