package asakichy.architecture.domain_logic.domain_model;

/**
 * 「収益認識」ドメインモデルの「商品」クラス.
 */

public class Product {
	private String name;
	private RecognitionStrategy recognitionStrategy;

	private Product(String name, RecognitionStrategy recognitionStrategy) {
		this.name = name;
		this.recognitionStrategy = recognitionStrategy;
	}

	public static Product newWordProcessor(String name) {
		return new Product(name, new CompleteRecognitionStrategy());
	}

	public static Product newDatabase(String name) {
		return new Product(name, new ThreeWayRecognitionStrategy(60, 90));
	}

	public static Product newSpreadSheet(String name) {
		return new Product(name, new ThreeWayRecognitionStrategy(30, 60));
	}

	public String getName() {
		return name;
	}

	public void calculateRevenueRecognitions(Contract contract) {
		recognitionStrategy.calculateRevenueRecognitions(contract);
	}

}
