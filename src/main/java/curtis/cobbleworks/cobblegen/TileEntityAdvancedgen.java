package curtis.cobbleworks.cobblegen;

import curtis.cobbleworks.Config;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

public class TileEntityAdvancedgen extends TileEntityCobblegen implements IFluidHandler {
	
	public FluidTank lava;// = new FluidTank(((this instanceof TileEntityCustomgen) ? Config.customLavaCapacity : Config.advLavaCapacity));
	protected static float[] lavaConsumed = new float[] {0f, 1f, .5f, .1f, .1f, 1.0f, .1f, .1f, 10f};
	
	public TileEntityAdvancedgen() {
		power = new customEnergyStorage(Config.advPowerCapacity);
		lava = new FluidTank(Config.advLavaCapacity);
	}
	
	@Override
	public String getInventoryName() {
		return "Tier " + this.upgradeLevel + " Advanced CobbleWorks";
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
					EnergyPerTick = 0;
					break;
					}
				case 1: {
					incrementLimit = 18;
					EnergyPerTick = 500;
					setProduction(1, 0);
					setProduction(2, 0);
					setProduction(3, 0);
					break;
				}
				case 2: {
					incrementLimit = 36;
					EnergyPerTick = 1000;
					setProduction(4, 0);
					setProduction(5, 0);
					setProduction(6, 0);
					break;
				}
				
				case 3: {
					incrementLimit = 72;
					EnergyPerTick = 2000;
					setProduction(7, 0);
					setProduction(8, 0);
					break;
				}
				case 4: {
					incrementLimit = 144;	
					EnergyPerTick = 3000; 	
					break;
				}
				case 5: {
					incrementLimit = 288; 	
					EnergyPerTick = 4000;	
					break;
				}
			}
		}
	}
	
	@Override
	public ItemStack generateHelper(int index, int amount) {
		switch (index) {
		case 0: return new ItemStack(Blocks.MOSSY_COBBLESTONE, 	amount);
		case 1: return new ItemStack(Blocks.NETHERRACK, 		amount);
		case 2: return new ItemStack(Blocks.SOUL_SAND, 			amount);
		case 3: return new ItemStack(Blocks.ICE, 				amount);
		case 4: return new ItemStack(Blocks.END_STONE, 			amount);
		case 5: return new ItemStack(Blocks.CLAY, 				amount);
		case 6: return new ItemStack(Blocks.SNOW, 				amount);
		case 7: return new ItemStack(Items.PRISMARINE_SHARD, 		amount);
		case 8: return new ItemStack(Blocks.OBSIDIAN, 			amount);
		default: return null;
		}
	}
	
	@Override
	public void setProduction(int index, int amount) {
		
		if (tierRequired[index] > upgradeLevel) {
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
	public void update() {
		if (world.isRemote) {
			return;
		}
		
		if (power.getEnergyStored() >= calcRFCost() && ticksSinceEvent < 100 && !allZero() && calcLavaCost()) {
			ticksSinceEvent++;
			power.setEnergyStored((int)(power.getEnergyStored() - calcRFCost()), false);
			
			float sum = 0f;
			
			for (int i = 0; i < 9; i++) {
				sum += this.lavaConsumed[i] * (float)this.produceAmount[i];
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
	
	public float calcLavaCostFloat() {
		float sum = 0f;
		
		for (int i = 0; i < 9; i++) {
			sum += this.lavaConsumed[i] * (float)this.produceAmount[i];
		}
		
		return sum;
	}
	
	public int calcLavaBarHeight() {
		int result = 0;
		
		result = 52 * this.lava.getFluidAmount() / this.lava.getCapacity();
		
		return result;
	}
	
	protected boolean calcLavaCost() {
		float sum = 0f;
		
		for (int i = 0; i < 9; i++) {
			sum += this.lavaConsumed[i] * (float)this.produceAmount[i];
		}
		
		if (sum > (float)lava.getFluidAmount()) {
			return false;
		}
		
		return true;
	}
	
	@Override
	protected void readSyncableDataFromNBT(NBTTagCompound msgNBT) {
		super.readSyncableDataFromNBT(msgNBT);
		lava.readFromNBT(msgNBT);
	}
	
	@Override
	protected void writeSyncableDataToNBT(NBTTagCompound msgNBT) {
		super.writeSyncableDataToNBT(msgNBT);
		lava.writeToNBT(msgNBT);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound q) {
		super.readFromNBT(q);
		lava.readFromNBT(q);
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound q) {
		super.writeToNBT(q);
		lava.writeToNBT(q);
		return q;
	}

	@Override
	public IFluidTankProperties[] getTankProperties() {
		return lava.getTankProperties();
	}

	@Override
	public int fill(FluidStack resource, boolean doFill) {
		
		if (resource == null) {
			return 0;
		}
		
		if (resource.getFluid() == null || resource.getFluid() != FluidRegistry.LAVA) {
			return 0;
		}
		
		return lava.fill(resource, doFill);
	}

	@Override
	public FluidStack drain(FluidStack resource, boolean doDrain) {
		return null;
	}

	@Override
	public FluidStack drain(int maxDrain, boolean doDrain) {
		return null;
	}
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		
		if (capability == CapabilityEnergy.ENERGY || capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
			return true;
		}
		
		return super.hasCapability(capability, facing);
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (capability == CapabilityEnergy.ENERGY || capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
			return (T)this;
		}
		
		return super.getCapability(capability, facing);
	}
}
