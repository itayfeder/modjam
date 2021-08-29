package net.aritsu.blockentity;

import net.aritsu.registry.AritsuBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class LightAirBlockEntity extends BlockEntity {
    public LightAirBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(AritsuBlockEntities.LIGHT_AIR_BE.get(), blockPos, blockState);
    }
    public static void lightAirTick(Level level, BlockPos pos, BlockState state, LightAirBlockEntity be) {

        AABB aabb =new AABB(pos.getX()-1,pos.getY()-1,pos.getZ()-1,pos.getX()+2,pos.getY()+2,pos.getZ()+2);
        List<Player> playersList = level.getEntitiesOfClass(Player.class,aabb);
        if (playersList.isEmpty())
        {
            level.removeBlock(pos,true);
        }
    }
}
