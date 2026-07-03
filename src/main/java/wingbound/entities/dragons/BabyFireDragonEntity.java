package wingbound.entities.dragons;

import java.util.UUID;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FleeSunGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.chicken.Chicken;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class BabyFireDragonEntity extends Chicken {
	private UUID ownerUuid;

	public BabyFireDragonEntity(EntityType<? extends BabyFireDragonEntity> entityType, Level level) {
		super(entityType, level);
	}

	public void setOwner(ServerPlayer player) {
		this.ownerUuid = player.getUUID();
	}

	public ServerPlayer getOwner() {
		if (this.level() instanceof ServerLevel serverLevel && this.ownerUuid != null) {
			net.minecraft.world.entity.Entity entity = serverLevel.getEntity(this.ownerUuid);
			return entity instanceof ServerPlayer ? (ServerPlayer)entity : null;
		}
		return null;
	}

	public static AttributeSupplier.Builder createAttributes() {
		return Animal.createAnimalAttributes()
			.add(Attributes.MAX_HEALTH, 20.0D)
			.add(Attributes.MOVEMENT_SPEED, 0.22D)
			.add(Attributes.JUMP_STRENGTH, 0.6D);
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(1, new FleeSunGoal(this, 1.1D));
		this.goalSelector.addGoal(2, new FollowOwnerGoal(this, 1.0D, 3.0F, 20.0F));
		this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 8.0F));
		this.goalSelector.addGoal(4, new WaterAvoidingRandomStrollGoal(this, 1.0D));
		this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));
		this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
	}

	@Override
	public boolean isFood(ItemStack stack) {
		return false;
	}

	@Override
	public boolean removeWhenFarAway(double distanceToClosestPlayer) {
		return false;
	}

	@Override
	public boolean canBreed() {
		return false;
	}

	@Override
	public Chicken getBreedOffspring(ServerLevel serverLevel, AgeableMob ageableMob) {
		return null;
	}
}
