package net.aritsu.client.renderer.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.player.AbstractClientPlayer;

public class BackpackModel extends EntityModel<AbstractClientPlayer> {

    private final ModelPart backpack;

    public BackpackModel(ModelPart p_170903_) {
        backpack = p_170903_.getChild("backpack");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        partdefinition.addOrReplaceChild("backpack", CubeListBuilder.create()
                        .texOffs(0, 0).addBox(-4.0F, 1.0F, 2.0F, 8.0F, 9.0F, 3.0F)
                        .texOffs(0, 12).addBox(-3.0F, 5.0F, 5.0F, 6.0F, 5.0F, 2.0F)
                , PartPose.offset(0.0F, 0.0F, 0.0F));
        return LayerDefinition.create(meshdefinition, 32, 32);

    }

    @Override
    public void setupAnim(AbstractClientPlayer p_102618_, float p_102619_, float p_102620_, float p_102621_, float p_102622_, float p_102623_) {

    }

    @Override
    public void renderToBuffer(PoseStack p_103111_, VertexConsumer p_103112_, int p_103113_, int p_103114_, float p_103115_, float p_103116_, float p_103117_, float p_103118_) {

    }
}
