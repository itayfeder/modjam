package net.aritsu.client.renderer.models;

import net.aritsu.entity.grizzly_bear.GrizzlyBear;
import net.minecraft.client.model.QuadrupedModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public class GrizzlyBearModel<T extends GrizzlyBear> extends QuadrupedModel<T> {

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
        int i = 10;
        CubeListBuilder cubelistbuilder = CubeListBuilder.create().texOffs(42, 47).addBox(-2.0F, 0.0F, 1.5F, 4.0F, 10.0F, 7.0F);
        partdefinition.addOrReplaceChild("right_hind_leg", cubelistbuilder, PartPose.offset(-4.5F, 14.0F, 6.0F));
        partdefinition.addOrReplaceChild("left_hind_leg", cubelistbuilder, PartPose.offset(4.5F, 14.0F, 6.0F));
        CubeListBuilder cubelistbuilder1 = CubeListBuilder.create().texOffs(88, 48).addBox(-1.0F, 0.0F, -3.0F, 4.0F, 10.0F, 6.0F);
        partdefinition.addOrReplaceChild("right_front_leg", cubelistbuilder1, PartPose.offset(-3.5F, 14.0F, -8.0F));
        partdefinition.addOrReplaceChild("left_front_leg", cubelistbuilder1, PartPose.offset(3.5F, 14.0F, -8.0F));
        return LayerDefinition.create(meshdefinition, 128, 64);
    }

    public void setupAnim(T p_103429_, float p_103430_, float p_103431_, float p_103432_, float p_103433_, float p_103434_) {
        super.setupAnim(p_103429_, p_103430_, p_103431_, p_103432_, p_103433_, p_103434_);
        float f = p_103432_ - (float) p_103429_.tickCount;
        float f1 = p_103429_.getStandingAnimationScale(f);
        f1 = f1 * f1;
        float f2 = 1.0F - f1;
        this.body.xRot = ((float) Math.PI / 2F) - f1 * (float) Math.PI * 0.35F;
        this.body.y = 9.0F * f2 + 11.0F * f1;
        this.rightFrontLeg.y = 14.0F * f2 - 6.0F * f1;
        this.rightFrontLeg.z = -8.0F * f2 - 4.0F * f1;
        this.rightFrontLeg.xRot -= f1 * (float) Math.PI * 0.45F;
        this.leftFrontLeg.y = this.rightFrontLeg.y;
        this.leftFrontLeg.z = this.rightFrontLeg.z;
        this.leftFrontLeg.xRot -= f1 * (float) Math.PI * 0.45F;
        if (this.young) {
            this.head.y = 10.0F * f2 - 9.0F * f1;
            this.head.z = -16.0F * f2 - 7.0F * f1;
        } else {
            this.head.y = 10.0F * f2 - 14.0F * f1;
            this.head.z = -16.0F * f2 - 3.0F * f1;
        }

        this.head.xRot += f1 * (float) Math.PI * 0.15F;
    }
}
