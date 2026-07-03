package wingbound.client.render;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.ChickenRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.ChickenRenderState;
import net.minecraft.resources.Identifier;

import wingbound.Wingbound;
import wingbound.entities.dragons.BabyFireDragonEntity;

@Environment(EnvType.CLIENT)
public class BabyFireDragonRenderer extends ChickenRenderer {
    public BabyFireDragonRenderer(EntityRendererProvider.Context ctx) {
        super(ctx);
    }

    @Override
    public Identifier getTextureLocation(ChickenRenderState renderState) {
        return Wingbound.id("textures/entity/baby_fire_dragon.png");
    }
}
