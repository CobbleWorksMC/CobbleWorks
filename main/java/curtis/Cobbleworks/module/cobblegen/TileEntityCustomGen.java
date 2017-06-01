package curtis.Cobbleworks.module.cobblegen;

import curtis.Cobbleworks.Config;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class TileEntityCustomGen extends TileEntityAdvancedGen {
	
	public static ItemStack[] generated = new ItemStack[9];
	
	/*
	 * Only needs to be called once.
	 * Determines based on configs whether or not to enable this block.
	 * 
	 * */
	public static boolean isActive() {
		int i = 0;
		
		for (String id : Config.customItems) {
			
			Item thing = Item.getByNameOrId(id);
			
			if (thing != null) {
				generated[i] = new ItemStack(thing);
				System.out.println("Registered " + id + " to the Custom Cobbleworks.");
			} else {
				System.out.println("Failed to register item: " + id + " to the Custom Cobbleworks, aborting!");
				return false;
			}
			
			++i;
		}
		
		
		init();
		System.out.println("Successfully registered all nine items to the Custom Cobbleworks! Woohoo!");
		return true;
	}
	
	public TileEntityCustomGen() {
		
	}
	
	public static void init() {
		
		for (int i = 0; i < 9; i++) {
			tierRequired[i] = Config.customTiers[i];
			TileEntityCustomGen.lavaConsumed[i] = ((float) Config.customLava[i]) / 1000f;
		}
	}
	
	@Override
	public void setLevel(int i) {
		if (!worldObj.isRemote){
			upgradeLevel = i;
			
			if (upgradeLevel > 5 || upgradeLevel < 0) {
				upgradeLevel = 0;
			}
			ticksSinceEvent = 0;
			switch (i){
				case 0: {
					incrementLimit = 9;
					EnergyPerTick = Config.customPower[0];
					break;
					}
				case 1: {
					incrementLimit = 18;
					EnergyPerTick = Config.customPower[1];
					setProduction(1, 0);
					setProduction(2, 0);
					setProduction(3, 0);
					break;
				}
				case 2: {
					incrementLimit = 36;
					EnergyPerTick = Config.customPower[2];
					setProduction(4, 0);
					setProduction(5, 0);
					setProduction(6, 0);
					break;
				}
				
				case 3: {
					incrementLimit = 72;
					EnergyPerTick = Config.customPower[3];
					setProduction(7, 0);
					setProduction(8, 0);
					break;
				}
				case 4: {
					incrementLimit = 144;	
					EnergyPerTick = Config.customPower[4]; 	
					break;
				}
				case 5: {
					incrementLimit = 288; 	
					EnergyPerTick = Config.customPower[5];	
					break;
				}
			}
		}
	}
	
	@Override
	public ItemStack generateHelper(int index, int amount) {
		
		return new ItemStack(generated[index].getItem(), amount);
	}
}
