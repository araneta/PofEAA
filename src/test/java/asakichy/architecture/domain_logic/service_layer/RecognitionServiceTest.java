package asakichy.architecture.domain_logic.service_layer;

import static org.mockito.Mockito.*;

import org.junit.Test;

public class RecognitionServiceTest {
	@Test
	public void 収益認識額の通知() throws Exception {
		// 他サービスはモック
		EmailGateway emailGateway = mock(EmailGateway.class);
		IntegrationGateway integrationGateway = mock(IntegrationGateway.class);
		String to = "admin@asakichy.com";
		String subject = "RE: 契約番号 #1";
		String body = "収益認識を算出しました。";
		doNothing().when(emailGateway).sendEmailMesage(to, subject, body);
		doNothing().when(integrationGateway).publishRevenueRecognitionCalculation(1);

		// 実行
		RecognitionService service = new RecognitionService(emailGateway, integrationGateway);
		service.calculateRevenueRecognitions(1);

		// 他サービス呼び出し確認
		verify(emailGateway).sendEmailMesage(to, subject, body);
		verify(integrationGateway).publishRevenueRecognitionCalculation(1);
	}

}
