package asakichy.architecture.domain_logic.domain_model;

/**
 * 「収益認識」ドメインモデルの「収益認識計算」ストラテジー.
 */

public interface RecognitionStrategy {
	/**
	 * 契約における、収益認識を登録します.
	 * 
	 * @param contract 契約
	 */
	void calculateRevenueRecognitions(Contract contract);
}
