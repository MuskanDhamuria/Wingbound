package wingbound.client.render;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import wingbound.entities.dragons.BabyFireDragonEntity;

@Environment(EnvType.CLIENT)
public class BabyFireDragonRenderer extends EntityRenderer<BabyFireDragonEntity, EntityRenderState> {
    public BabyFireDragonRenderer(EntityRendererProvider.Context ctx) {
        super(ctx);
    }

    @Override
    public EntityRenderState createRenderState() {
        return new EntityRenderState();
    }
}
