package wingbound.entities.dragons;

import java.util.UUID;

import com.geckolib.animatable.GeoEntity;
import com.geckolib.animatable.instance.AnimatableInstanceCache;
import com.geckolib.animatable.manager.AnimatableManager;
import com.geckolib.animation.AnimationController;
import com.geckolib.animation.RawAnimation;
import com.geckolib.util.GeckoLibUtil;

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
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.projectile.hurtingprojectile.SmallFireball;

public class BabyFireDragonEntity extends Chicken implements GeoEntity {
	private static final EntityDataAccessor<Integer> FLIGHT_DIRECTION = SynchedEntityData.defineId(BabyFireDragonEntity.class, EntityDataSerializers.INT);

	private static final RawAnimation IDLE_ANIMATION = RawAnimation.begin().thenLoop("idle");
	private static final float FEED_HEAL_AMOUNT = 4.0F;
	private static final double FLY_VERTICAL_SPEED = 0.2D;
	private static final double RIDDEN_FLY_SPEED = 0.55D;
	private static final double RIDER_HEIGHT_OFFSET = 4.25D;
	private static final double RIDER_FORWARD_OFFSET = -0.35D;

	private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);
	private UUID ownerUuid;
	private int attackCooldown;

	public BabyFireDragonEntity(EntityType<? extends BabyFireDragonEntity> entityType, Level level) {
		super(entityType, level);
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		super.defineSynchedData(builder);
		builder.define(FLIGHT_DIRECTION, 0);
	}

	@Override
	public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
		controllers.add(new AnimationController<>("idle", 5, state -> state.setAndContinue(IDLE_ANIMATION)));
	}

	@Override
	public AnimatableInstanceCache getAnimatableInstanceCache() {
		return this.geoCache;
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
		return stack.is(Items.COOKED_BEEF) || stack.is(Items.COOKED_CHICKEN);
	}

	@Override
	public InteractionResult mobInteract(Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		if (stack.isEmpty()) {
			if (!this.level().isClientSide()) {
				player.startRiding(this);
			}
			return InteractionResult.SUCCESS;
		}

		if (!this.isFood(stack)) {
			return super.mobInteract(player, hand);
		}

		if (this.level() instanceof ServerLevel serverLevel) {
			this.heal(FEED_HEAL_AMOUNT);
			serverLevel.sendParticles(ParticleTypes.HEART, this.getX(), this.getY() + 0.8D, this.getZ(), 3, 0.25D, 0.25D, 0.25D, 0.0D);
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

			this.setNoGravity(true);
			this.setOnGround(false);
			this.fallDistance = 0.0F;
			this.setSpeed((float)RIDDEN_FLY_SPEED);
			super.travel(new Vec3(strafe, 0.0D, forward));
			Vec3 deltaMovement = this.getDeltaMovement();
			this.setDeltaMovement(deltaMovement.x, this.entityData.get(FLIGHT_DIRECTION) * FLY_VERTICAL_SPEED, deltaMovement.z);
			return;
		}

		this.setNoGravity(false);
		super.travel(travelVector);
	}

	@Override
	public void aiStep() {
		super.aiStep();

		if (!(this.getControllingPassenger() instanceof Player)) {
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

		if (this.attackCooldown > 0) {
			this.attackCooldown--;
		}

		if (!(this.getControllingPassenger() instanceof Player)) {
			return;
		}

		int flightDirection = this.entityData.get(FLIGHT_DIRECTION);
		if (flightDirection == 0) {
			return;
		}

		this.setNoGravity(true);
		this.setOnGround(false);
		this.fallDistance = 0.0F;
		Vec3 deltaMovement = this.getDeltaMovement();
		this.setDeltaMovement(deltaMovement.x, flightDirection > 0 ? FLY_VERTICAL_SPEED : -FLY_VERTICAL_SPEED, deltaMovement.z);
	}

	@Override
	protected void customServerAiStep(ServerLevel serverLevel) {
		super.customServerAiStep(serverLevel);

		if (!(this.getControllingPassenger() instanceof ServerPlayer)) {
			this.entityData.set(FLIGHT_DIRECTION, 0);
		}
	}

	public void applyDownloadedModControls(boolean rise, boolean descend, boolean attack) {
		if (!this.isVehicle()) {
			this.entityData.set(FLIGHT_DIRECTION, 0);
			return;
		}

		this.entityData.set(FLIGHT_DIRECTION, rise ? 1 : descend ? -1 : 0);
		if (attack) {
			this.shootFireballBurst();
		}
	}

	private void shootFireballBurst() {
		if (this.level().isClientSide() || this.attackCooldown > 0) {
			return;
		}

		this.attackCooldown = 10;
		Vec3 lookAngle = this.getLookAngle();
		for (int i = 0; i < 8; i++) {
			SmallFireball fireball = new SmallFireball(this.level(), this, lookAngle);
			fireball.setPos(this.getX(), this.getEyeY() - 0.1D, this.getZ());
			this.level().addFreshEntity(fireball);
		}
	}

	@Override
	protected Vec3 getPassengerAttachmentPoint(Entity entity, EntityDimensions entityDimensions, float partialTick) {
		return new Vec3(0.0D, RIDER_HEIGHT_OFFSET, RIDER_FORWARD_OFFSET);
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
