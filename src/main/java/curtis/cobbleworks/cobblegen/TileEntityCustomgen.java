package curtis.cobbleworks.cobblegen;

import curtis.cobbleworks.Config;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public class TileEntityCustomgen extends TileEntityAdvancedgen {
	
	public static ItemStack[] generated = new ItemStack[9];
	public static boolean isEnabled = false;
	protected static float[] advLavaConsumed = new float[] {0f, 1f, .5f, .1f, .1f, 1.0f, .1f, .1f, 10f};
	
	/*
	 * Only needs to be called once.
	 * Determines based on configs whether or not to enable this block.
	 * 
	 * */
	public static boolean isActive() {
		int i = 0;
		
		for (String id : Config.customItems) {
			
			String newId = id;
			int meta = 0;
			
			if (id.contains(",")) {
				String[] str = id.split(",");
				newId = str[0].trim();
				meta = Integer.parseInt(str[1].trim());
			} else {
				newId = id.trim();
			}
			
			Item thing = Item.getByNameOrId(newId);
			
			if (thing != null) {
				generated[i] = new ItemStack(thing, 1, meta);
				System.out.println("Registered " + id + " to the Custom Cobbleworks as " + generated[i].getDisplayName() + ".");
			} else {
				System.out.println("Failed to register item: " + id + " to the Custom Cobbleworks, aborting!");
				isEnabled = false;
				return false;
			}
			
			++i;
		}
		
		init();
		System.out.println("Successfully registered all nine items to the Custom Cobbleworks! Woohoo!");
		isEnabled = true;
		return true;
	}
	
	public TileEntityCustomgen() {
		
	}
	
	public static void init() {
		
		for (int i = 0; i < 9; i++) {
			tierRequired[i] = Config.customTiers[i];
			advLavaConsumed[i] = ((float) Config.customLava[i]) / 1000f;
		}
	}
	
	@Override
	public void setLevel(int i) {
		if (!world.isRemote){
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
	
	public String getName() {
        return this.hasCustomName() ? this.customName : this.getInventoryName();
    }
	
	public String getInventoryName() {
		return "Tier " + this.upgradeLevel + " Custom CobbleWorks";
	}
	
	@Override
	public void update() {
		if (world.isRemote) {
			return;
		}
		
		if (!isEnabled) {
			return;
		}
		
		if (power.getEnergyStored() >= calcRFCost() && ticksSinceEvent < 100 && !allZero() && calcLavaCost()) {
			ticksSinceEvent++;
			power.setEnergyStored((int)(power.getEnergyStored() - calcRFCost()), false);
			
			float sum = 0f;
			
			for (int i = 0; i < 9; i++) {
				sum += this.advLavaConsumed[i] * (float)this.produceAmount[i];
			}
			
			lava.drain((int)sum, true);
		}
		
		if (ticksSinceEvent == 100 && isInventoryEmpty()) {
			for (int gg = 0; gg < 9; gg++) {
				if (upgradeLevel >= tierRequired[gg]) {
					generate(gg);
				}
			}
			ticksSinceEvent = 0;
		}
		
		if (ticksSinceEvent > 100 || ticksSinceEvent < 0) {
			ticksSinceEvent = 0;
			System.out.println("Debug time! We've managed to make ticksSinceEvent more than 100 or less than zero.");
		}
		
		IBlockState state = world.getBlockState(getPos());
		world.notifyBlockUpdate(getPos(), state, state, 3);
		this.markDirty();
		return;
	}
}
