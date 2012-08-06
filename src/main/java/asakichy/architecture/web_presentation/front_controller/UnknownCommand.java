package asakichy.architecture.web_presentation.front_controller;

import java.io.IOException;

import javax.servlet.ServletException;

/**
 * 未知のコマンドクラス.
 */

public class UnknownCommand extends FrontCommand {
	@Override
	protected void proccess() throws ServletException, IOException {
		forward("unknown.jsp");
	}
}
