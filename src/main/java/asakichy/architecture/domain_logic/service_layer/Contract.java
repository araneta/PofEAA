package asakichy.architecture.domain_logic.service_layer;

import java.sql.Date;

/**
 * 「収益認識」ドメインモデルの「契約」クラス.
 */
public class Contract {

	private long contractID;
	private String administratorEmailAddress;

	public Contract(long contractID, String administratorEmailAddress) {
		this.contractID = contractID;
		this.administratorEmailAddress = administratorEmailAddress;
	}

	public Contract(long contractID) {
		this(contractID, "admin@asakichy.com");
	}

	public static Contract readForUpdate(long contractID) {
		return new Contract(contractID);
	}

	public static Contract read(long contractID) {
		return new Contract(contractID);
	}

	public long getContractID() {
		return contractID;
	}

	public String getAdministratorEmailAddress() {
		return administratorEmailAddress;
	}

	public long recognizedRevenue(Date asOf) {
		return 0L;
	}

	public void calculateRecognitions() {
	}

}
