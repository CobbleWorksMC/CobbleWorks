package curtis.cobbleworks.gui;

import javax.annotation.Nullable;

import curtis.cobbleworks.cobblegen.TileEntityCobblegen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerCobblegen extends Container {

private TileEntityCobblegen te;
	
	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return te.canInteractWith(playerIn);
	}
	
	@Nullable
    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
		return ItemStack.EMPTY;
	}
    
    /*public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (index < te.getSizeInventory()) {
                if (!this.mergeItemStack(itemstack1, te.getSizeInventory(), this.inventorySlots.size(), true)) {
                    return null;
                }
            } else if (!this.mergeItemStack(itemstack1, 0, te.getSizeInventory(), false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.getCount() == 0) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }
        }

        return itemstack;
    }*/
	
	public ContainerCobblegen(IInventory playerInv, TileEntityCobblegen tecg) {
		//No longer displays the block inventory in the GUI.
		this.te = tecg;
	    for (int y = 0; y < 3; ++y) {
	        for (int x = 0; x < 9; ++x) {
	            this.addSlotToContainer(new Slot(playerInv, x + y * 9 + 9, 34 + x * 18, 84 + y * 18));
	        }
	    }
	    
	    for (int x = 0; x < 9; ++x) {
	    	this.addSlotToContainer(new Slot(playerInv, x, 34 + x * 18, 142));
	    }
	}

}
