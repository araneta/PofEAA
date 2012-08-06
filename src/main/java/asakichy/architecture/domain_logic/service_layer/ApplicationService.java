package asakichy.architecture.domain_logic.service_layer;

public class ApplicationService {
	private EmailGateway emailGateway;
	private IntegrationGateway integrationGateway;

	public ApplicationService(EmailGateway emailGateway, IntegrationGateway integrationGateway) {
		this.emailGateway = emailGateway;
		this.integrationGateway = integrationGateway;
	}

	protected EmailGateway getEmailGateway() {
		return emailGateway;
	}

	protected IntegrationGateway getIntegrationGateway() {
		return integrationGateway;
	}

}
