package wingbound.client.render;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import com.geckolib.renderer.GeoEntityRenderer;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;

import wingbound.entities.dragons.BabyFireDragonEntity;

@Environment(EnvType.CLIENT)
public class BabyFireDragonRenderer extends GeoEntityRenderer<BabyFireDragonEntity, EntityRenderState> {
    public BabyFireDragonRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new FireDragonGeoModel());
    }
}
