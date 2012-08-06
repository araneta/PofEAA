package asakichy.architecture.web_presentation.template_view;

/**
 * Artistクラス.
 */

public class Artist {

	public static Artist findByName(String name) {
		return new Artist(name, "indies"); // dummy
	}

	private String name;
	private String label;

	public Artist(String name, String label) {
		this.name = name;
		this.label = label;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

}
