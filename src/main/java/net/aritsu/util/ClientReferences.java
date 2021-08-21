package net.aritsu.util;

import net.aritsu.events.modbus.AritsuModelRegistrationEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;

public class ClientReferences {

    private static HumanoidModel<Player> innerModel;
    private static HumanoidModel<Player> outerModel;

    public static HumanoidModel<?> getArmorModel(EquipmentSlot armorSlot) {
        EntityModelSet modelSets = Minecraft.getInstance().getEntityModels();

        switch (armorSlot)
        {
            case HEAD -> {
                return new PlayerModel<Player>(modelSets.bakeLayer(AritsuModelRegistrationEvent.HEAD_MODEL_LOCATION),false);
            }
            case CHEST, FEET -> {
                return new PlayerModel<Player>(modelSets.bakeLayer(AritsuModelRegistrationEvent.CHESTNBOOTS_MODEL_LOCATION),false);
            }
            case LEGS -> {
                return new PlayerModel<Player>(modelSets.bakeLayer(AritsuModelRegistrationEvent.LEGS_MODEL_LOCATION),false);
            }
        }
        return null; //null will default armor models to MC armor models
    }
}
