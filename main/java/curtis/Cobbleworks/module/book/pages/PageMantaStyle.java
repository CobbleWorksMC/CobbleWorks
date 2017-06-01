package curtis.Cobbleworks.module.book.pages;

import curtis.Cobbleworks.CommonProxy;
import curtis.Cobbleworks.module.book.BookPage;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class PageMantaStyle extends BookPage {
	
	private static final int[] pageTypes = new int[] {0, 1};
	
	public PageMantaStyle() {
		super(1, pageTypes);
	}
	
	public String getButtonName() {
		return "Manta Style";
	}
	
	public String getDisplayText(int minor) {
		String result = "";
		
		switch (minor) {
		case 0: { result = "This strage axe has many unique properties. Firstly, it can cut down very large trees with a single strike, and has a lot of durability. Also, when you activate it, it spawns two entities to fight for you, and clears all potion effects off you. They should look familiar." + '\n' + "45 second cooldown."; break; }
		case 1: { result = "Costing three nether stars, you can really only get this when you have finished farming."; break; }
		}
		
		return result;
	}
	
	public String getTooltipText(int minor) {
		switch (minor) {
		case 0: return "";
		case 1: return "I'VE GOT MANTA STYLE";
		}
		return "";
	}
	
	public ItemStack[] getRecipeRender(int minor) {
		if (minor == 1) {
			return new ItemStack[] {new ItemStack(Items.NETHER_STAR), new ItemStack(Items.NETHER_STAR), new ItemStack(Blocks.AIR), new ItemStack(Items.NETHER_STAR), new ItemStack(Items.DIAMOND_AXE), new ItemStack(Blocks.AIR), new ItemStack(Blocks.AIR), new ItemStack(Items.GOLDEN_SWORD), new ItemStack(Blocks.AIR), new ItemStack(CommonProxy.manta)};
		}
		return null;
	}
}
