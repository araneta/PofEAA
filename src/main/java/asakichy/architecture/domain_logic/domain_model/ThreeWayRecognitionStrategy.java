package asakichy.architecture.domain_logic.domain_model;

import static asakichy.architecture.domain_logic.DateUtils.*;

/**
 * 「収益認識」ドメインモデルの「収益認識計算」ストラテジー
 * ＜三分割計上＞.
 */

public class ThreeWayRecognitionStrategy implements RecognitionStrategy {

	private int firstRecognitionOffset;
	private int secondRecognitionOffset;

	public ThreeWayRecognitionStrategy(int firstRecognitionOffset, int secondRecognitionOffset) {
		this.firstRecognitionOffset = firstRecognitionOffset;
		this.secondRecognitionOffset = secondRecognitionOffset;
	}

	@Override
	public void calculateRevenueRecognitions(Contract contract) {
		long oneThirdRevenue = contract.getRevenue() / 3;
		long remainder = contract.getRevenue() % 3;
		contract.addRevenueRecognition(new RevenueRecognition(oneThirdRevenue, contract.getWhenSigned()));
		contract.addRevenueRecognition(new RevenueRecognition(oneThirdRevenue, addDays(contract.getWhenSigned(),
				firstRecognitionOffset)));
		contract.addRevenueRecognition(new RevenueRecognition(oneThirdRevenue + remainder, addDays(contract.getWhenSigned(),
				secondRecognitionOffset)));
	}
}
