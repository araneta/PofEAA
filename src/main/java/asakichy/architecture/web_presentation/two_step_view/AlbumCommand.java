package asakichy.architecture.web_presentation.two_step_view;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.w3c.dom.Document;

/**
 * Albumコマンドクラス（ツーステップビュー使用）.
 */

public class AlbumCommand extends FrontCommand {
	@Override
	protected void proccess() throws ServletException, IOException {
		String title = request.getParameter("title");
		Album album = Album.findByTitle(title);
		Document document = album.toXML();
		transform(document);
	}

	private void transform(Document document) throws IOException {
		try {
			TransformerFactory transformFactory = TransformerFactory.newInstance();

			// 第一段階
			Source xsl = new StreamSource("src/main/java/asakichy/architecture/web_presentation/two_step_view/first.xsl");
			Transformer transformer = transformFactory.newTransformer(xsl);
			transformer.setOutputProperty(OutputKeys.METHOD, "xml");
			Source source = new DOMSource(document);
			DOMResult domResult = new DOMResult();
			transformer.transform(source, domResult);

			// 第二段階
			xsl = new StreamSource("src/main/java/asakichy/architecture/web_presentation/two_step_view/second.xsl");
			transformer = transformFactory.newTransformer(xsl);
			transformer.setOutputProperty(OutputKeys.METHOD, "xml");
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			source = new DOMSource(domResult.getNode());
			PrintWriter writer = response.getWriter();
			Result result = new StreamResult(writer);
			transformer.transform(source, result);
		} catch (TransformerException e) {
			throw new IOException(e);
		}
	}
}
