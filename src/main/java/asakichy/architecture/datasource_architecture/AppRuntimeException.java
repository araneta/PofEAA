package asakichy.architecture.datasource_architecture;

import java.sql.SQLException;

import javax.xml.bind.JAXBException;

/**
 * アプリケーションのエラー.
 * 
 * 例外翻訳に使用します。
 */

public class AppRuntimeException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public AppRuntimeException(SQLException e) {
		super(e);
	}

	public AppRuntimeException(JAXBException e) {
		super(e);
	}
}
