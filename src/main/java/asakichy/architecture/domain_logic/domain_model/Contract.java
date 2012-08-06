package asakichy.architecture.domain_logic.domain_model;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * 「収益認識」ドメインモデルの「契約」クラス.
 */

public class Contract {
	private long id;
	private Product product;
	private long revenue;
	private Date whenSigned;
	private List<RevenueRecognition> revenueRecognitions = new ArrayList<RevenueRecognition>();

	public Contract(long id, Product product, long revenue, Date whenSigned) {
		this.id = id;
		this.product = product;
		this.revenue = revenue;
		this.whenSigned = whenSigned;
	}

	public long getId() {
		return id;
	}

	public Date getWhenSigned() {
		return whenSigned;
	}

	public long getRevenue() {
		return revenue;
	}

	public void addRevenueRecognition(RevenueRecognition revenueRecognition) {
		revenueRecognitions.add(revenueRecognition);
	}

	/**
	 * 契約における、指定時点での収益認識額を取得します.
	 * 
	 * @param asOf 時点
	 * @return 収益認識額
	 */
	public long recognizedRevenue(Date asOf) {
		long result = 0;
		for (RevenueRecognition r : revenueRecognitions) {
			if (r.isRecognizableBy(asOf)) {
				result += r.getAmount();
			}
		}
		return result;
	}

	/**
	 * 契約における、収益認識を登録します.
	 */
	public void calculateRecognitions() {
		product.calculateRevenueRecognitions(this);
	}

}
