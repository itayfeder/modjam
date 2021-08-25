package net.aritsu.util;

import net.aritsu.block.TentBlock;
import net.aritsu.blockentity.TentBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BedPart;

import javax.annotation.Nonnull;
import java.util.Objects;

public class TentUtils {

    @Nonnull
    public static BlockState getTentNeighbourState(BlockPos pos, Level level) {
        return level.getBlockState(Objects.requireNonNull(getTentNeighbourPos(pos, level)));
    }

    @Nonnull
    public static BlockPos getTentNeighbourPos(BlockPos currentPos, Level level) {

        BlockState current = level.getBlockState(currentPos);
        BlockPos neighbour = BlockPos.ZERO;

        if (!(level.getBlockState(currentPos).getBlock() instanceof TentBlock))
            return BlockPos.ZERO;

        if (current.getValue(TentBlock.PART) == BedPart.FOOT) {
            neighbour = switch (current.getValue(TentBlock.FACING)) {
                case NORTH -> currentPos.north();
                case SOUTH -> currentPos.south();
                case EAST -> currentPos.east();
                case WEST -> currentPos.west();
                default -> null;
            };
        } else if (current.getValue(TentBlock.PART) == BedPart.HEAD) {
            neighbour = switch (current.getValue(TentBlock.FACING)) {
                case NORTH -> currentPos.south();
                case SOUTH -> currentPos.north();
                case EAST -> currentPos.west();
                case WEST -> currentPos.east();
                default -> null;
            };
        }
        if (neighbour != null && level.getBlockState(neighbour).getBlock() instanceof TentBlock)
            return neighbour;
        return BlockPos.ZERO;
    }

    public static boolean isTentEmpty(BlockPos current, Level level) {
        BlockPos neighbour = getTentNeighbourPos(current, level);
        if (level.getBlockEntity(neighbour) instanceof TentBlockEntity secondTent && level.getBlockEntity(current) instanceof TentBlockEntity curentTent) {
            return secondTent.getInventory().getStackInSlot(0).isEmpty() && curentTent.getInventory().getStackInSlot(0).isEmpty();
        }
        return false;
    }
}
