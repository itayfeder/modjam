package net.aritsu.client.renderer.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;

public class HikerArmorModel extends PlayerModel<AbstractClientPlayer> {
    public HikerArmorModel(ModelPart p_170821_, boolean p_170822_) {
        super(p_170821_, p_170822_);
    }

    @Override
    public void renderToBuffer(PoseStack p_102034_, VertexConsumer p_102035_, int p_102036_, int p_102037_, float p_102038_, float p_102039_, float p_102040_, float p_102041_) {

        //hijack rendering to set secondairy parts visible.
        //armor rendering sets all parts to invisible and sets only base humanoid model parts visible
        this.leftSleeve.visible = this.leftArm.visible;
        this.rightSleeve.visible = this.rightArm.visible;
        this.leftPants.visible = this.leftLeg.visible;
        this.rightPants.visible = this.rightLeg.visible;
        this.jacket.visible = this.body.visible;

        this.leftSleeve.copyFrom(this.leftArm);
        this.rightSleeve.copyFrom(this.rightArm);
        this.rightPants.copyFrom(this.rightLeg);
        this.leftPants.copyFrom(this.leftLeg);
        this.jacket.copyFrom(this.body);

        super.renderToBuffer(p_102034_, p_102035_, p_102036_, p_102037_, p_102038_, p_102039_, p_102040_, p_102041_);

    }
}
