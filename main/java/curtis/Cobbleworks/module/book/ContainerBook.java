package curtis.Cobbleworks.module.book;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;

public class ContainerBook extends Container {
	
	ItemBook Book;

	public ContainerBook(ItemBook book) {
		
		this.Book = book;
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return true;
	}

}
