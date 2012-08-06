package asakichy.object_relational.structural.dependent_mapping;

/**
 * Trackドメインオブジェクト.
 */

public class Track extends DomainObject {
	private String title;
	private String time;

	public Track(long id, String title, String time) {
		super(id);
		this.title = title;
		this.time = time;
	}

	public String getTitle() {
		return title;
	}

	public String getTime() {
		return time;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setTime(String time) {
		this.time = time;
	}

}
