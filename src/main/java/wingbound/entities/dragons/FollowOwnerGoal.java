package wingbound.entities.dragons;

import java.util.EnumSet;
import java.util.UUID;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;

public class FollowOwnerGoal extends Goal {
    private final BabyFireDragonEntity dragon;
    private final double speedModifier;
    private final float startDistance;
    private final float stopDistance;
    private ServerPlayer owner;

    public FollowOwnerGoal(BabyFireDragonEntity dragon, double speedModifier, float startDistance, float stopDistance) {
        this.dragon = dragon;
        this.speedModifier = speedModifier;
        this.startDistance = startDistance;
        this.stopDistance = stopDistance;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        ServerPlayer owner = this.dragon.getOwner();
        if (owner == null) {
            return false;
        }

        double distanceSq = this.dragon.distanceToSqr(owner);
        if (distanceSq <= (double)(this.startDistance * this.startDistance)) {
            return false;
        }

        this.owner = owner;
        return true;
    }

    @Override
    public boolean canContinueToUse() {
        if (this.owner == null) {
            return false;
        }

        if (!this.dragon.getNavigation().isDone()) {
            return true;
        }

        double distanceSq = this.dragon.distanceToSqr(this.owner);
        return distanceSq > (double)(this.startDistance * this.startDistance);
    }

    @Override
    public void start() {
        this.owner = this.dragon.getOwner();
    }

    @Override
    public void stop() {
        this.owner = null;
        this.dragon.getNavigation().stop();
    }

    @Override
    public void tick() {
        if (this.owner == null) {
            return;
        }

        double distanceSq = this.dragon.distanceToSqr(this.owner);
        if (distanceSq > 400.0D) {
            this.dragon.teleportTo(this.owner.getX(), this.owner.getY(), this.owner.getZ());
            return;
        }

        if (distanceSq > (double)(this.startDistance * this.startDistance)) {
            this.dragon.getNavigation().moveTo(this.owner, this.speedModifier);
        }
    }
}
