package asakichy.architecture.web_presentation.transform_view;

import java.io.ByteArrayInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

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

	public Document toXML() {
		try {
			StringBuilder sb = new StringBuilder();
			sb.append("<artist>");
			sb.append("<name>");
			sb.append(getName());
			sb.append("</name>");
			sb.append("<label>");
			sb.append(getLabel());
			sb.append("</label>");
			sb.append("</artist>");
			DocumentBuilderFactory dbfactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = dbfactory.newDocumentBuilder();
			return documentBuilder.parse(new ByteArrayInputStream(sb.toString().getBytes("UTF-8")));
		} catch (Exception ignore) {
			return null;
		}

	}

}
