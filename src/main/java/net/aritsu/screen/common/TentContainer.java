package net.aritsu.screen.common;

import net.aritsu.item.FlaskItem;
import net.aritsu.item.SleepingBagItem;
import net.aritsu.item.TentItem;
import net.aritsu.registry.AritsuContainers;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.ItemStackHandler;

import java.util.Optional;

public class TentContainer extends AbstractContainerMenu {

    public static final int RESULT_SLOT = 0;
    private static final int CRAFT_SLOT_START = 1;
    private static final int CRAFT_SLOT_END = 10;
    private static final int INV_SLOT_START = 10;
    private static final int INV_SLOT_END = 37;
    private static final int USE_ROW_SLOT_START = 37;
    private static final int USE_ROW_SLOT_END = 46;
    final ItemStackHandler backpackInventory;
    private final CraftingContainer craftSlots = new CraftingContainer(this, 3, 3);
    private final ResultContainer resultSlots = new ResultContainer();
    private final ContainerLevelAccess access;
    private Player player;

    public TentContainer(int id, Inventory playerInventory, ItemStackHandler inventory) {
        this(id, playerInventory, inventory, ContainerLevelAccess.NULL);
    }

    public TentContainer(int id, Inventory playerInventory, ItemStackHandler inventory, ContainerLevelAccess access) {
        super(AritsuContainers.TENT_CONTAINER_TYPE.get(), id);
        this.access = access;
        backpackInventory = inventory;
        this.player = playerInventory.player;

        this.addSlot(new ResultSlot(player, this.craftSlots, this.resultSlots, 0, 124, 35));

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                this.addSlot(new Slot(this.craftSlots, j + i * 3, 30 + j * 18, 17 + i * 18));
            }
        }

        for (int k = 0; k < 3; ++k) {
            for (int i1 = 0; i1 < 9; ++i1) {
                this.addSlot(new Slot(playerInventory, i1 + k * 9 + 9, 8 + i1 * 18, 84 + k * 18));
            }
        }

        for (int l = 0; l < 9; ++l) {
            this.addSlot(new Slot(playerInventory, l, 8 + l * 18, 142));
        }
    }

    /********************** FACTORIES **********************/
    public static TentContainer registerClientContainer(int id, Inventory playerinventory) {
        return new TentContainer(id, playerinventory, new ItemStackHandler(6));
    }

    /**
     * Get the server container provider for NetworkHooks.openGui
     */
    public static MenuConstructor getServerContainerProvider(ItemStackHandler stackHandler) {

        return (id, playerInventory, serverPlayer) -> new TentContainer(id, playerInventory, stackHandler);
    }

    protected static void slotChangedCraftingGrid(AbstractContainerMenu p_150547_, Level p_150548_, Player p_150549_, CraftingContainer p_150550_, ResultContainer p_150551_) {
        if (!p_150548_.isClientSide) {
            ServerPlayer serverplayer = (ServerPlayer) p_150549_;
            ItemStack itemstack = ItemStack.EMPTY;
            Optional<CraftingRecipe> optional = p_150548_.getServer().getRecipeManager().getRecipeFor(RecipeType.CRAFTING, p_150550_, p_150548_);
            if (optional.isPresent()) {
                CraftingRecipe craftingrecipe = optional.get();
                if (p_150551_.setRecipeUsed(p_150548_, serverplayer, craftingrecipe)) {
                    itemstack = craftingrecipe.assemble(p_150550_);
                }
            }

            p_150551_.setItem(0, itemstack);
            p_150547_.setRemoteSlot(0, itemstack);
            serverplayer.connection.send(new ClientboundContainerSetSlotPacket(p_150547_.containerId, p_150547_.incrementStateId(), 0, itemstack));
        }
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int slotnumber) {
        ItemStack copy = ItemStack.EMPTY;
        Slot clicked = this.getSlot(slotnumber);
        if (clicked != null && clicked.hasItem()) {
            ItemStack slotItem = clicked.getItem();
            copy = slotItem.copy();

            //shift clicked inside backpack
            if (slotnumber < backpackInventory.getSlots()) {
                //move to player inventory
                if (!this.moveItemStackTo(slotItem, backpackInventory.getSlots(), this.slots.size() - 9, false)) {
                    return ItemStack.EMPTY;
                } else if (!this.moveItemStackTo(slotItem, this.slots.size() - 9, this.slots.size(), false)) {
                    return ItemStack.EMPTY;
                }
            } else {
                //check for specific item
                if (slotItem.getItem() instanceof FlaskItem) {
                    //move to specific item slot
                    if (!this.moveItemStackTo(slotItem, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (slotItem.getItem().equals(Items.ENDER_CHEST)) {
                    if (!this.moveItemStackTo(slotItem, 1, 2, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (slotItem.getItem() instanceof SleepingBagItem) {
                    if (!this.moveItemStackTo(slotItem, 2, 3, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (slotItem.getItem() instanceof TentItem) {
                    if (!this.moveItemStackTo(slotItem, 3, 4, false)) {
                        return ItemStack.EMPTY;
                    }
                }

                //move to extra slots
                else if (!this.moveItemStackTo(slotItem, 4, backpackInventory.getSlots(), false)) {
                    return ItemStack.EMPTY;
                }
            }
            if (slotItem.isEmpty())
                clicked.set(ItemStack.EMPTY);
            else clicked.setChanged();
        }
        return copy;
    }

    public void slotsChanged(Container container) {
        this.access.execute((containerLambda, lambda) -> {
            slotChangedCraftingGrid(this, containerLambda, this.player, this.craftSlots, this.resultSlots);
        });
    }

    public void fillCraftSlotsStackedContents(StackedContents p_39374_) {
        this.craftSlots.fillStackedContents(p_39374_);
    }

    public void clearCraftingContent() {
        this.craftSlots.clearContent();
        this.resultSlots.clearContent();
    }

    public boolean recipeMatches(Recipe<? super CraftingContainer> p_39384_) {
        return p_39384_.matches(this.craftSlots, this.player.level);
    }

    public void removed(Player player) {
        super.removed(player);
        this.access.execute((p_39371_, p_39372_) -> {
            this.clearContainer(player, this.craftSlots);
        });
    }
}