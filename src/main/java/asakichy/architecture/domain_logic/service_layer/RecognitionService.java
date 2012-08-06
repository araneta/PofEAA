package asakichy.architecture.domain_logic.service_layer;

import java.sql.Date;

/**
 * 「収益認識」のサービスレイヤ.
 */

public class RecognitionService extends ApplicationService {

	public RecognitionService(EmailGateway emailGateway, IntegrationGateway integrationGateway) {
		super(emailGateway, integrationGateway);
	}

	/**
	 * 契約における、収益認識を登録します.
	 * 
	 * @param contractID 契約番号
	 */
	public void calculateRevenueRecognitions(long contractID) {
		// 収益認識を算出、登録
		Contract contract = Contract.readForUpdate(contractID);
		contract.calculateRecognitions();

		// 管理者にメール通知
		String to = contract.getAdministratorEmailAddress();
		String subject = "RE: 契約番号 #" + contractID;
		String body = "収益認識を算出しました。";
		getEmailGateway().sendEmailMesage(to, subject, body);

		// 他システムへ公開
		getIntegrationGateway().publishRevenueRecognitionCalculation(contractID);
	}

	/**
	 * 契約における、指定時点での収益認識額を取得します.
	 * 
	 * @param contractID 契約番号
	 * @param asOf 時点
	 * 
	 * @return 収益認識額
	 */
	public long recognizedRevenue(long contractID, Date asOf) {
		return Contract.read(contractID).recognizedRevenue(asOf);
	}

}
