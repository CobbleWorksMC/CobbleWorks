package curtis.Cobbleworks.module.tool.gui;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ContainerFurnace;
import net.minecraft.inventory.IInventory;

public class ContainerPocketFurnace extends ContainerFurnace {

	public ContainerPocketFurnace(InventoryPlayer playerInventory, IInventory furnaceInventory) {
		super(playerInventory, furnaceInventory);
	}

}
