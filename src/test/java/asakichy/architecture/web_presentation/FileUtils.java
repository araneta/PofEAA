package asakichy.architecture.web_presentation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * ファイルユーティリティ.
 */
public class FileUtils {
	/**
	 * ファイルの内容を文字列で取得します.
	 * 
	 * @param fileName ファイル名
	 * @return ファイルの内容の文字列
	 * 
	 * @throws IOException
	 */
	public static String file2string(String fileName) throws IOException {
		File file = new File(fileName);
		FileInputStream fis = new FileInputStream(file);
		InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
		BufferedReader br = new BufferedReader(isr);
		String line;
		StringBuilder sb = new StringBuilder();
		while ((line = br.readLine()) != null) {
			sb.append(line);
		}
		br.close();
		return sb.toString();
	}
}
