package curtis.Cobbleworks.module.book.pages;

import curtis.Cobbleworks.CommonProxy;
import curtis.Cobbleworks.module.book.BookPage;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class PagePaydayArmor extends BookPage {
	
	public static final ItemStack[] recipe1 = new ItemStack[] {new ItemStack(Blocks.WOOL), new ItemStack(Items.LEATHER), new ItemStack(Blocks.WOOL),
											new ItemStack(Blocks.WOOL), new ItemStack(Items.DYE, 1, 0), new ItemStack(Blocks.WOOL),
											new ItemStack(Blocks.AIR), new ItemStack(Blocks.AIR), new ItemStack(Blocks.AIR),
											new ItemStack(CommonProxy.suitHat)};
	public static final ItemStack[] recipe2 = new ItemStack[] {new ItemStack(Blocks.WOOL), new ItemStack(Items.DYE, 1, 0), new ItemStack(Blocks.WOOL),
											new ItemStack(Blocks.WOOL), new ItemStack(Items.LEATHER), new ItemStack(Blocks.WOOL),
											new ItemStack(Blocks.WOOL), new ItemStack(Blocks.WOOL), new ItemStack(Blocks.WOOL),
											new ItemStack(CommonProxy.suitChest)};
	public static final ItemStack[] recipe3 = new ItemStack[] {new ItemStack(Blocks.WOOL), new ItemStack(Items.LEATHER), new ItemStack(Blocks.WOOL),
											new ItemStack(Blocks.WOOL), new ItemStack(Items.DYE, 1, 0), new ItemStack(Blocks.WOOL),
											new ItemStack(Blocks.WOOL), new ItemStack(Blocks.AIR), new ItemStack(Blocks.WOOL),
											new ItemStack(CommonProxy.suitLegs)};
	public static final ItemStack[] recipe4 = new ItemStack[] {new ItemStack(Blocks.WOOL), new ItemStack(Items.DYE, 1, 0), new ItemStack(Blocks.WOOL),
											new ItemStack(Blocks.WOOL), new ItemStack(Items.LEATHER), new ItemStack(Blocks.WOOL),
											new ItemStack(Blocks.AIR), new ItemStack(Blocks.AIR), new ItemStack(Blocks.AIR),
											new ItemStack(CommonProxy.suitBoots)};

	public PagePaydayArmor() {
		super(4, new int[] {0, 1, 1, 1, 1});
	}
	
	public String getButtonName() {
		return "Nice Suit";
	}
	
	public String getDisplayText(int minor) {
		
		String result = "";
		
		switch (minor) {
		case 0: { result = "Not all armor protects the user by reducing the damage they take. This armor is unique, it does not offer any physical protection, instead it gives you a chance to simply not take damage when you otherwise would. Multiple pieces add their chance together."; break; }
		case 1: { result = "Not a clown mask, but it will do."; break; }
		case 2: { result = "How does dodging bullets work, anyway?"; break; }
		case 3: { result = "This suit does not protect from drop kicks, tasers, or high caliber rifles."; break; }
		case 4: { result = "THERE WILL BE NO PAYDAY FOR YOU!"; break; }
		}
		
		return result;
	}
	
	public String getTooltipText(int minor) {
		return "Don\'t act dumb";
	}
	
	public ItemStack[] getRecipeRender(int minor) {
		switch (minor) {
		case 1: return recipe1;
		case 2: return recipe2;
		case 3: return recipe3;
		case 4: return recipe4;
		}
		
		return null;
	}
}
