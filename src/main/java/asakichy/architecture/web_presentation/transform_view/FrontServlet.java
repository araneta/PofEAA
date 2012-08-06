package asakichy.architecture.web_presentation.transform_view;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import asakichy.architecture.web_presentation.AppRuntimeException;

/**
 * フロントコントローラー.
 */

public class FrontServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		FrontCommand command = getCommand(request);
		command.init(request, response, getServletContext());
		command.proccess();
	}

	private FrontCommand getCommand(HttpServletRequest request) {
		try {
			return getCommandClass(request).newInstance();
		} catch (Exception e) {
			throw new AppRuntimeException(e);
		}
	}

	private Class<? extends FrontCommand> getCommandClass(HttpServletRequest request) throws ClassNotFoundException {
		String command = request.getParameter("command");
		String commandClassName = "asakichy.architecture.web_presentation.front_controller." + command + "Command";
		Class<? extends FrontCommand> commandClass;
		commandClass = Class.forName(commandClassName).asSubclass(FrontCommand.class);
		return commandClass;
	}

}
