package curtis.Cobbleworks.module.book.pages;

import curtis.Cobbleworks.CommonProxy;
import curtis.Cobbleworks.module.book.BookPage;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class PageHarvester extends BookPage {

	public PageHarvester() {
		super(6, new int[] {0, 1, 0, 0, 0, 0, 0});
	}
	
	public String getButtonName() {
		return "Harvester";
	}
	
	public String getDisplayText(int minor) {
		String result = "";
		
		switch (minor) {
		case 0: { result = "The Harvester block iterates through all blocks within its radius on the same Y-level as it, and attempts to harvest them if they are plants. It has many different modes, all of which operate independently. There is an internal item storage of 18 slots for harvested and picked up items."; break; }
		case 1: { result = "Why mine ores when there are plants that make stuff for you?"; break; }
		case 2: { result = "In the block GUI, there are several buttons. The vanilla plant mode enables the block to harvest things like wheat, carrots, and potatoes. Certain mod-added plants will act like vanilla plants, and be harvested with this method."; break; }
		case 3: { result = "Agricraft mode specifically enables harvesting of agricraft plants. It is different enough that it became its own mode. Tall plant mode enables harvesting of sugarcane, cactus, and any mod-added two block tall or taller plant, like Immersive Engineering Hemp."; break; }
		case 4: { result = "Tree mode will attempt to cut down trees. It does not replant the saplings, however (may be added in the future). The + and - buttons increase and decrease the radius, with a minimum of 1 and maximum of 9 blocks. "; break; }
		case 5: { result = "Item collection mode lets the block pick up all stray items within an area slightly larger than the area checked for plants, 3 blocks tall. The collection occurs when the block cycles back to the start of the harvest area. "; break; }
		case 6: { result = "Item voiding mode is rather dangerous, and disabled by default. If enabled, it will destroy any items it attempts to pick up that do not fit in the block\'s 18-slot inventory. Use at your own risk."; break; }
		}
		
		return result;
	}
	
	public String getTooltipText(int minor) {
		
		if (minor == 1) {
			return "Farm all the things!";
		}
		
		return "";
	}
	
	public ItemStack[] getRecipeRender(int minor) {
		
		if (minor == 1) {
			return new ItemStack[] {new ItemStack(Items.GOLDEN_AXE), new ItemStack(Items.DIAMOND), new ItemStack(Items.GOLDEN_HOE),
									new ItemStack(Blocks.HOPPER), new ItemStack(Blocks.QUARTZ_BLOCK), new ItemStack(Blocks.REDSTONE_LAMP),
									new ItemStack(Items.GOLD_INGOT), new ItemStack(Blocks.CHEST), new ItemStack(Items.GOLD_INGOT),
									new ItemStack(CommonProxy.harvester)};
		}
		
		return null;
	}
	
}
