package wingbound.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

import wingbound.client.render.BabyFireDragonRenderer;
import wingbound.registry.ModEntities;

public class WingboundClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		EntityRendererRegistry.register(ModEntities.BABY_FIRE_DRAGON, BabyFireDragonRenderer::new);
		DragonKeyMappings.register();
	}
}
