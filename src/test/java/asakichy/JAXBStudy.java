package asakichy;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.Test;

public class JAXBStudy {

	@Test
	public void XMLからオブジェクト_アンマーシャリング() throws Exception {
		String xml = "<folder name=\"root\"><children name=\"sub1\"><children name=\"sub1_1\"></children></children></folder>";

		JAXBContext context = JAXBContext.newInstance(Folder.class);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		StringReader sr = new StringReader(xml);
		Folder root = (Folder) unmarshaller.unmarshal(sr);

		assertThat(root.getName(), is("root"));
		assertThat(root.getChildren().get(0).getName(), is("sub1"));
		assertThat(root.getChildren().get(0).getChildren().get(0).getName(), is("sub1_1"));
	}

	@Test
	public void オブジェクトからXML_マーシャリング() throws Exception {
		Folder root = new Folder("root");
		Folder sub1 = new Folder("sub1");
		Folder sub1_1 = new Folder("sub1_1");
		sub1.addChildren(sub1_1);
		root.addChildren(sub1);

		JAXBContext context = JAXBContext.newInstance(Folder.class);
		Marshaller marshaller = context.createMarshaller();
		StringWriter sw = new StringWriter();
		marshaller.marshal(root, sw);
		// System.out.println(sw.toString());

		String xml = "<folder name=\"root\"><children name=\"sub1\"><children name=\"sub1_1\"></children></children></folder>";
		XMLUnit.setIgnoreWhitespace(true);
		Diff diff = new Diff(xml, sw.toString());
		assertThat(diff.similar(), is(true));
	}

	@XmlRootElement(name = "folder")
	public static class Folder {
		private String name;
		private List<Folder> children;

		public Folder(String name) {
			this.name = name;
			children = null;
		}

		public Folder() {
			name = null;
			children = null;
		}

		@XmlAttribute
		public String getName() {
			return name;
		}

		@XmlElement
		public List<Folder> getChildren() {
			return children;
		}

		public void setName(String name) {
			this.name = name;
		}

		public void setChildren(List<Folder> children) {
			this.children = children;
		}

		public void addChildren(Folder child) {
			if (children == null) {
				children = new ArrayList<Folder>();
			}
			children.add(child);
		}

	}

}
