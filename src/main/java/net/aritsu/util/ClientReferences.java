package net.aritsu.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.world.entity.player.Player;

public class ClientReferences {

    private static HumanoidModel<Player> innerModel;
    private static HumanoidModel<Player> outerModel;

    public static HumanoidModel<?> getArmorModel(boolean isInner) {
        EntityModelSet modelSets = Minecraft.getInstance().getEntityModels();
        if (innerModel == null)
            innerModel = new HumanoidModel<>(modelSets.bakeLayer(ModelLayers.PLAYER_INNER_ARMOR));
        if (outerModel == null)
            outerModel = new HumanoidModel<>(modelSets.bakeLayer(ModelLayers.PLAYER_OUTER_ARMOR));

        return isInner ? innerModel : outerModel;
    }
}
