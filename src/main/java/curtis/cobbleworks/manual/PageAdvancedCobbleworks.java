package curtis.cobbleworks.manual;

import curtis.cobbleworks.CommonProxy;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class PageAdvancedCobbleworks extends BookPage {
	
	private static final int[] pageTypes = new int[] {0, 1, 0, 0, 0, 0};
	
	public PageAdvancedCobbleworks() {
		super(5, pageTypes);
	}
	
	@Override
	public String getButtonName() {
		return "Advanced Cobbleworks";
	}
	
	@Override
	public String getDisplayText(int minor) {
		
		String result = "";
		
		switch (minor) {
		case 0: { result = "The upgraded flagship block of the mod, the Advanced Cobbleworks generates harder to get materials, using RF and lava. Use upgrades on it to make it even better!"; break; }
		case 1: { result = "The ultimate all in one producer"; break; }
		case 2: { result = "Blocks produced, by tier:" + '\n' 
		+ "0+ : Mossy Cobblestone" + '\n'
		+ "1+ : Netherrack, Soul sand, ice" + '\n'
		+ "2+ : End stone, Clay, Snow" + '\n'
		+ "3+ : Prismarine, Obsidian"; break; }
		case 3: { result = "Power consumed, in RF/t, by tier:" + '\n'
		+ "0 : 0" + '\n'
		+ "1 : 500"+ '\n'
		+ "2 : 1000" + '\n'
		+ "3 : 2000" + '\n'
		+ "4 : 3000" + '\n'
		+ "5 : 4000" + '\n'
		+ "Uses less rf when producing less blocks / items than the maximum at that tier"; break; }
		case 4: { result = "Lava consumed in mB/t, per item:" + '\n' 
		+ "Mossy cobble: 0" + '\n' 
		+ "Netherrack: 1" + '\n'
		+ "Soul sand: .5" + '\n'
		+ "Ice: .1" + '\n'
		+ "End stone: .1" + '\n'
		+ "Clay: 1" + '\n'
		+ "Snow: .1" + '\n'
		+ "Prismarine: .1" + '\n'
		+ "Obisdian: 10"; break; }
		case 5: { result = "Other information: "+ '\n'
		+ "Emits light, because why not." + '\n'
		+ "Generates up to [9/18/36/72/144/288] total items per cycle for tiers 0 to 5, respectively, at up to 128 of any one item at a time." + '\n'
		+ "In the GUI, hold ctrl or shift to change the amount by 16 or 64, respectively."; break; }
		default: {result = "ERROR"; break; }
		}
		return result;
	}
	
	@Override
	public String getTooltipText(int minor) {
		return "Wow, that's a low price!";
	}
	
	public ItemStack[] getRecipeRender(int minor) {
		
		if (minor == 1) {
			return new ItemStack[] {
				new ItemStack(Items.GOLD_INGOT), new ItemStack(Items.NETHER_STAR), new ItemStack(Items.GOLD_INGOT),
				new ItemStack(Items.WATER_BUCKET), new ItemStack(Items.DIAMOND_PICKAXE), new ItemStack(Items.WATER_BUCKET),
				new ItemStack(Items.GOLD_INGOT), new ItemStack(Blocks.QUARTZ_BLOCK), new ItemStack(Items.GOLD_INGOT),
				new ItemStack(CommonProxy.advgen)
			};
		}
		
		return null;
	}
}
