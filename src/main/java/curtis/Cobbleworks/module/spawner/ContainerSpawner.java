package curtis.Cobbleworks.module.spawner;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public class ContainerSpawner extends Container {
	
	private TileEntityPendulumSummoner te;
	
	@Override
	public boolean canInteractWith(EntityPlayer caster) {
		return te.isUseableByPlayer(caster);
	}
	
	@Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        ItemStack itemstack = null;
        Slot slot = this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (index < te.getSizeInventory()) {
                if (!this.mergeItemStack(itemstack1, te.getSizeInventory(), this.inventorySlots.size(), true)) {
                    return null;
                }
            } else if (!this.mergeItemStack(itemstack1, 0, te.getSizeInventory(), false)) {
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
	
	public ContainerSpawner(IInventory playerInv, TileEntityPendulumSummoner teps) {
		
		this.te = teps;
		
		this.addSlotToContainer(new Slot(teps, 0, 8, 32));
		
		for (int i = 1; i < 6; i++) {
			this.addSlotToContainer(new Slot(teps, i, 26 + 18 * i, 32));
		}
		
		this.addSlotToContainer(new Slot(teps, 6, 152, 32));
		
		for (int y = 0; y < 3; ++y) {
	        for (int x = 0; x < 9; ++x) {
	            this.addSlotToContainer(new Slot(playerInv, x + y * 9 + 9, 8 + x * 18, 84 + y * 18));
	        }
	    }

	    for (int x = 0; x < 9; ++x) {
	    		this.addSlotToContainer(new Slot(playerInv, x, 8 + x * 18, 142));
	    }
	}
}
