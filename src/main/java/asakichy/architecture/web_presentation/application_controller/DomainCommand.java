package asakichy.architecture.web_presentation.application_controller;

import java.util.Map;

public interface DomainCommand {

	void run(Map<String, String[]> params);

}
