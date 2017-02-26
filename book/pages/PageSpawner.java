package curtis.Cobbleworks.module.book.pages;

import curtis.Cobbleworks.CommonProxy;
import curtis.Cobbleworks.Config;
import curtis.Cobbleworks.module.book.BookPage;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class PageSpawner extends BookPage {
	
	private static final int[] pageTypes = new int[] {0, 1, 0, 0, 0, 0};
	
	public PageSpawner() {
		super(5, pageTypes);
	}
	
	public String getButtonName() {
		return "Pendulum Summoner";
	}
	
	public String getDisplayText(int minor) {
		String result = "";
		
		switch (minor) {
		case 0: { result = "The Pendulum Summoner is this mod's take on a mob spawing system. If you play children's trading card games, you likely know what this is based off of, and it works exactly how you think. For those who do not know, the following pages will explain everything."; break; }
		case 1: { result = "The best monsters never die, they just chill out in the extra deck." + '\n' + "The Scale items are found in dungeons."; break; }
		case 2: { result = "To get started using the spawner, you need to collect some mob cards. You can find these in packs, in various dungeon chests." + '\n' + "Once you collect some cards (one pack gives three, I recommend finding several packs), you should be ready to spawn some mobs."; break; }
		case 3: { result = "There are two numbers that are important on these cards. Scale determines what level mobs you can spawn, and level determines what kind of scales you need to summon the mob." + '\n' + "You must have two cards, one each placed in the far apart slots of the spawner's GUI (set as scales). "; break; }
		case 4: { result = "Next, place up to five cards in the middle five slots. The spawner will spawn monsters from them, whos levels are exclusively between the scale numbers of the two cards set as scales. So you need one higher scale and one lower scale (neither equal to) than your monster's levels. Order doesn't matter."; break; }
		case 5: { result = "The delay between spawns is configurable. You can try to balance it however you like, even if that means making it horribly imbalanced. The default delay is 600 ticks, or 30 seconds, between spawn cycles." + '\n' + "The current spawn delay is set to " + Config.spawnerDelay + " ticks."; break; }
		}
		
		return result;
	}
	
	public String getTooltipText(int minor) {
		
		if (minor == 1) {
			return "Pls no solemn strike";
		}
		
		return "";
	}
	
	public ItemStack[] getRecipeRender(int minor) {
		
		if (minor == 1) {
			return new ItemStack[] {new ItemStack(Blocks.OBSIDIAN), new ItemStack(Items.CLOCK), new ItemStack(Blocks.OBSIDIAN), new ItemStack(CommonProxy.spaceScale), new ItemStack(Items.NETHER_STAR), new ItemStack(CommonProxy.timeScale), new ItemStack(Blocks.OBSIDIAN), new ItemStack(Items.END_CRYSTAL), new ItemStack(Blocks.OBSIDIAN), new ItemStack(CommonProxy.summoner),};
		}
		
		return null;
	}
}
