package curtis.cobbleworks.manual;

import curtis.cobbleworks.CommonProxy;
import curtis.cobbleworks.Config;
import curtis.cobbleworks.cobblegen.TileEntityCustomgen;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class PageCustomCobblegen extends BookPage {
	
	public static final ItemStack[] recipe = new ItemStack[] {new ItemStack(Items.GOLD_INGOT), new ItemStack(CommonProxy.cobblegen), new ItemStack(Items.GOLD_INGOT), 
															  new ItemStack(Items.IRON_INGOT), new ItemStack(Items.IRON_INGOT), new ItemStack(Items.IRON_INGOT),
															  new ItemStack(Items.GOLD_INGOT), new ItemStack(CommonProxy.advgen), new ItemStack(Items.GOLD_INGOT),
															  new ItemStack(CommonProxy.customgen)};

	public PageCustomCobblegen() {
		super(4, new int[] {0, 1, 0, 0, 0});
	}
	
	public String getButtonName() {
		return "Custom Cobbleworks";
	}
	
	public String getDisplayText(int minor) {
		
		String result = "";
		
		switch (minor) {
		case 0: { result = "The customizable flagship block of the mod, the Custom Cobbleworks generates anything, using RF and lava. It is fully customizable via its own config file. Use upgrades on it to make it even better! Intended for use in \"balanced\" modpacks."; break; }
		case 1: { result = "The ultimate all in one producer"; break; }
		case 2: { result = "Power consumed, in RF/t, by tier:" + '\n'
		+ "0 : " + Config.customPower[0] + '\n'
		+ "1 : " + Config.customPower[1] + '\n'
		+ "2 : " + Config.customPower[2] + '\n'
		+ "3 : " + Config.customPower[3] + '\n'
		+ "4 : " + Config.customPower[4] + '\n'
		+ "5 : " + Config.customPower[5] + '\n'
		+ "Uses less rf when producing less blocks / items than the maximum at that tier"; break; }
		case 3: { result = "Lava consumed in mB/t, per item:" + '\n' 
		+ TileEntityCustomgen.generated[0].getDisplayName() + ": " + Config.customLava[0] + '\n' 
		+ TileEntityCustomgen.generated[1].getDisplayName() + ": " + Config.customLava[1] + '\n'
		+ TileEntityCustomgen.generated[2].getDisplayName() + ": " + Config.customLava[2] + '\n'
		+ TileEntityCustomgen.generated[3].getDisplayName() + ": " + Config.customLava[3] + '\n'
		+ TileEntityCustomgen.generated[4].getDisplayName() + ": " + Config.customLava[4] + '\n'
		+ TileEntityCustomgen.generated[5].getDisplayName() + ": " + Config.customLava[5] + '\n'
		+ TileEntityCustomgen.generated[6].getDisplayName() + ": " + Config.customLava[6] + '\n'
		+ TileEntityCustomgen.generated[7].getDisplayName() + ": " + Config.customLava[7] + '\n'
		+ TileEntityCustomgen.generated[8].getDisplayName() + ": " + Config.customLava[8]; break; }
		case 4: { result = "Other information: "+ '\n'
		+ "Emits light, because why not." + '\n'
		+ "Generates up to [9/18/36/72/144/288] total items per cycle for tiers 0 to 5, respectively, at up to 128 of any one item at a time." + '\n'
		+ "In the GUI, hold ctrl or shift to change the amount by 16 or 64, respectively."; break; }
		default: {result = "ERROR"; break; }
		}
		return result;
	}
	
	public String getTooltipText(int minor) {
		String result = "";
		
		
		
		return result;
	}
	
	public ItemStack[] getRecipeRender(int minor) {
		return recipe;
	}
}
