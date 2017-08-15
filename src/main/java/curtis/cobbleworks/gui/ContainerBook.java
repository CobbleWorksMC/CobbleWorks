package curtis.cobbleworks.gui;

import curtis.cobbleworks.manual.ItemManual;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

public class ContainerBook extends Container {
	
	ItemManual Book;

	public ContainerBook(ItemManual book) {
		
		this.Book = book;
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return true;
	}

}
