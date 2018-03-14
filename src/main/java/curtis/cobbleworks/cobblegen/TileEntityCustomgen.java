package curtis.cobbleworks.cobblegen;

import curtis.cobbleworks.Config;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.FluidTank;

public class TileEntityCustomgen extends TileEntityAdvancedgen {
	
	public static ItemStack[] generated = new ItemStack[9];
	public static boolean isEnabled = false;
	/*protected static float[] advLavaConsumed = new float[] {((float) Config.customLava[0]) / 1000f, 
															((float) Config.customLava[1]) / 1000f, 
															((float) Config.customLava[2]) / 1000f, 
															((float) Config.customLava[3]) / 1000f, 
															((float) Config.customLava[4]) / 1000f, 
															((float) Config.customLava[5]) / 1000f, 
															((float) Config.customLava[6]) / 1000f, 
															((float) Config.customLava[7]) / 1000f, 
															((float) Config.customLava[8]) / 1000f};*/
	/*protected static int[] customTierRequired = new int[] {Config.customTiers[0],
															Config.customTiers[1],
															Config.customTiers[2],
															Config.customTiers[3],
															Config.customTiers[4],
															Config.customTiers[5],
															Config.customTiers[6],
															Config.customTiers[7],
															Config.customTiers[8]};*/
	
	public TileEntityCustomgen() {
		power = new customEnergyStorage(Config.customPowerCapacity);
		lava = new FluidTank(Config.customLavaCapacity);
	}
	
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
		
		//init();
		System.out.println("Successfully registered all nine items to the Custom Cobbleworks! Woohoo!");
		isEnabled = true;
		return true;
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
					break;
				}
				case 2: {
					incrementLimit = 36;
					EnergyPerTick = Config.customPower[2];
					break;
				}
				
				case 3: {
					incrementLimit = 72;
					EnergyPerTick = Config.customPower[3];
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
	public void setProduction(int index, int amount) {
		
		if (Config.customTiers[index] > upgradeLevel) {
			return;
		}
		
		int realAmount = amount;
		int total = 0;
		
		//add up total blocks produced
		for (int i = 0; i < 9; i++) {
			total += produceAmount[i];
		}
		
		//check if total + new amount is more than the limit
		if ((total + amount) > incrementLimit) {
			//if it is, reduce real amount to fit the limit
			realAmount = (incrementLimit - total);
		}
		
		produceAmount[index] += realAmount;
		
		//typical sanity checks
		if (produceAmount[index] < 0) {
			produceAmount[index] = 0;
		}
		if (produceAmount[index] > incrementLimit) {
			produceAmount[index] = incrementLimit;
		}
		if (produceAmount[index] > 128) {
			produceAmount[index] = 128;
		}
	}
	
	@Override
	public ItemStack generateHelper(int index, int amount) {
		
		return new ItemStack(generated[index].getItem(), amount, generated[index].getMetadata());
	}
	
	public String getName() {
        return this.hasCustomName() ? this.customName : this.getInventoryName();
    }
	
	public String getInventoryName() {
		return "Tier " + this.upgradeLevel + " Custom CobbleWorks";
	}
	
	protected boolean calcLavaCost() {
		
		float sum = 0f;
		
		for (int i = 0; i < 9; i++) {
			sum += Config.customLava[i] * (float)this.produceAmount[i];
		}
		
		if (sum > (float)lava.getFluidAmount()) {
			return false;
		}
		
		return true;
	}
	
	public float calcLavaCostFloat() {
		float sum = 0f;
		
		for (int i = 0; i < 9; i++) {
			sum += Config.customLava[i] * (float)this.produceAmount[i];
		}
		
		return sum;
	}
	
	@Override
	public float calcRFCost() {
		
		int total = 0;
		
		for (int i : produceAmount) {
			total += i;
		}
		
		float ratio = (total / (float)incrementLimit);
		float cost = (float)Config.customPower[this.upgradeLevel] * ratio;
		return cost;
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
				sum += Config.customLava[i] * (float)this.produceAmount[i];
			}
			
			lava.drain((int)sum, true);
		}
		
		if (ticksSinceEvent == 100 && isInventoryEmpty()) {
			for (int gg = 0; gg < 9; gg++) {
				if (upgradeLevel >= Config.customTiers[gg]) {
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
