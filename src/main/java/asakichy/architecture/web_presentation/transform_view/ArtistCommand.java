package asakichy.architecture.web_presentation.transform_view;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.w3c.dom.Document;

/**
 * Artistコマンドクラス（トランスフォームビュー使用）.
 */

public class ArtistCommand extends FrontCommand {
	@Override
	protected void proccess() throws ServletException, IOException {
		String name = request.getParameter("name");
		Artist artist = Artist.findByName(name);
		Document document = artist.toXML();
		transform(document);
	}

	private void transform(Document document) throws IOException {
		try {
			// テンプレート・変換器
			TransformerFactory transformFactory = TransformerFactory.newInstance();
			Source xsl = new StreamSource("src/main/java/asakichy/architecture/web_presentation/transform_view/artist.xsl");
			Transformer transformer = transformFactory.newTransformer(xsl);

			// 余計なメタタグを抑制
			transformer.setOutputProperty(OutputKeys.METHOD, "xml");

			// データ
			Source source = new DOMSource(document);

			// 出力先
			PrintWriter writer = response.getWriter();
			Result result = new StreamResult(writer);

			// 変換
			transformer.transform(source, result);
		} catch (TransformerException e) {
			throw new IOException(e);
		}
	}
}
