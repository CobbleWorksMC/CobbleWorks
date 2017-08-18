package curtis.cobbleworks.cobblegen;

import curtis.cobbleworks.Config;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class TileEntityCobblegen extends TileEntity implements ITickable, /*IInventory,*/ IItemHandler, IEnergyStorage {
	
	private NonNullList<ItemStack> contents = NonNullList.<ItemStack>withSize(36, ItemStack.EMPTY);
	protected String customName;
    protected int ticksSinceEvent = 0; 
    protected int upgradeLevel = 0;
    protected static int[] tierRequired = new int[] {0, 1, 1, 1, 2, 2, 2, 3, 3};
    public int incrementLimit = 9;
    public customEnergyStorage power = new customEnergyStorage(Config.basicPowerCapacity);
    public int EnergyPerTick = 0;
    public int[] produceAmount = new int[] {1, 0, 0, 0, 0, 0, 0, 0, 0};
    protected int numEnabled = 1;
	
    public TileEntityCobblegen() {
    }

    public String getCustomName() {
        return this.customName;
    }

    public void setCustomName(String customName) {
        this.customName = customName;
    }
	
    public String getName() {
        return this.hasCustomName() ? this.customName : this.getInventoryName();
    }

    public boolean hasCustomName() {
        return this.customName != null && !this.customName.equals("");
    }
    
    public NonNullList<ItemStack> getInv() {
    	return contents;
    }
    
	//@Override
	public int getSizeInventory() {
		return 36;
	}
	
	@Override
	public ItemStack getStackInSlot(int index) {
	    if (index < 0 || index >= this.getSizeInventory())
	        return ItemStack.EMPTY;
	    return this.contents.get(index);
	}
	
	public boolean isInventoryEmpty() {
		boolean result = true;
		for(int i = 0; i < this.getSizeInventory(); i++) {
			if (contents.get(i) != ItemStack.EMPTY) {
				result = false;
			}
		}
		return result;
	}

	//@Override
	public ItemStack decrStackSize(int i, int c) {
	
		if (this.getStackInSlot(i) != ItemStack.EMPTY) {
			ItemStack itemstack;
			
			if (this.getStackInSlot(i).getCount() <= c){
				itemstack = this.getStackInSlot(i);
				this.setInventorySlotContents(i, ItemStack.EMPTY);
				this.markDirty();
				return itemstack;
			} else {
				itemstack = this.getStackInSlot(i).splitStack(c);
			
				if (this.getStackInSlot(i).getCount() <= 0){
					this.setInventorySlotContents(i, ItemStack.EMPTY);
				} else {
					this.setInventorySlotContents(i, this.getStackInSlot(i));
				}
			
				this.markDirty();
				return itemstack;
				}
			
			} else {
			return ItemStack.EMPTY;
		}
	}	

	//@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
	    if (index < 0 || index >= this.getSizeInventory())
	        return;
	    if (stack != ItemStack.EMPTY && stack.getCount() > this.getInventoryStackLimit())
	        stack.setCount(this.getInventoryStackLimit());
	    if (stack != ItemStack.EMPTY && stack.getCount() == 0)
	        stack = ItemStack.EMPTY;
	    this.contents.set(index, stack);
	    this.markDirty();
	}

	public String getInventoryName() {
		return "Tier " + this.upgradeLevel + " CobbleWorks";
	}

	//@Override
	public int getInventoryStackLimit() {
		return 64;
	}
	
	public boolean isUsableByPlayer(EntityPlayer player) {
    	return this.world.getTileEntity(this.pos) != this ? false : player.getDistanceSq((double)this.pos.getX() + 0.5D, (double)this.pos.getY() + 0.5D, (double)this.pos.getZ() + 0.5D) <= 64.0D;
    }

    public void openInventory(EntityPlayer player) {
    }

    public void closeInventory(EntityPlayer player) {
    }
	
	//@Override
	public boolean isItemValidForSlot(int i, ItemStack s) {
		return false;
	}
	
	public void setLevel(int i) {
		if (!world.isRemote){
			upgradeLevel = i;
			
			if (upgradeLevel > 5) {
				upgradeLevel = 5;
			}
			ticksSinceEvent = 0;
			switch (i){
				case 0: {
					incrementLimit = 9;
					EnergyPerTick = 0;
					break;
					}
				case 1: {
					incrementLimit = 36;
					EnergyPerTick = 250;
					setProduction(1, 0);
					setProduction(2, 0);
					setProduction(3, 0);
					break;
				}
				case 2: {
					incrementLimit = 72;
					EnergyPerTick = 500;
					setProduction(4, 0);
					setProduction(5, 0);
					setProduction(6, 0);
					break;
				}
				
				case 3: {
					incrementLimit = 144;
					EnergyPerTick = 1000;
					setProduction(7, 0);
					setProduction(8, 0);
					break;
				}
				case 4: incrementLimit = 288;	EnergyPerTick = 1500; 	break;
				case 5: incrementLimit = 576; 	EnergyPerTick = 2000;	break;
			}
		}
	}
	
	public int getLevel() {
		return upgradeLevel;
	}
	
	public int getTierRequired(int i) {
		return this.tierRequired[i];
	}
	
	@Override
	public void readFromNBT(NBTTagCompound q) {
		super.readFromNBT(q);
		
		NBTTagList nbttaglist = q.getTagList("Items", 10);
		power.setEnergyStored(q.getInteger("RF"), false);
		incrementLimit = q.getInteger("incrementLimit");
		EnergyPerTick = q.getInteger("RF/t");
		ticksSinceEvent = q.getInteger("Time");
		upgradeLevel = q.getInteger("upgradeLevel");
		
		produceAmount = q.getIntArray("prodAmt");
		
		if (q.hasKey("CustomName", 8)) {
			customName = q.getString("CustomName");
		}
		
		ItemStackHelper.loadAllItems(q, contents);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound q) {
		
		super.writeToNBT(q);
		q.setInteger("RF", power.getEnergyStored());
		q.setInteger("RF/t", EnergyPerTick);
		q.setInteger("Time", ticksSinceEvent);
		q.setInteger("incrementLimit", incrementLimit);
		q.setInteger("upgradeLevel", upgradeLevel);
		
		q.setIntArray("prodAmt", produceAmount);

		ItemStackHelper.saveAllItems(q, contents);
		
		if (this.hasCustomName()) {
			q.setString("CustomName", this.customName);
		}
		
		return q;
	}

	public int getField(int id) {
	    return 0;
	}

	public void setField(int id, int value) {
	}

	public int getFieldCount() {
	    return 0;
	}
	
	public void clear() {
	    for (int i = 0; i < this.getSizeInventory(); i++)
	        setInventorySlotContents(i, ItemStack.EMPTY);
	}
	
	public boolean hasPower() {
		
		return (power.getEnergyStored() > 9*EnergyPerTick) ? true : false;
	}
	
	@Override
	public void update() {
		if (world.isRemote) {
			return;
		}
		
		if (power.getEnergyStored() >= calcRFCost() && ticksSinceEvent < 100 && !allZero()) {
			ticksSinceEvent++;
			power.setEnergyStored((int)(power.getEnergyStored() - calcRFCost()), false);
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
	
	//Passes in index of increment amount to look up, uses it to set stacks
	public void generate(int offset) {
		int make = produceAmount[offset];
		
		if (make >= 192) {
			contents.set(4*offset+3, generateHelper(offset, 64));
			make -= 64;
		}
		
		if (make >= 128) {
			contents.set(4*offset+2, generateHelper(offset, 64));
			make -= 64;
		}
		
		if (make >= 64) {
			contents.set(4*offset+1, generateHelper(offset, 64));
			make -= 64;
		}
		
		if (make > 0) {
			contents.set(4*offset, generateHelper(offset, make));
			make -= 64;
		}
	}
	
	//Helps generate, obviously
	public ItemStack generateHelper(int index, int amount) {
		switch (index) {
		case 0: return new ItemStack(Blocks.COBBLESTONE, 	amount);
		case 1: return new ItemStack(Blocks.STONE, 			amount);
		case 2: return new ItemStack(Blocks.SAND, 			amount);
		case 3: return new ItemStack(Blocks.GLASS, 			amount);
		case 4: return new ItemStack(Blocks.GRAVEL, 		amount);
		case 5: return new ItemStack(Items.FLINT, 			amount);
		case 6: return new ItemStack(Blocks.STONEBRICK, 	amount);
		case 7: return new ItemStack(Blocks.SANDSTONE, 		amount);
		case 8: return new ItemStack(Blocks.DIRT, 			amount);
		default: return ItemStack.EMPTY;
		}
	}
	
	public float calcRFCost() {
		
		int total = 0;
		
		for (int i : produceAmount) {
			total += i;
		}
		
		float ratio = (total / (float)incrementLimit);
		float cost = (float)EnergyPerTick * ratio;
		return cost;
	}
	
	public int calcProduced() {
		
		int total = 0;
		
		for (int i : produceAmount) {
			total += i;
		}
		
		return total;
	}
	
	public int getIncrementLimit() {
		return incrementLimit;
	}
	
	public boolean allZero() {
		
		for(int i = 0; i < produceAmount.length; i++) {
			if (upgradeLevel < tierRequired[i]) {
				return true;
			} else if (produceAmount[i] != 0) {
				return false;
			}
		}
		
		return true;
	}
	
	@Override
	public boolean canReceive() {
    	return true;
	}

	@Override
	public int receiveEnergy(int maxReceive, boolean simulate) {
		
		return power.receiveEnergy(maxReceive, simulate);
	}
	
	@Override
	public int getEnergyStored() {
		
		return power.getEnergyStored();
	}

	@Override
	public int getMaxEnergyStored() {
		
		return 250000;
	}
	
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
		if (produceAmount[index] > 256) {
			produceAmount[index] = 256;
		}
	}
	
	
	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		NBTTagCompound syncData = new NBTTagCompound();
	    this.writeSyncableDataToNBT(syncData);
	    return new SPacketUpdateTileEntity(this.getPos(), 1, syncData);
	}
	
	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		this.readSyncableDataFromNBT(pkt.getNbtCompound());
	}
	
	@Override
	public NBTTagCompound getUpdateTag() {
		NBTTagCompound q = new NBTTagCompound(); 
		writeSyncableDataToNBT(q);
		return q;
	}
	
	@Override
	 public void handleUpdateTag(NBTTagCompound tag) {
		 this.readSyncableDataFromNBT(tag);
	 }
	
	protected void readSyncableDataFromNBT(NBTTagCompound msgNBT) {
		power.setEnergyStored(msgNBT.getInteger("power"), false);
		EnergyPerTick = msgNBT.getInteger("rft");
		ticksSinceEvent = msgNBT.getInteger("Progress");
		upgradeLevel = msgNBT.getInteger("Tier");
		produceAmount = msgNBT.getIntArray("prodAmt");
	}
	
	protected void writeSyncableDataToNBT(NBTTagCompound msgNBT) {
		msgNBT.setInteger("power", power.getEnergyStored());
		msgNBT.setInteger("rft", EnergyPerTick);
		msgNBT.setInteger("Progress", ticksSinceEvent);
		msgNBT.setInteger("Tier", upgradeLevel);
		msgNBT.setIntArray("prodAmt", produceAmount);
	}
	
	public void receiveMessageFromClient(NBTTagCompound msgNBT) {
		if(msgNBT.hasKey("index") && msgNBT.hasKey("amount")) {
			int index = msgNBT.getInteger("index");
			int amount = msgNBT.getInteger("amount");
			setProduction(index, amount);
			this.markDirty();
		} else {
			System.out.println("receiveMessageFromClient acted dumb");
		}
	}
	
	public int getProgress() {
		return ticksSinceEvent;
	}

	@Override
	public boolean canExtract() {
		return false;
	}

	//@Override
	public ItemStack removeStackFromSlot(int index) {
		return ItemStackHelper.getAndRemove(this.contents, index);
	}

	public boolean canInteractWith(EntityPlayer playerIn) {
		return !isInvalid() && playerIn.getDistanceSq(pos.add(0.5D, 0.5D, 0.5D)) <= 64D;
	}

	@Override
	public int extractEnergy(int maxExtract, boolean simulate) {
		
		return power.extractEnergy(maxExtract, simulate);
	}
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		
		if (capability == CapabilityEnergy.ENERGY || capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return true;
		}
		
		return super.hasCapability(capability, facing);
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (capability == CapabilityEnergy.ENERGY || capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return (T)this;
		}
		
		return super.getCapability(capability, facing);
	}
	
	public int getProduceAmount(int i) {
		if (i < 0 || i > 8) {
			return 0;
		}
		return produceAmount[i];
	}

	//@Override
	public boolean isEmpty() {
		return this.isInventoryEmpty();
	}

	@Override
	public int getSlots() {
		return getSizeInventory();
	}

	@Override
	public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
		return stack;
	}

	@Override
	public ItemStack extractItem(int slot, int amount, boolean simulate) {
		
		//System.out.println("Attempting to get item(s) from slot: " + slot);
		if (slot < 0 || slot >= this.getSizeInventory()) {
			//System.out.println("Error: out of bounds exception");
	        return ItemStack.EMPTY;
		}
		
		if (contents.get(slot).isEmpty()) {
			//System.out.println("Stack was empty.");
			return ItemStack.EMPTY;
		}
		
	    ItemStack stack = contents.get(slot);
	    //System.out.println("Stack is:" + stack.getDisplayName() + ", with a size of " + stack.getCount());
	    
	    if (stack.getCount() > amount) {
	    	ItemStack stack2 = new ItemStack(stack.getItem(), stack.getCount());
	    	stack.setCount(stack.getCount() - amount);
	    	//System.out.println("Set stack size to " + stack.getCount());
	    	if (stack.getCount() == 0) {
	    		contents.set(slot, ItemStack.EMPTY);
	    	}
	    	return stack2;
	    } else {
	    	ItemStack stack2 = new ItemStack(stack.getItem(), stack.getCount());
	    	//System.out.println("Returning " + stack.getDisplayName() + ", with a size of " + stack.getCount());
	    	stack.setCount(0);
	    	contents.set(slot, ItemStack.EMPTY);
	    	return stack2;
	    }
	    
	    //return stack;
	}

	@Override
	public int getSlotLimit(int slot) {
		return this.getInventoryStackLimit();
	}
	
	
	
}
