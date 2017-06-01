package curtis.Cobbleworks.module.book.pages;

import curtis.Cobbleworks.CommonProxy;
import curtis.Cobbleworks.module.book.BookPage;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class PageHydroponicSoil extends BookPage {

	public PageHydroponicSoil() {
		super(2, new int[] {0, 1, 0});
	}
	
	public String getButtonName() {
		return "Hydroponic Soil";
	}
	
	public String getDisplayText(int minor) {
		String result = "";
		
		switch (minor) {
		case 0: { result = "The Hydroponic Soil is basically farmland that will never break if something falls on it, is always watered, and grows plants a little faster. You can use it to grow almost anything, the only notable (vanilla) exceptions are whole-blocks, like melon and pumpkin bocks (the stems will work, however)."; break; }
		case 1: { result = "The water bottle can also be a bucket. \nThe recipe outputs 8 Hydroponic Soil blocks at a time."; break; }
		case 2: { result = "For plants that do not require a direct view of the sky, you can use this block in conjuction with the Harvester block to make large vertical farms that do not take up a ton of land space. The idea is to build up, not out, so you can have an entire field of farmland in a 9x9 tower."; break; }
		}
		//TODO: Fix manual page displaying stack quantities
		return result;
	}
	
	public String getTooltipText(int minor) {
		
		if (minor == 1) {
			return "Farm all the things!";
		}
		
		return "";
	}
	
	public ItemStack[] getRecipeRender(int minor) {
		
		ItemStack stack = new ItemStack(Blocks.DIRT);
		
		if (minor == 1) {
			return new ItemStack[] {stack, stack, stack, 
									stack, new ItemStack(Items.POTIONITEM), stack, 
									stack, stack, stack,
									new ItemStack(CommonProxy.farmland, 8)};
		}
		
		return null;
	}
}
