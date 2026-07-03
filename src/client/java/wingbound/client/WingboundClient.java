package wingbound.client;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.renderer.entity.EntityRenderers;

import wingbound.client.render.BabyFireDragonRenderer;
import wingbound.registry.ModEntities;

public class WingboundClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		@SuppressWarnings({"unchecked", "rawtypes"})
		net.minecraft.client.renderer.entity.EntityRendererProvider provider = BabyFireDragonRenderer::new;
		EntityRenderers.register((net.minecraft.world.entity.EntityType) ModEntities.BABY_FIRE_DRAGON, provider);
	}
}