package wingbound.client;

import com.mojang.blaze3d.platform.InputConstants;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.KeyMapping;

import wingbound.network.DragonControlPayload;

public final class DragonKeyMappings {
	private static final KeyMapping X = new KeyMapping("key.greendragons.x", InputConstants.KEY_X, KeyMapping.Category.MISC);
	private static final KeyMapping Z = new KeyMapping("key.greendragons.z", InputConstants.KEY_Z, KeyMapping.Category.MISC);
	private static final KeyMapping R = new KeyMapping("key.greendragons.r", InputConstants.KEY_R, KeyMapping.Category.MISC);

	private static boolean wasRDown;

	private DragonKeyMappings() {
	}

	public static void register() {
		KeyMappingHelper.registerKeyMapping(X);
		KeyMappingHelper.registerKeyMapping(Z);
		KeyMappingHelper.registerKeyMapping(R);

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			boolean rDown = R.isDown();
			boolean attack = rDown && !wasRDown;
			wasRDown = rDown;

			if (client.player != null && client.player.isPassenger()) {
				ClientPlayNetworking.send(new DragonControlPayload(X.isDown(), Z.isDown(), attack));
			}
		});
	}
}
