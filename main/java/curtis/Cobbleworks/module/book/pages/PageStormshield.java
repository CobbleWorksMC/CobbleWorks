package curtis.Cobbleworks.module.book.pages;

import curtis.Cobbleworks.CommonProxy;
import curtis.Cobbleworks.module.book.BookPage;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class PageStormshield extends BookPage {

	public PageStormshield() {
		super(1, new int[] {0, 1});
	}
	
	public String getButtonName() {
		return "Stormshield";
	}
	
	public String getDisplayText(int minor) {
		
		String result = "";
		
		switch (minor) {
		case 0: { result = "Stormshield is a diabolical shield that protects the user like a second layer of armor. While you cannot block with it like a normal shield, it offers the chance to block automatically, and if it does not it reduces the damage you do take, by a large fraction."; break; }
		case 1: { result = "It is also indestructible, so you can just keep it in your shield slot forever."; break; }
		}
		
		return result;
	}
	
	public String getTooltipText(int minor) {
		return "";
	}
	
	public ItemStack[] getRecipeRender(int minor) {
		return new ItemStack[] {new ItemStack(Items.GOLD_INGOT), new ItemStack(Items.SHIELD), new ItemStack(Items.GOLD_INGOT),
								new ItemStack(Blocks.OBSIDIAN), new ItemStack(Items.DIAMOND), new ItemStack(Blocks.OBSIDIAN),
								new ItemStack(Blocks.OBSIDIAN), new ItemStack(Blocks.OBSIDIAN), new ItemStack(Blocks.OBSIDIAN),
								new ItemStack(CommonProxy.stormShield),};
	}
}
