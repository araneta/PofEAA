package asakichy.object_relational.structural.single_table_inheritance;

/**
 * Bowlerドメインオブジェクト.
 */

public class Bowler extends Cricketer {

	private long bowlingAverage;

	public Bowler(long id) {
		super(id, TYPE.BOWLING);
	}

	public Bowler() {
		this(-1);
	}

	public long getBowlingAverage() {
		return bowlingAverage;
	}

	public void setBowlingAverage(long bowlingAverage) {
		this.bowlingAverage = bowlingAverage;
	}
}
