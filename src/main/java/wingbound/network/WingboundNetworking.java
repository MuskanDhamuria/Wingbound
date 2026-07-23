package wingbound.network;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.world.entity.Entity;

import wingbound.entities.dragons.BabyFireDragonEntity;

public final class WingboundNetworking {
	private WingboundNetworking() {
	}

	public static void register() {
		PayloadTypeRegistry.serverboundPlay().register(DragonControlPayload.TYPE, DragonControlPayload.CODEC);
		ServerPlayNetworking.registerGlobalReceiver(DragonControlPayload.TYPE, (payload, context) ->
			context.server().execute(() -> {
				Entity vehicle = context.player().getVehicle();
				if (vehicle instanceof BabyFireDragonEntity dragon) {
					dragon.applyDownloadedModControls(payload.rise(), payload.descend(), payload.attack());
				}
			})
		);
	}
}
