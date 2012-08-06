package asakichy.architecture.domain_logic;

import java.sql.SQLException;

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
}
