package asakichy.architecture.domain_logic.domain_model;

/**
 * 「収益認識」ドメインモデルの「収益認識計算」ストラテジー
 * ＜一括計上＞.
 */

public class CompleteRecognitionStrategy implements RecognitionStrategy {
	@Override
	public void calculateRevenueRecognitions(Contract contract) {
		contract.addRevenueRecognition(new RevenueRecognition(contract.getRevenue(), contract.getWhenSigned()));
	}

}
