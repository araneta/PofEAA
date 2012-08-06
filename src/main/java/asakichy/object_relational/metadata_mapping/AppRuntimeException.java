package asakichy.object_relational.metadata_mapping;

/**
 * アプリケーションのエラー.
 * 
 * 例外翻訳に使用します。
 */

public class AppRuntimeException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public AppRuntimeException(Exception e) {
		super(e);
	}

	public AppRuntimeException(String message) {
		super(message);
	}
}
