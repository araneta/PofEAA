package asakichy.object_relational.structural.embedded_value;

/**
 * 金額オブジェクト.
 */

public class Money {
	private long amount;
	private String currency;

	public Money(long amount, String currency) {
		this.amount = amount;
		this.currency = currency;
	}

	public long getAmount() {
		return amount;
	}

	public String getCurrency() {
		return currency;
	}

}
