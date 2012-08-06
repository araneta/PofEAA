package asakichy.architecture.web_presentation.application_controller;

import asakichy.architecture.web_presentation.AppRuntimeException;

/**
 * 応答クラス.
 */

public class Response {
	private Class<? extends DomainCommand> domainCommand;
	private String view;

	public Response(Class<? extends DomainCommand> domainCommand, String view) {
		this.domainCommand = domainCommand;
		this.view = view;
	}

	public DomainCommand getDomainCommand() {
		try {
			return domainCommand.newInstance();
		} catch (Exception e) {
			throw new AppRuntimeException(e);
		}
	}

	public String getView() {
		return view;
	}

}
