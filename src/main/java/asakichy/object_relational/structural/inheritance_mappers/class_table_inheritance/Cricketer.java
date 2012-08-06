package asakichy.object_relational.structural.inheritance_mappers.class_table_inheritance;

public class Cricketer extends Player {

	long battingAverage;

	public Cricketer(long id, TYPE type) {
		super(id, type);
	}

	public Cricketer(long id) {
		super(id, TYPE.CRICKET);
	}

	public Cricketer() {
		this(-1);
	}

	public long getBattingAverage() {
		return battingAverage;
	}

	public void setBattingAverage(long battingAverage) {
		this.battingAverage = battingAverage;
	}

}
