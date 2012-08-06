package asakichy.architecture.web_presentation.application_controller;

/**
 * Assetクラス.
 */

public class Asset {

	public static Asset find(String id) {
		// dummy
		if ("1".equals(id)) {
			return new Asset("1", AssetStatus.ON_LEAS);
		} else if ("2".equals(id)) {
			return new Asset("2", AssetStatus.IN_INVENTORY);
		} else if ("3".equals(id)) {
			return new Asset("3", AssetStatus.ON_LEAS);
		} else if ("4".equals(id)) {
			return new Asset("4", AssetStatus.IN_INVENTORY);
		} else {
			return null;
		}
	}

	private String id;
	private AssetStatus status;

	public Asset(String id, AssetStatus status) {
		this.id = id;
		this.status = status;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setStatus(AssetStatus status) {
		this.status = status;
	}

	public AssetStatus getStatus() {
		return status;
	}

}
