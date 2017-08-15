package curtis.cobbleworks.manual;

import curtis.cobbleworks.CommonProxy;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class PageCobbleUpgrades extends BookPage {
	
	private static final int[] pageTypes = new int[] {1, 1, 1, 1, 1};
	private static final ItemStack[] r0 = new ItemStack[] {new ItemStack(Items.IRON_INGOT), new ItemStack(Items.GOLD_INGOT), new ItemStack(Items.IRON_INGOT), new ItemStack(Blocks.FURNACE), new ItemStack(Blocks.COAL_BLOCK), new ItemStack(Blocks.PISTON), new ItemStack(Items.IRON_INGOT), new ItemStack(Items.GOLD_INGOT), new ItemStack(Items.IRON_INGOT), new ItemStack(CommonProxy.up1, 1, 0)};
	private static final ItemStack[] r1 = new ItemStack[] {new ItemStack(Items.GOLD_INGOT), new ItemStack(Items.DIAMOND), new ItemStack(Items.GOLD_INGOT), new ItemStack(Blocks.CRAFTING_TABLE), new ItemStack(CommonProxy.up1, 1, 0), new ItemStack(Blocks.CRAFTING_TABLE), new ItemStack(Items.GOLD_INGOT), new ItemStack(Items.DIAMOND), new ItemStack(Items.GOLD_INGOT), new ItemStack(CommonProxy.up1, 1, 1)};
	private static final ItemStack[] r2 = new ItemStack[] {new ItemStack(Items.MAGMA_CREAM), new ItemStack(Items.DIAMOND), new ItemStack(Items.MAGMA_CREAM), new ItemStack(Blocks.NETHER_BRICK), new ItemStack(CommonProxy.up1, 1, 1), new ItemStack(Blocks.NETHER_BRICK), new ItemStack(Items.MAGMA_CREAM), new ItemStack(Items.DIAMOND), new ItemStack(Items.MAGMA_CREAM), new ItemStack(CommonProxy.up1, 1, 2)};
	private static final ItemStack[] r3 = new ItemStack[] {new ItemStack(Blocks.QUARTZ_BLOCK), new ItemStack(Items.BLAZE_ROD), new ItemStack(Blocks.QUARTZ_BLOCK), new ItemStack(Items.LAVA_BUCKET), new ItemStack(CommonProxy.up1, 1, 2), new ItemStack(Items.LAVA_BUCKET), new ItemStack(Blocks.QUARTZ_BLOCK), new ItemStack(Items.BLAZE_ROD), new ItemStack(Blocks.QUARTZ_BLOCK), new ItemStack(CommonProxy.up1, 1, 3)};
	private static final ItemStack[] r4 = new ItemStack[] {new ItemStack(Items.DIAMOND), new ItemStack(Items.NETHER_STAR), new ItemStack(Items.DIAMOND), new ItemStack(Items.GHAST_TEAR), new ItemStack(CommonProxy.up1, 1, 3), new ItemStack(Items.GHAST_TEAR), new ItemStack(Items.GOLD_INGOT), new ItemStack(Items.GOLD_INGOT), new ItemStack(Items.GOLD_INGOT), new ItemStack(CommonProxy.up1, 1, 4)};
	
	public PageCobbleUpgrades() {
		super(4, pageTypes);
	}
	
	public String getButtonName() {
		return "Block Tier Upgrades";
	}
	
	public String getDisplayText(int minor) {
		int next = minor + 1;
		return "Use on a tier " + minor + " Cobbleworks or Advanced Cobbleworks block to upgrade it to tier " + next + ".";
	}
	
	public String getTooltipText(int minor) {
		return "";
	}
	
	public ItemStack[] getRecipeRender(int minor) {
		
		switch (minor) {
		case 0: { return r0; }
		case 1: { return r1; }
		case 2: { return r2; }
		case 3: { return r3; }
		case 4: { return r4; }
		}
		
		return null;
	}
}
