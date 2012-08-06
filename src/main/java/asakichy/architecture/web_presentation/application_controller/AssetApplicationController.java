package asakichy.architecture.web_presentation.application_controller;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

/**
 * Assetアプリケーションコントローラー.
 */

public class AssetApplicationController implements ApplicationController {

	private static AssetApplicationController instance;

	private AssetApplicationController() {
	}

	public static synchronized AssetApplicationController getInstance() {
		return instance;
	}

	static {
		instance = new AssetApplicationController();
		instance.addResponse("return", AssetStatus.ON_LEAS, GatherReturnDetailsCommand.class, "return");
		instance.addResponse("return", AssetStatus.IN_INVENTORY, NullAssetCommand.class, "illegalAction");
		instance.addResponse("damage", AssetStatus.ON_LEAS, InventoryDamageCommand.class, "leaseDamage");
		instance.addResponse("damage", AssetStatus.IN_INVENTORY, LeaseDamageCommand.class, "inventoryDamage");
	}

	private Map<String, Map<AssetStatus, Response>> events = new HashMap<String, Map<AssetStatus, Response>>();

	@Override
	public DomainCommand getDomainCommand(String commandString, Map<String, String[]> params) {
		AssetStatus assetStatus = getAssetStatus(params);
		Response response = getResponse(commandString, assetStatus);
		return response.getDomainCommand();
	}

	@Override
	public String getView(String commandString, Map<String, String[]> params) {
		AssetStatus assetStatus = getAssetStatus(params);
		Response response = getResponse(commandString, assetStatus);
		return response.getView();
	}

	private Response getResponse(String commandString, AssetStatus assetStatus) {
		Map<AssetStatus, Response> map = events.get(commandString);
		return map.get(assetStatus);
	}

	private AssetStatus getAssetStatus(Map<String, String[]> params) {
		String id = params.get("assetID")[0];
		Asset asset = Asset.find(id);
		return asset.getStatus();
	}

	private void addResponse(String event, AssetStatus state, Class<? extends DomainCommand> domainCommand, String view) {
		Response response = new Response(domainCommand, view);
		if (!events.containsKey(event)) {
			events.put(event, new EnumMap<AssetStatus, Response>(AssetStatus.class));
		}
		events.get(event).put(state, response);
	}

}
