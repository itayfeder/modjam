package net.aritsu.client.renderer.models;

import com.mojang.blaze3d.vertex.PoseStack;
import net.aritsu.entity.grizzly_bear.GrizzlyBear;
import net.minecraft.client.model.QuadrupedModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;

public class GrizzlyBearModel<T extends GrizzlyBear> extends QuadrupedModel<T> {

    private float sitAmount;

    public GrizzlyBearModel(ModelPart p_170857_) {
        super(p_170857_, true, 16.0F, 4.0F, 2.25F, 2.0F, 24);
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-3.5F, -3.0F, -4.0F, 7.0F, 7.0F, 7.0F).texOffs(0, 38).addBox("mouth", -2.5F, 1.0F, -7.0F, 5.0F, 3.0F, 3.0F).texOffs(0, 0).addBox("right_ear", 2.5F, -5.0F, -2.0F, 2.0F, 2.0F, 1.0F).texOffs(0, 3).mirror().addBox("left_ear", -4.5F, -5.0F, -2.0F, 2.0F, 2.0F, 1.0F), PartPose.offset(0.0F, 10.0F, -15.0F));
        partdefinition.addOrReplaceChild("body", CubeListBuilder.create()
                .texOffs(1, 16).addBox(-9.0F, -26.0F, -7.0F, 14.0F, 10.0F, 11.0F)
                .texOffs(79, 23).addBox(-9.0F, -6.0F, -7.0F, 14.0F, 10.0F, 10.0F)
                .texOffs(41, 0).addBox(-9.0F, -16.0F, -8.5F, 14.0F, 10.0F, 12.0F)
                .texOffs(13, 50).addBox(5.0F, -26.0F, -11.0F, 0.0F, 10.0F, 4.0F)
                .texOffs(13, 50).addBox(5.0F, -6.0F, -11.0F, 0.0F, 10.0F, 4.0F)
                .texOffs(13, 50).addBox(-9.0F, -6.0F, -11.0F, 0.0F, 10.0F, 4.0F)
                .texOffs(13, 50).addBox(-9.0F, -26.0F, -11.0F, 0.0F, 10.0F, 4.0F)
                .texOffs(9, 45).addBox(-9.0F, 4.0F, -11.0F, 14.0F, 0.0F, 4.0F)
                .texOffs(18, 0).addBox(-9.0F, -26.0F, -11.0F, 14.0F, 0.0F, 4.0F), PartPose.offsetAndRotation(2.0F, 9.0F, 12.0F, ((float) Math.PI / 2F), 0.0F, 0.0F));
        CubeListBuilder cubelistbuilder = CubeListBuilder.create().texOffs(42, 47).addBox(-2.0F, 0.0F, 1.5F, 4.0F, 10.0F, 7.0F);
        partdefinition.addOrReplaceChild("right_hind_leg", cubelistbuilder, PartPose.offset(-4.5F, 14.0F, 6.0F));
        partdefinition.addOrReplaceChild("left_hind_leg", cubelistbuilder, PartPose.offset(4.5F, 14.0F, 6.0F));
        CubeListBuilder cubelistbuilder1 = CubeListBuilder.create().texOffs(88, 48).addBox(-1.0F, 0.0F, -3.0F, 4.0F, 10.0F, 6.0F);
        partdefinition.addOrReplaceChild("right_front_leg", cubelistbuilder1, PartPose.offset(-4.5F, 14.0F, -8.0F));
        partdefinition.addOrReplaceChild("left_front_leg", cubelistbuilder1, PartPose.offset(3.5F, 14.0F, -8.0F));
        return LayerDefinition.create(meshdefinition, 128, 64);
    }

    @Override
    public void prepareMobModel(T entity, float p_102615_, float p_102616_, float partialTicks) {
        super.prepareMobModel(entity, p_102615_, p_102616_, partialTicks);
        this.sitAmount = entity.getSitAmount(partialTicks);

    }

    public void setupAnim(T entity, float p_103430_, float p_103431_, float swing, float p_103433_, float p_103434_) {
        this.leftHindLeg.setRotation(0, 0, 0);
        this.rightHindLeg.setRotation(0, 0, 0);
        this.leftFrontLeg.setRotation(0, 0, 0);
        this.rightFrontLeg.setRotation(0, 0, 0);
        this.rightFrontLeg.x = -5.0F;
        this.leftFrontLeg.x = 3.0F;
        super.setupAnim(entity, p_103430_, p_103431_, swing, p_103433_, p_103434_);


        float tick = swing - (float) entity.tickCount;
        float standScale = entity.getStandingAnimationScale(tick);
        standScale = standScale * standScale;
        float f2 = 1.0F - standScale;
        this.body.xRot = ((float) Math.PI / 2F) - standScale * (float) Math.PI * 0.35F;
        this.body.y = 9.0F * f2 + 11.0F * standScale;
        this.rightFrontLeg.y = 14.0F * f2 - 6.0F * standScale;
        this.rightFrontLeg.z = -8.0F * f2 - 4.0F * standScale;
        this.rightFrontLeg.xRot -= standScale * (float) Math.PI * 0.45F;
        this.leftFrontLeg.y = this.rightFrontLeg.y;
        this.leftFrontLeg.z = this.rightFrontLeg.z;
        this.leftFrontLeg.xRot -= standScale * (float) Math.PI * 0.45F;
        if (this.young) {
            this.head.y = 10.0F * f2 - 9.0F * standScale;
            this.head.z = -16.0F * f2 - 7.0F * standScale;
        } else {
            this.head.y = 10.0F * f2 - 14.0F * standScale;
            this.head.z = -16.0F * f2 - 3.0F * standScale;
        }

        this.head.xRot += standScale * (float) Math.PI * 0.15F;

        if (this.sitAmount > 0.0F) {
            if (this.young) {
                this.head.y = 10.0F * f2 - 2.0F * standScale;
                this.head.z = -16.0F * f2 - 7.0F * standScale;
            } else {
                this.head.y = 10.0F * f2 - 7.0F * standScale;
                this.head.z = -16.0F * f2 - 3.0F * standScale;
            }

            this.body.xRot = ((float) Math.PI / 2F) - standScale * (float) Math.PI * 0.35F;
            this.body.y = 9.0F * f2 + 18.0F * standScale;

            this.rightFrontLeg.y = 14.0F * f2 + 2.0F * standScale;
            this.rightFrontLeg.z = -8.0F * f2 - 2.0F * standScale;
            this.leftFrontLeg.y = this.rightFrontLeg.y;
            this.leftFrontLeg.z = this.rightFrontLeg.z;

            this.rightHindLeg.xRot = -1.5f;
            this.leftHindLeg.xRot = -1.5f;
            this.rightHindLeg.yRot = 0.4f;
            this.leftHindLeg.yRot = -0.4f;

            this.rightFrontLeg.zRot = 0.27079642F * standScale;
            this.leftFrontLeg.zRot = -0.27079642F * standScale;
            this.head.xRot = 0.2f * Mth.sin(swing * 0.4F);
            this.rightFrontLeg.xRot = -(1.5F - 0.2F * Mth.sin(swing * 0.6F)) * standScale;
            this.leftFrontLeg.xRot = -(1.5F - 0.2F * Mth.sin(swing * 0.6F)) * standScale;

        }
    }

    public void translateToFrontRightLeg(PoseStack stack) {
            rightFrontLeg.translateAndRotate(stack);
    }
}
