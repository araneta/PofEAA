package asakichy.architecture.web_presentation.application_controller;

import java.util.Map;

/**
 * アプリケーションコントローラー・インターフェイス.
 */

public interface ApplicationController {

	DomainCommand getDomainCommand(String commandString, Map<String, String[]> params);

	String getView(String command, Map<String, String[]> params);

}
