package asakichy.architecture.domain_logic.transaction_script;

import static asakichy.architecture.domain_logic.DateUtils.*;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

import asakichy.architecture.domain_logic.AppRuntimeException;
import asakichy.architecture.domain_logic.Gateway;

/**
 * 「収益認識」のトランザクションスクリプト.
 */

public class RecognitionService {
	private Gateway gw;

	public RecognitionService(Gateway gw) {
		this.gw = gw;
	}

	/**
	 * 契約における、収益認識を登録します.
	 * 
	 * @param contractID 契約番号
	 */
	public void calculateRevenueRecognitions(long contractID) {
		try {
			ResultSet contracts = gw.findContract(contractID);
			contracts.next();
			long totalRevenue = contracts.getLong("revenue");
			Date signedDate = contracts.getDate("date_signed");
			String productType = contracts.getString("type");

			if (productType.equals("WordProcessor")) {
				gw.insertRecognition(contractID, signedDate, totalRevenue);
			} else if (productType.equals("Database")) {
				long oneThirdRevenue = totalRevenue / 3;
				long remainder = totalRevenue % 3;
				gw.insertRecognition(contractID, signedDate, oneThirdRevenue);
				gw.insertRecognition(contractID, addDays(signedDate, 60), oneThirdRevenue);
				gw.insertRecognition(contractID, addDays(signedDate, 90), oneThirdRevenue + remainder);
			} else if (productType.equals("SpreadSheet")) {
				long oneThirdRevenue = totalRevenue / 3;
				long remainder = totalRevenue % 3;
				gw.insertRecognition(contractID, signedDate, oneThirdRevenue);
				gw.insertRecognition(contractID, addDays(signedDate, 30), oneThirdRevenue);
				gw.insertRecognition(contractID, addDays(signedDate, 60), oneThirdRevenue + remainder);
			}

		} catch (SQLException e) {
			throw new AppRuntimeException(e);
		}
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
		long result = 0;
		try {
			ResultSet rs = gw.findRecognitionsFor(contractID, asOf);
			while (rs.next()) {
				result += rs.getLong("amount");
			}
			return result;
		} catch (SQLException e) {
			throw new AppRuntimeException(e);
		}
	}

}
