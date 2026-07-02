package wingbound.client;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.ChickenRenderer;

import wingbound.entities.dragons.BabyFireDragonEntity;
import wingbound.registry.ModEntities;

public class WingboundClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		// Register a vanilla chicken renderer for the baby dragon as a placeholder.
		@SuppressWarnings({"unchecked", "rawtypes"})
		net.minecraft.client.renderer.entity.EntityRendererProvider provider = ChickenRenderer::new;
		EntityRenderers.register((net.minecraft.world.entity.EntityType) ModEntities.BABY_FIRE_DRAGON, provider);
	}
}