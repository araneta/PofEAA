package asakichy.offline_concurrency;

/**
 * 並行性のエラー.
 */

public class ConcurrencyRuntimeException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public ConcurrencyRuntimeException(String message) {
		super(message);
	}
}
