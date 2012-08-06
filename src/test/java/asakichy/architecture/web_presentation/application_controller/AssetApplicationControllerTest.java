package asakichy.architecture.web_presentation.application_controller;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class AssetApplicationControllerTest {
	@Test
	public void コマンド取得() throws Exception {
		AssetApplicationController controller = AssetApplicationController.getInstance();

		Map<String, String[]> params = new HashMap<String, String[]>();
		DomainCommand command;

		params.put("assetID", new String[] { "1" });
		command = controller.getDomainCommand("return", params);
		assertThat(command, instanceOf(GatherReturnDetailsCommand.class));

		params.put("assetID", new String[] { "2" });
		command = controller.getDomainCommand("return", params);
		assertThat(command, instanceOf(NullAssetCommand.class));

		params.put("assetID", new String[] { "3" });
		command = controller.getDomainCommand("damage", params);
		assertThat(command, instanceOf(InventoryDamageCommand.class));

		params.put("assetID", new String[] { "4" });
		command = controller.getDomainCommand("damage", params);
		assertThat(command, instanceOf(LeaseDamageCommand.class));
	}

	@Test
	public void ビュー取得() throws Exception {
		AssetApplicationController controller = AssetApplicationController.getInstance();

		Map<String, String[]> params = new HashMap<String, String[]>();
		String view;

		params.put("assetID", new String[] { "1" });
		view = controller.getView("return", params);
		assertThat(view, is("return"));

		params.put("assetID", new String[] { "2" });
		view = controller.getView("return", params);
		assertThat(view, is("illegalAction"));

		params.put("assetID", new String[] { "3" });
		view = controller.getView("damage", params);
		assertThat(view, is("leaseDamage"));

		params.put("assetID", new String[] { "4" });
		view = controller.getView("damage", params);
		assertThat(view, is("inventoryDamage"));
	}
}
