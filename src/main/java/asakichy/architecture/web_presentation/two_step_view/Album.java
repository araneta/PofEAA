package asakichy.architecture.web_presentation.two_step_view;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

/**
 * Albumクラス.
 */

public class Album {
	public static Album findByTitle(String title) {
		// dummy
		List<Track> trackList = new ArrayList<Track>();
		trackList.add(new Track("Liebestraum", "4:00"));
		trackList.add(new Track("Hungarian Rhapsody", "2:40"));
		return new Album(title, "Liszt", trackList);
	}

	private String title;
	private String artist;
	private List<Track> trackList;

	public Album(String title, String artist, List<Track> trackList) {
		this.title = title;
		this.artist = artist;
		this.trackList = trackList;
	}

	public Album(String title) {
		this.title = title;
		this.artist = "unknown";
		this.trackList = Collections.emptyList();
	}

	public String getTitle() {
		return title;
	}

	public String getArtist() {
		return artist;
	}

	public List<Track> getTrackList() {
		return trackList;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}

	public void setTrackList(List<Track> trackList) {
		this.trackList = trackList;
	}

	public Document toXML() {
		try {
			StringBuilder sb = new StringBuilder();
			sb.append("<album>");
			sb.append("<title>");
			sb.append(getTitle());
			sb.append("</title>");
			sb.append("<artist>");
			sb.append(getArtist());
			sb.append("</artist>");

			sb.append("<trackList>");
			for (Track track : trackList) {
				sb.append(track.toXMLString());
			}
			sb.append("</trackList>");

			sb.append("</album>");
			DocumentBuilderFactory dbfactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = dbfactory.newDocumentBuilder();
			return documentBuilder.parse(new ByteArrayInputStream(sb.toString().getBytes("UTF-8")));
		} catch (Exception ignore) {
			return null;
		}
	}

	/**
	 * Trackクラス.
	 */
	public static class Track {
		private String title;
		private String time;

		public Track(String title, String time) {
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

		public String toXMLString() {
			StringBuilder sb = new StringBuilder();
			sb.append("<track>");
			sb.append("<title>");
			sb.append(getTitle());
			sb.append("</title>");
			sb.append("<time>");
			sb.append(getTime());
			sb.append("</time>");
			sb.append("</track>");
			return sb.toString();
		}

	}

}
