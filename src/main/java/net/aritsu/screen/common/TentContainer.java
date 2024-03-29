package net.aritsu.screen.common;

import net.aritsu.item.SleepingBagItem;
import net.aritsu.registry.AritsuContainers;
import net.aritsu.screen.slots.LanternSlot;
import net.aritsu.screen.slots.SleepingBagSlot;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

import java.util.Optional;

public class TentContainer extends AbstractContainerMenu {

    private static final int TENTSIZE = 6;
    private static final int CRAFTSIZE = 10;
    private static final int INVENTORYSIZE = 27;
    private static final int HOTBAR = 9;
    private static final int TOTAL = TENTSIZE + INVENTORYSIZE + HOTBAR + CRAFTSIZE;
    private static final int CRAFTSTART = TOTAL - CRAFTSIZE;
    private static final int HOTBARSTART = CRAFTSTART - HOTBAR;
    private static final int INVENTORYSTART = HOTBARSTART - INVENTORYSIZE;

    final ItemStackHandler tentInventory;
    private final CraftingContainer craftSlots = new CraftingContainer(this, 3, 3);
    private final ResultContainer resultSlots = new ResultContainer();
    private final ContainerLevelAccess access;
    private final Player player;

    public TentContainer(int id, Inventory playerInventory, ItemStackHandler inventory) {
        this(id, playerInventory, inventory, ContainerLevelAccess.NULL);
    }

    public TentContainer(int id, Inventory playerInventory, ItemStackHandler tentInventory, ContainerLevelAccess access) {
        super(AritsuContainers.TENT_CONTAINER_TYPE.get(), id);
        this.access = access;
        this.tentInventory = tentInventory;
        this.player = playerInventory.player;

        this.addSlot(new SleepingBagSlot(tentInventory, 0, 30, 81));
        this.addSlot(new LanternSlot(tentInventory, 1, 30 + 18, 81));
        for (int x = 0; x < 4; x++)
            this.addSlot(new SlotItemHandler(tentInventory, x + 2, 84 + 18 * x, 81));

        for (int y = 0; y < 3; ++y) {
            for (int x = 0; x < 9; ++x) {
                this.addSlot(new Slot(playerInventory, x + y * 9 + 9, 8 + x * 18, (84 + 27) + y * 18));
            }
        }

        for (int x = 0; x < 9; ++x) {
            this.addSlot(new Slot(playerInventory, x, 8 + x * 18, 142 + 27));
        }

        this.addSlot(new ResultSlot(player, this.craftSlots, this.resultSlots, 0, 124, 35));

        for (int y = 0; y < 3; ++y) {
            for (int x = 0; x < 3; ++x) {
                this.addSlot(new Slot(this.craftSlots, x + y * 3, 30 + x * 18, 17 + y * 18));
            }
        }
    }

    /********************** FACTORIES **********************/
    public static TentContainer registerClientContainer(int id, Inventory playerinventory) {
        return new TentContainer(id, playerinventory, new ItemStackHandler(6));
    }

    /**
     * Get the server container provider for NetworkHooks.openGui
     */
    public static MenuConstructor getServerContainerProvider(ItemStackHandler stackHandler, ContainerLevelAccess access) {

        return (id, playerInventory, serverPlayer) -> new TentContainer(id, playerInventory, stackHandler, access);
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

            if (slotnumber == CRAFTSTART) {
                this.access.execute((p_39378_, p_39379_) -> {
                    slotItem.getItem().onCraftedBy(slotItem, p_39378_, player);
                });
                if (!this.moveItemStackTo(slotItem, TENTSIZE, CRAFTSTART, true)) {
                    return ItemStack.EMPTY;
                }

                clicked.onQuickCraft(slotItem, copy);
            }
            //shift clicked inside tent
            else if (slotnumber < TENTSIZE) {
                //move to player inventory
                if (!this.moveItemStackTo(slotItem, HOTBARSTART, CRAFTSTART, false)) {
                    if (!this.moveItemStackTo(slotItem, TENTSIZE, HOTBARSTART, false)) {
                        return ItemStack.EMPTY;
                    }
                    return ItemStack.EMPTY;
                }
            } else {
                //check for specific item
                if (slotItem.getItem() instanceof SleepingBagItem) {
                    //move to specific item slot
                    if (!this.moveItemStackTo(slotItem, 0, 1, false)) {
                        return switchBetweenPlayerInv(slotnumber, slotItem);
                    }
                } else if (slotItem.getItem().equals(Items.LANTERN)) {
                    if (!this.moveItemStackTo(slotItem, 1, 2, false)) {
                        return switchBetweenPlayerInv(slotnumber, slotItem);
                    }
                }
                //move to extra slots
                else if (!this.moveItemStackTo(slotItem, 2, tentInventory.getSlots(), false)) {
                    return switchBetweenPlayerInv(slotnumber, slotItem);
                }
            }
            if (slotItem.isEmpty())
                clicked.set(ItemStack.EMPTY);
            else clicked.setChanged();

            if (slotItem.getCount() == copy.getCount()) {
                return ItemStack.EMPTY;
            }

            clicked.onTake(player, slotItem);
            if (slotnumber == CRAFTSTART) {
                player.drop(slotItem, false);
            }
        }
        return copy;
    }

    private ItemStack switchBetweenPlayerInv(int slotnumber, ItemStack slotItem) {
        if (slotnumber < HOTBARSTART) {
            if (!this.moveItemStackTo(slotItem, HOTBARSTART, CRAFTSTART, false)) {
                return ItemStack.EMPTY;
            }
        } else if (slotnumber < CRAFTSTART) {
            if (!this.moveItemStackTo(slotItem, TENTSIZE, HOTBARSTART, false)) {
                return ItemStack.EMPTY;
            }
        }
        return ItemStack.EMPTY;
    }

    @Override
    public void slotsChanged(Container container) {
        this.access.execute((containerLambda, lambda) -> {
            slotChangedCraftingGrid(this, containerLambda, this.player, this.craftSlots, this.resultSlots);
        });
    }

    public void removed(Player player) {
        super.removed(player);
        this.access.execute((p_39371_, p_39372_) -> {
            this.clearContainer(player, this.craftSlots);
        });
    }
}