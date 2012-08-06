package asakichy.architecture.domain_logic.domain_model;

import java.sql.Date;

/**
 * 「収益認識」ドメインモデルの「収益認識」クラス.
 */

public class RevenueRecognition {
	private long amount;
	private Date date;

	public RevenueRecognition(long amount, Date date) {
		this.amount = amount;
		this.date = date;
	}

	public long getAmount() {
		return amount;
	}

	boolean isRecognizableBy(Date asOf) {
		return asOf.after(date) || asOf.equals(date);
	}
}
