package curtis.Cobbleworks.module.book.pages;

import curtis.Cobbleworks.module.book.BookPage;
import net.minecraft.item.ItemStack;

public class PageOverview extends BookPage {

	public PageOverview() {
		super(2, new int[] {0, 0, 0});
	}
	
	public String getButtonName() {
		return "Mod Overview";
	}
	
	public String getDisplayText(int minor) {
		String result = "";
		
		switch (minor) {
		case 0: { result = "Hello! You have likely installed this mod because you, like the developers, are tired of gathering basic materials. Well while this mod adds in features that make life easier, like the titular cobblestone generating device (AKA the Cobbleworks), it also adds in lots of other stuff."; break; }
		case 1: { result = "But you can ignore any of the content you do not want. The configuration file contains options to disable any features you would like to. Note that disabling a feature will remove it from the game entirely, not just the way to get it, so it is best to configure first, then make a world."; break; }
		case 2: { result = "There are also options to change certain attributes of certain objects, such as the delay between spawn attemps for the mob spawner." + '\n' + " Anyway, enjoy the mod!"; break; }
		}
		
		return result;
	}
}
