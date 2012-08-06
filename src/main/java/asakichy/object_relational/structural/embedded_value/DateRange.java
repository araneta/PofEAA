package asakichy.object_relational.structural.embedded_value;

import java.sql.Date;

/**
 * 日付範囲オブジェクト.
 */

public class DateRange {
	private Date start;
	private Date end;

	public DateRange(Date start, Date end) {
		this.start = start;
		this.end = end;
	}

	public Date getStart() {
		return start;
	}

	public Date getEnd() {
		return end;
	}

}
