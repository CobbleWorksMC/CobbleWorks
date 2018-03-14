package curtis.cobbleworks.manual;

import curtis.cobbleworks.CommonProxy;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class PageBasicCobbleworks extends BookPage {
	
	//Determines which pages are generic and which are crafting.
	//0 Gives the generic book page, 1 gives the crafting page for the minorPage at that index
	//
	private static final int[] pageTypes = new int[] {0, 1, 0, 0, 0};
	
	public PageBasicCobbleworks() {
		super(4, pageTypes);
	}
	
	@Override
	public String getButtonName() {
		return "Cobbleworks";
	}
	
	@Override
	public String getDisplayText(int minor) {
		
		String result = "";
		
		switch (minor) {
		case 0: { result = "The flagship block of the mod, the Cobbleworks generates basic materials using RF. Use upgrades on it to make it even better!"; break; }
		case 1: { result = "The water and lava buckets can be swapped"; break; }
		case 2: { result = "Blocks produced, by tier:" + '\n' 
		+ "0+ : Cobblestone" + '\n'
		+ "1+ : Stone, Sand, Glass" + '\n'
		+ "2+ : Gravel, Flint, Stone Bricks" + '\n'
		+ "3+ : Sandstone, Dirt"; break; }
		case 3: { result = "Maximum power consumed, in RF/t, by tier:" + '\n'
		+ "0 : 0" + '\n'
		+ "1 : 250"+ '\n'
		+ "2 : 500" + '\n'
		+ "3 : 1000" + '\n'
		+ "4 : 1500" + '\n'
		+ "5 : 2000" + '\n'
		+ "Uses less rf when producing less blocks / items than the maximum at that tier"; break; }
		case 4: { result = "Other information: "+ '\n'
		+ "Emits light, because why not." + '\n'
		+ "Generates up to [9/36/72/144/288/576] total items per cycle for tiers 0 to 5, respectively, at up to 256 of any one item at a time." + '\n'
		+ "In the GUI, hold ctrl or shift to change the amount by 16 or 64, respectively."; break; }
		default: {result = "ERROR"; break; }
		}
		return result;
	}
	
	@Override
	public String getTooltipText(int minor) {
		return "The block that started it all";
	}
	
	public ItemStack[] getRecipeRender(int minor) {
		
		if (minor == 1) {
			return new ItemStack[] {
				new ItemStack(Items.IRON_INGOT), new ItemStack(Items.IRON_INGOT), new ItemStack(Items.IRON_INGOT),
				new ItemStack(Items.LAVA_BUCKET), new ItemStack(Items.DIAMOND_PICKAXE), new ItemStack(Items.WATER_BUCKET),
				new ItemStack(Items.IRON_INGOT), new ItemStack(Items.IRON_INGOT), new ItemStack(Items.IRON_INGOT),
				new ItemStack(CommonProxy.cobblegen)
			};
		}
		
		return null;
	}
}

