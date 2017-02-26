package curtis.Cobbleworks.module.book.pages;

import curtis.Cobbleworks.CommonProxy;
import curtis.Cobbleworks.module.book.BookPage;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class PageLightWand extends BookPage {
	
	private static final int[] pageTypes = new int[] {0, 1};
	
	public PageLightWand() {
		super(1, pageTypes);
	}
	
	public String getButtonName() {
		return "Light Wand";
	}
	
	public String getDisplayText(int minor) {
		String result = "";
		
		switch (minor) {
		case 0: { result = "Use this wand on a solid surface to place a small invisible light. While you hold the wand, all invisible lights will emit flame particles. Punch them to break them."; break; }
		case 1: { result = "InvisibLights? No idea what you're talking about."; break; }
		}
		
		return result;
	}
	
	public String getTooltipText(int minor) {
		return "Ooooh, shiny...";
	}
	
	public ItemStack[] getRecipeRender(int minor) {
		if (minor == 1) {
			return new ItemStack[] {new ItemStack(Items.GLOWSTONE_DUST), new ItemStack(Blocks.GLOWSTONE), new ItemStack(Items.GLOWSTONE_DUST), new ItemStack(Items.GLOWSTONE_DUST), new ItemStack(Items.BLAZE_ROD), new ItemStack(Items.GLOWSTONE_DUST), new ItemStack(Items.GLOWSTONE_DUST), new ItemStack(Items.BLAZE_ROD), new ItemStack(Items.GLOWSTONE_DUST), new ItemStack(CommonProxy.lightWand)};
		}
		return null;
	}
}
