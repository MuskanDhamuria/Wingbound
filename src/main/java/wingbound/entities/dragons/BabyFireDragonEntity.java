package wingbound.entities.dragons;

import java.util.UUID;

import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FleeSunGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.chicken.Chicken;
import net.minecraft.world.entity.player.Input;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraft.core.particles.ParticleTypes;
import wingbound.Wingbound;

public class BabyFireDragonEntity extends Chicken {
	private static final EntityDataAccessor<Integer> FLIGHT_DIRECTION = SynchedEntityData.defineId(BabyFireDragonEntity.class, EntityDataSerializers.INT);

	public enum GrowthStage {
		BABY,
		JUVENILE,
		ADULT
	}

	private static final int FEED_BOND_POINTS = 5;
	private static final int MAX_BOND_POINTS = 100;
	private static final double FLY_VERTICAL_SPEED = 0.35D;

	private UUID ownerUuid;
	private int bondPoints;

	public BabyFireDragonEntity(EntityType<? extends BabyFireDragonEntity> entityType, Level level) {
		super(entityType, level);
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		super.defineSynchedData(builder);
		builder.define(FLIGHT_DIRECTION, 0);
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

	public int getBondPoints() {
		return this.bondPoints;
	}

	public String getBondLevel() {
		if (this.bondPoints >= MAX_BOND_POINTS) {
			return "BFF!!";
		}
		if (this.bondPoints >= 75) {
			return "Best Friend";
		}
		if (this.bondPoints >= 50) {
			return "Friend";
		}
		if (this.bondPoints >= 25) {
			return "Curious";
		}
		return "Stranger";
	}

	public GrowthStage getGrowthStage() {
		if (this.bondPoints >= MAX_BOND_POINTS) {
			return GrowthStage.ADULT;
		}
		if (this.bondPoints >= 50) {
			return GrowthStage.JUVENILE;
		}
		return GrowthStage.BABY;
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
		return stack.is(Items.COOKED_BEEF) || stack.is(Items.COOKED_CHICKEN);
	}

	@Override
	public InteractionResult mobInteract(Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		if (stack.isEmpty()) {
			if (this.getGrowthStage() != GrowthStage.ADULT) {
				if (player instanceof ServerPlayer serverPlayer) {
					serverPlayer.sendSystemMessage(Component.literal("This dragon is not big enough to ride yet."));
				}
				return InteractionResult.SUCCESS;
			}
			if (!this.level().isClientSide()) {
				player.startRiding(this);
			}
			return InteractionResult.SUCCESS;
		}

		if (!this.isFood(stack)) {
			return super.mobInteract(player, hand);
		}

		if (this.level() instanceof ServerLevel serverLevel) {
			this.bondPoints = Math.min(this.bondPoints + FEED_BOND_POINTS, MAX_BOND_POINTS);
			serverLevel.sendParticles(ParticleTypes.HEART, this.getX(), this.getY() + 0.8D, this.getZ(), 3, 0.25D, 0.25D, 0.25D, 0.0D);
			Wingbound.LOGGER.debug(
				"[BabyFireDragon] Fed with {} by {}, bond is now {}/100 ({}), growth is {}",
				stack.getItem(),
				player.getName().getString(),
				this.bondPoints,
				this.getBondLevel(),
				this.getGrowthStage()
			);
			if (player instanceof ServerPlayer serverPlayer) {
				serverPlayer.sendSystemMessage(Component.literal(
					"Bond: " + this.bondPoints + "/100 - " + this.getBondLevel() + "\nGrowth: " + this.getGrowthStage()
				));
			}
			if (!player.getAbilities().instabuild) {
				stack.shrink(1);
			}
		}

		return InteractionResult.SUCCESS;
	}

	@Override
	protected boolean canAddPassenger(Entity entity) {
		return this.getPassengers().isEmpty();
	}

	@Override
	public LivingEntity getControllingPassenger() {
		return this.getFirstPassenger() instanceof LivingEntity livingEntity ? livingEntity : null;
	}

	@Override
	public void travel(Vec3 travelVector) {
		if (this.isAlive() && this.isVehicle() && this.getControllingPassenger() instanceof Player player) {
			this.setYRot(player.getYRot());
			this.yRotO = this.getYRot();
			this.setXRot(player.getXRot() * 0.5F);
			this.setRot(this.getYRot(), this.getXRot());
			this.yBodyRot = this.getYRot();
			this.yHeadRot = this.yBodyRot;

			float strafe = player.xxa * 0.5F;
			float forward = player.zza;
			if (forward <= 0.0F) {
				forward *= 0.25F;
			}

			boolean canFly = this.getGrowthStage() == GrowthStage.ADULT;
			if (canFly) {
				int flightDirection = this.entityData.get(FLIGHT_DIRECTION);
				double verticalMovement = flightDirection > 0
					? FLY_VERTICAL_SPEED
					: flightDirection < 0
						? -FLY_VERTICAL_SPEED
						: 0.0D;
				this.setNoGravity(true);
				this.setOnGround(false);
				this.fallDistance = 0.0F;
				if (verticalMovement != 0.0D) {
					this.setPos(this.getX(), this.getY() + verticalMovement, this.getZ());
				}
				this.setSpeed((float)this.getAttributeValue(Attributes.MOVEMENT_SPEED));
				super.travel(new Vec3(strafe, 0.0D, forward));
				Vec3 deltaMovement = this.getDeltaMovement();
				this.setDeltaMovement(deltaMovement.x, 0.0D, deltaMovement.z);
				return;
			}

			this.setSpeed((float)this.getAttributeValue(Attributes.MOVEMENT_SPEED));
			this.setNoGravity(false);
			super.travel(new Vec3(strafe, 0.0D, forward));
			return;
		}

		this.setNoGravity(false);
		super.travel(travelVector);
	}

	@Override
	public void aiStep() {
		super.aiStep();

		if (!(this.getControllingPassenger() instanceof Player) || this.getGrowthStage() != GrowthStage.ADULT) {
			if (!this.level().isClientSide()) {
				this.entityData.set(FLIGHT_DIRECTION, 0);
			}
			this.setNoGravity(false);
			return;
		}

		this.setNoGravity(true);
		this.fallDistance = 0.0F;
	}

	@Override
	public void tick() {
		super.tick();

		if (!(this.getControllingPassenger() instanceof Player) || this.getGrowthStage() != GrowthStage.ADULT) {
			return;
		}

		int flightDirection = this.entityData.get(FLIGHT_DIRECTION);
		if (flightDirection == 0) {
			return;
		}

		this.setNoGravity(true);
		this.setOnGround(false);
		this.fallDistance = 0.0F;
		this.setPos(this.getX(), this.getY() + (flightDirection > 0 ? FLY_VERTICAL_SPEED : -FLY_VERTICAL_SPEED), this.getZ());
	}

	@Override
	protected void customServerAiStep(ServerLevel serverLevel) {
		super.customServerAiStep(serverLevel);

		if (!(this.getControllingPassenger() instanceof ServerPlayer serverPlayer) || this.getGrowthStage() != GrowthStage.ADULT) {
			this.entityData.set(FLIGHT_DIRECTION, 0);
			return;
		}

		Input input = serverPlayer.getLastClientInput();
		this.entityData.set(FLIGHT_DIRECTION, input.jump() ? 1 : input.shift() ? -1 : 0);
	}

	@Override
	protected Vec3 getPassengerAttachmentPoint(Entity entity, EntityDimensions entityDimensions, float partialTick) {
		return new Vec3(0.0D, this.getBbHeight() * 0.8D, 0.0D);
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
