package wingbound.client.render;

import com.geckolib.model.GeoModel;
import com.geckolib.renderer.base.GeoRenderState;

import net.minecraft.resources.Identifier;

import wingbound.Wingbound;
import wingbound.entities.dragons.BabyFireDragonEntity;

public class FireDragonGeoModel extends GeoModel<BabyFireDragonEntity> {
	private static final Identifier MODEL = Wingbound.id("geo/fire_dragon");
	private static final Identifier ANIMATION = Wingbound.id("fire_dragon");
	private static final Identifier TEXTURE = Wingbound.id("textures/entity/fire_dragon.png");

	@Override
	public Identifier getModelResource(GeoRenderState renderState) {
		return MODEL;
	}

	@Override
	public Identifier getAnimationResource(BabyFireDragonEntity animatable) {
		return ANIMATION;
	}

	@Override
	public Identifier getTextureResource(GeoRenderState renderState) {
		return TEXTURE;
	}
}
