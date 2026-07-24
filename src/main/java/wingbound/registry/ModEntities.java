package wingbound.registry;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

import wingbound.Wingbound;
import wingbound.entities.dragons.BabyFireDragonEntity;

public final class ModEntities {
	public static EntityType<BabyFireDragonEntity> BABY_FIRE_DRAGON;

	private ModEntities() {
	}

	public static void register() {
			BABY_FIRE_DRAGON = Registry.register(
			BuiltInRegistries.ENTITY_TYPE,
			Wingbound.id("baby_fire_dragon"),
			EntityType.Builder.<BabyFireDragonEntity>of(BabyFireDragonEntity::new, MobCategory.CREATURE)
				.sized(8.0F, 6.5F)
				.build(ResourceKey.create(Registries.ENTITY_TYPE, Wingbound.id("baby_fire_dragon")))
		);

		FabricDefaultAttributeRegistry.register(BABY_FIRE_DRAGON, BabyFireDragonEntity.createAttributes().build());
	}
}
