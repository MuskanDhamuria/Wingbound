package wingbound.client.render;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.entity.state.ChickenRenderState;
import net.minecraft.util.Mth;

public class DragonModel extends EntityModel<ChickenRenderState> {
	private final ModelPart root;
	private final ModelPart head;
	private final ModelPart jaw;
	private final ModelPart neck;
	private final ModelPart leftWing;
	private final ModelPart leftWingTip;
	private final ModelPart rightWing;
	private final ModelPart rightWingTip;
	private final ModelPart tail;
	private final ModelPart frontLeftLeg;
	private final ModelPart frontRightLeg;
	private final ModelPart backLeftLeg;
	private final ModelPart backRightLeg;

	public DragonModel(ModelPart root) {
		super(root);
		this.root = root.getChild("dragon");
		this.head = this.root.getChild("head");
		this.jaw = this.head.getChild("jaw");
		this.neck = this.root.getChild("neck");
		this.leftWing = this.root.getChild("left_wing");
		this.leftWingTip = this.leftWing.getChild("left_wing_tip");
		this.rightWing = this.root.getChild("right_wing");
		this.rightWingTip = this.rightWing.getChild("right_wing_tip");
		this.tail = this.root.getChild("tail");
		this.frontLeftLeg = this.root.getChild("front_left_leg");
		this.frontRightLeg = this.root.getChild("front_right_leg");
		this.backLeftLeg = this.root.getChild("back_left_leg");
		this.backRightLeg = this.root.getChild("back_right_leg");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();

		PartDefinition dragon = root.addOrReplaceChild(
			"dragon",
			CubeListBuilder.create(),
			PartPose.offset(0.0F, 18.0F, 0.0F)
		);

		dragon.addOrReplaceChild(
			"body",
			CubeListBuilder.create()
				.texOffs(64, 64).addBox(-8.0F, -10.0F, -18.0F, 16.0F, 14.0F, 36.0F, new CubeDeformation(0.0F))
				.texOffs(232, 53).addBox(-1.0F, -15.0F, -14.0F, 2.0F, 5.0F, 8.0F, new CubeDeformation(0.0F))
				.texOffs(232, 53).addBox(-1.0F, -15.0F, -2.0F, 2.0F, 5.0F, 8.0F, new CubeDeformation(0.0F))
				.texOffs(232, 53).addBox(-1.0F, -15.0F, 10.0F, 2.0F, 5.0F, 8.0F, new CubeDeformation(0.0F)),
			PartPose.ZERO
		);

		dragon.addOrReplaceChild(
			"neck",
			CubeListBuilder.create()
				.texOffs(202, 104).addBox(-4.0F, -4.0F, -18.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
				.texOffs(202, 104).addBox(-4.0F, -3.0F, -25.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
				.texOffs(54, 0).addBox(-1.0F, -9.0F, -24.0F, 2.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)),
			PartPose.offset(0.0F, -7.0F, -12.0F)
		);

		PartDefinition head = dragon.addOrReplaceChild(
			"head",
			CubeListBuilder.create()
				.texOffs(128, 30).addBox(-6.0F, -7.0F, -14.0F, 12.0F, 12.0F, 12.0F, new CubeDeformation(0.0F))
				.texOffs(192, 44).addBox(-4.5F, -4.0F, -27.0F, 9.0F, 4.0F, 14.0F, new CubeDeformation(0.0F))
				.texOffs(6, 0).addBox(2.5F, -11.0F, -9.0F, 2.0F, 5.0F, 5.0F, new CubeDeformation(0.0F))
				.texOffs(8, 0).addBox(-4.5F, -11.0F, -9.0F, 2.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)),
			PartPose.offset(0.0F, -9.0F, -31.0F)
		);

		head.addOrReplaceChild(
			"jaw",
			CubeListBuilder.create().texOffs(192, 65).addBox(-4.5F, 0.0F, -27.0F, 9.0F, 3.0F, 14.0F, new CubeDeformation(0.0F)),
			PartPose.rotation(0.12F, 0.0F, 0.0F)
		);

		PartDefinition leftWing = dragon.addOrReplaceChild(
			"left_wing",
			CubeListBuilder.create()
				.texOffs(120, 88).addBox(0.0F, -1.5F, -2.0F, 32.0F, 3.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(0, 88).addBox(0.0F, 0.0F, 2.0F, 32.0F, 1.0F, 24.0F, new CubeDeformation(-0.45F)),
			PartPose.offset(8.0F, -8.0F, -10.0F)
		);
		leftWing.addOrReplaceChild(
			"left_wing_tip",
			CubeListBuilder.create()
				.texOffs(116, 136).addBox(0.0F, -1.0F, -1.0F, 28.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(0, 144).addBox(0.0F, 0.0F, 2.0F, 28.0F, 1.0F, 22.0F, new CubeDeformation(-0.45F)),
			PartPose.offset(31.0F, 0.0F, 0.0F)
		);

		PartDefinition rightWing = dragon.addOrReplaceChild(
			"right_wing",
			CubeListBuilder.create()
				.texOffs(120, 88).mirror().addBox(-32.0F, -1.5F, -2.0F, 32.0F, 3.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(0, 88).mirror().addBox(-32.0F, 0.0F, 2.0F, 32.0F, 1.0F, 24.0F, new CubeDeformation(-0.45F)),
			PartPose.offset(-8.0F, -8.0F, -10.0F)
		);
		rightWing.addOrReplaceChild(
			"right_wing_tip",
			CubeListBuilder.create()
				.texOffs(116, 136).mirror().addBox(-28.0F, -1.0F, -1.0F, 28.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(0, 144).mirror().addBox(-28.0F, 0.0F, 2.0F, 28.0F, 1.0F, 22.0F, new CubeDeformation(-0.45F)),
			PartPose.offset(-31.0F, 0.0F, 0.0F)
		);

		dragon.addOrReplaceChild(
			"front_left_leg",
			CubeListBuilder.create().texOffs(112, 112).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 14.0F, 4.0F, new CubeDeformation(0.0F)),
			PartPose.offset(5.5F, -1.0F, -12.0F)
		);
		dragon.addOrReplaceChild(
			"front_right_leg",
			CubeListBuilder.create().texOffs(112, 112).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 14.0F, 4.0F, new CubeDeformation(0.0F)),
			PartPose.offset(-5.5F, -1.0F, -12.0F)
		);
		dragon.addOrReplaceChild(
			"back_left_leg",
			CubeListBuilder.create().texOffs(0, 16).addBox(-3.0F, 0.0F, -3.0F, 6.0F, 15.0F, 6.0F, new CubeDeformation(0.0F)),
			PartPose.offset(6.5F, -1.0F, 12.0F)
		);
		dragon.addOrReplaceChild(
			"back_right_leg",
			CubeListBuilder.create().texOffs(0, 16).mirror().addBox(-3.0F, 0.0F, -3.0F, 6.0F, 15.0F, 6.0F, new CubeDeformation(0.0F)),
			PartPose.offset(-6.5F, -1.0F, 12.0F)
		);

		dragon.addOrReplaceChild(
			"tail",
			CubeListBuilder.create()
				.texOffs(202, 104).addBox(-4.0F, -4.0F, 0.0F, 8.0F, 8.0F, 12.0F, new CubeDeformation(0.0F))
				.texOffs(202, 104).addBox(-3.0F, -3.0F, 11.0F, 6.0F, 6.0F, 13.0F, new CubeDeformation(0.0F))
				.texOffs(54, 0).addBox(-1.0F, -8.0F, 6.0F, 2.0F, 5.0F, 6.0F, new CubeDeformation(0.0F)),
			PartPose.offset(0.0F, -4.0F, 18.0F)
		);

		return LayerDefinition.create(mesh, 256, 256);
	}

	@Override
	public void setupAnim(ChickenRenderState state) {
		super.setupAnim(state);
		float time = state.ageInTicks * 0.28F;
		float wingFlap = Mth.sin(time) * 0.85F;
		float walk = state.walkAnimationPos;
		float walkSpeed = Math.min(state.walkAnimationSpeed, 1.0F);

		this.head.xRot = state.xRot * Mth.DEG_TO_RAD * 0.45F + Mth.sin(time * 0.35F) * 0.05F;
		this.head.yRot = state.yRot * Mth.DEG_TO_RAD * 0.35F;
		this.jaw.xRot = 0.12F + Mth.sin(time * 0.8F) * 0.08F;
		this.neck.xRot = -0.12F + Mth.sin(time * 0.4F) * 0.04F;

		this.leftWing.zRot = 0.25F + wingFlap;
		this.leftWingTip.zRot = 0.35F + wingFlap * 0.8F;
		this.rightWing.zRot = -0.25F - wingFlap;
		this.rightWingTip.zRot = -0.35F - wingFlap * 0.8F;

		this.tail.yRot = Mth.sin(time * 0.5F) * 0.18F;
		this.tail.xRot = -0.18F + Mth.cos(time * 0.4F) * 0.06F;

		this.frontLeftLeg.xRot = Mth.cos(walk * 0.6662F) * 0.7F * walkSpeed;
		this.frontRightLeg.xRot = Mth.cos(walk * 0.6662F + Mth.PI) * 0.7F * walkSpeed;
		this.backLeftLeg.xRot = Mth.cos(walk * 0.6662F + Mth.PI) * 0.65F * walkSpeed;
		this.backRightLeg.xRot = Mth.cos(walk * 0.6662F) * 0.65F * walkSpeed;
	}
}
