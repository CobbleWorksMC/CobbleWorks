package curtis.Cobbleworks.module.farming;

import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerHarvester extends Container {

	TileHarvester th;
	
	public ContainerHarvester(IInventory playerInv, TileHarvester teh) {
		th = teh;
		int id = 0;
		 
	    for (int x = 0; x < 9; ++x) {
	    	this.addSlotToContainer(new Slot(playerInv, id++, 34 + x * 18, 142));
	    }
		
		for (int y = 0; y < 3; ++y) {
	        for (int x = 0; x < 9; ++x) {
	            this.addSlotToContainer(new Slot(playerInv, id++, 34 + x * 18, 84 + y * 18));
	        }
	    }
	    
		id = 0;
		
	    for (int y = 0; y < 3; ++y) {
	        for (int x = 0; x < 6; ++x) {
	            this.addSlotToContainer(new Slot(th, id++, 34 + x * 18, 17 + y * 18));
	        }
	    }
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return th.canInteractWith(playerIn);
	}
	
	@Nullable
    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        ItemStack itemstack = null;
        Slot slot = this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (index < th.getSizeInventory()) {
                if (!this.mergeItemStack(itemstack1, th.getSizeInventory(), this.inventorySlots.size(), true)) {
                    return null;
                }
            } else if (!this.mergeItemStack(itemstack1, 0, th.getSizeInventory(), false)) {
                return null;
            }

            if (itemstack1.stackSize == 0) {
                slot.putStack(null);
            } else {
                slot.onSlotChanged();
            }
        }

        return itemstack;
    }

}
