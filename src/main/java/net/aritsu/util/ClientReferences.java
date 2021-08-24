package net.aritsu.util;

import net.aritsu.client.renderer.models.HikerArmorModel;
import net.aritsu.registry.AritsuModels;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class ClientReferences {

    private static HikerArmorModel innerModel;
    private static HikerArmorModel outerModel;

    public static HumanoidModel<?> getArmorModel(EquipmentSlot armorSlot) {
        EntityModelSet modelSets = Minecraft.getInstance().getEntityModels();
        HikerArmorModel model = null;
        ModelPart part = null;

        switch (armorSlot) {
            case HEAD -> {
                part = modelSets.bakeLayer(AritsuModels.HEAD_MODEL_LOCATION);
            }
            case CHEST, FEET -> {
                part = modelSets.bakeLayer(AritsuModels.CHESTNBOOTS_MODEL_LOCATION);
            }
            case LEGS -> {
                part = modelSets.bakeLayer(AritsuModels.LEGS_MODEL_LOCATION);
            }
        }
        if (part != null) {
            model = new HikerArmorModel(part, false);
            model.setAllVisible(true);

            //set player model extras to visible. humanoid parts get set to visible by MC
            switch (armorSlot) {
                case HEAD -> model.head.visible = model.hat.visible = true;
                case CHEST -> model.jacket.visible = model.rightSleeve.visible = model.leftSleeve.visible = true;
                case LEGS -> model.leftPants.visible = model.rightPants.visible = model.jacket.visible = true;
                case FEET -> model.leftPants.visible = model.rightPants.visible = true;
            }

        }
        return model; //null will default armor models to MC armor models
    }

    public static Level getClientLevel() {
        return getClientPlayer().level;
    }

    public static Player getClientPlayer() {
        return Minecraft.getInstance().player;
    }
}
