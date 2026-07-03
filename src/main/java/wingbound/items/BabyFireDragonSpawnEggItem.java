package wingbound.items;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

import wingbound.entities.dragons.BabyFireDragonEntity;
import wingbound.registry.ModEntities;
import wingbound.Wingbound;

public class BabyFireDragonSpawnEggItem extends Item {
	public BabyFireDragonSpawnEggItem(Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		Level level = context.getLevel();
		Player player = context.getPlayer();
		BlockPos pos = context.getClickedPos().relative(context.getClickedFace());

		if (level.isClientSide()) {
			return InteractionResult.SUCCESS;
		}

		Wingbound.LOGGER.info("[SpawnEgg] useOn called on server at {} by {}", pos, player == null ? "unknown" : player.getName().getString());
		BabyFireDragonEntity entity = ModEntities.BABY_FIRE_DRAGON.create(level, EntitySpawnReason.SPAWN_ITEM_USE);
		Wingbound.LOGGER.info("[SpawnEgg] create() returned {}", entity == null ? "null" : entity.getType().toString());
		if (entity == null) {
			return InteractionResult.PASS;
		}

		entity.setPos(pos.getX() + 0.5D, pos.getY() + 0.1D, pos.getZ() + 0.5D);
		entity.setPersistenceRequired();
		if (player instanceof net.minecraft.server.level.ServerPlayer serverPlayer) {
			entity.setOwner(serverPlayer);
		}
		level.addFreshEntity(entity);

		if (player != null && !player.getAbilities().instabuild) {
			context.getItemInHand().shrink(1);
		}

		return InteractionResult.CONSUME;
	}
}
