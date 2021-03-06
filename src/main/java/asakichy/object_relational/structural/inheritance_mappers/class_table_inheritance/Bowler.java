package asakichy.object_relational.structural.inheritance_mappers.class_table_inheritance;

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
