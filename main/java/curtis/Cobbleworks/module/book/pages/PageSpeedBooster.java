package curtis.Cobbleworks.module.book.pages;

import curtis.Cobbleworks.CommonProxy;
import curtis.Cobbleworks.Config;
import curtis.Cobbleworks.module.book.BookPage;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class PageSpeedBooster extends BookPage {
	
	public PageSpeedBooster() {
		super(2, new int[] {0, 0, 1});
	}
	
	@Override
	public String getButtonName() {
		return "Speed Booster";
	}
	
	@Override
	public String getDisplayText(int minor) {
		String result = "";
		
		switch (minor){
		case 0: result = "The Speed Boosters are a nice pair of boots that feature two abilities. The first is that the longer you sprint, the faster your sprint becomes, to a point. Every 10 ticks, or half second, of sprinting increases your speed by 15 percent, up to 150 percent. If you stop, your speed boost rapidly diminishes."; break;
		case 1: result = "The second is a little more complicated. Sprint for at least a few seconds, then start sneaking. Quickly jump while sneaking to launch yourself in the direction you are looking. Be careful, this costs 70 percent of your current health! You should take no fall damage from this jump, also known as a shinespark."; break;
		case 2: {
				if (Config.animals.equals("kill")) {
					result = "Save the frames, kill the animals";
				} else {result = "Save the animals, forget the frames";
				} break;
			}
		}
		
		return result;
	}
	
	public String getTooltipText(int minor) {
		
		if (minor == 2) {
			return "Take the fight to Ridley.";
		}
		
		return "";
	}
	
	public ItemStack[] getRecipeRender(int minor) {
		return new ItemStack[] {new ItemStack(Blocks.OBSIDIAN), new ItemStack(Blocks.WOOL), new ItemStack(Blocks.OBSIDIAN),
								new ItemStack(Items.FIREWORKS), new ItemStack(Items.DIAMOND_BOOTS), new ItemStack(Items.FIREWORKS),
								new ItemStack(Items.FIREWORKS), new ItemStack(Blocks.SLIME_BLOCK), new ItemStack(Items.FIREWORKS),
								new ItemStack(CommonProxy.speedBoosters)};
	}
}
