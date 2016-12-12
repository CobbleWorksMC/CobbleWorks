package curtis.Cobbleworks.module.cobblegen;

import curtis.Cobbleworks.module.energy.customEnergyStorage;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.main.Main;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;

public class TileEntityCobbleGen extends TileEntity implements ITickable, IInventory, IEnergyStorage {
	
	private ItemStack[] contents = new ItemStack[this.getSizeInventory()];
    private String customName;
	private int ticksSinceEvent = 0; 
    private int upgradeLevel = 0;
    private static final int[] tierRequired = new int[] {0, 1, 1, 1, 2, 2, 2, 3, 3};
    private int incrementLimit = 1;
    public customEnergyStorage power = new customEnergyStorage(250000);
    private int EnergyPerTick = 0;
    private int[] produceAmount = new int[] {1, 0, 0, 0, 0, 0, 0, 0, 0};
    private int numEnabled = 1;
	
    public TileEntityCobbleGen() {
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
    
	@Override
	public int getSizeInventory() {
		return 36;
	}

	@Override
	public ItemStack getStackInSlot(int index) {
	    if (index < 0 || index >= this.getSizeInventory())
	        return null;
	    return this.contents[index];
	}
	
	public boolean isInventoryEmpty() {
		boolean result = true;
		for(int i = 0; i < this.getSizeInventory(); i++) {
			if (contents[i] != null) {
				result = false;
			}
		}
		return result;
	}

	@Override
	public ItemStack decrStackSize(int i, int c) {
	
		if (this.getStackInSlot(i) != null) {
			ItemStack itemstack;
			
			if (this.getStackInSlot(i).stackSize <= c){
				itemstack = this.getStackInSlot(i);
				this.setInventorySlotContents(i, null);
				this.markDirty();
				return itemstack;
			} else {
				itemstack = this.getStackInSlot(i).splitStack(c);
			
				if (this.getStackInSlot(i).stackSize <= 0){
					this.setInventorySlotContents(i, null);
				} else {
					this.setInventorySlotContents(i, this.getStackInSlot(i));
				}
			
				this.markDirty();
				return itemstack;
				}
			
			} else {
			return null;
		}
	}	

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
	    if (index < 0 || index >= this.getSizeInventory())
	        return;
	    if (stack != null && stack.stackSize > this.getInventoryStackLimit())
	        stack.stackSize = this.getInventoryStackLimit();
	    if (stack != null && stack.stackSize == 0)
	        stack = null;
	    this.contents[index] = stack;
	    this.markDirty();
	}

	public String getInventoryName() {
		return "Tier " + this.upgradeLevel + " CobbleWorks";
	}

	@Override
	public int getInventoryStackLimit() {

		return 64;
	}

    public boolean isUseableByPlayer(EntityPlayer player) {
    	return this.worldObj.getTileEntity(this.pos) != this ? false : player.getDistanceSq((double)this.pos.getX() + 0.5D, (double)this.pos.getY() + 0.5D, (double)this.pos.getZ() + 0.5D) <= 64.0D;
    }

    public void openInventory(EntityPlayer player) {
    }

    public void closeInventory(EntityPlayer player) {
    }
	
	@Override
	public boolean isItemValidForSlot(int i, ItemStack s) {
		return false;
	}
	
	public void setLevel(int i) {
		if (!worldObj.isRemote){
			upgradeLevel = i;
			//System.out.println("Successfully upgraded CobbleGen to level: " + this.upgradeLevel);
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
					incrementLimit = 18;
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
		//contents = new ItemStack[this.getSizeInventory()];
		incrementLimit = q.getInteger("incrementLimit");
		EnergyPerTick = q.getInteger("RF/t");
		ticksSinceEvent = q.getInteger("Time");
		//this.storage = q.getIntArray("storage");
		upgradeLevel = q.getInteger("upgradeLevel");
		//System.out.println("Power retrieved: " + q.getInteger("RF") + " RF is now " + this.power.getEnergyStored());
		
		produceAmount = q.getIntArray("prodAmt");
		
		if (q.hasKey("CustomName", 8)) {
			customName = q.getString("CustomName");
		}
		
		for (int i = 0; i < nbttaglist.tagCount(); ++i) {
			NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
			int j = nbttagcompound1.getByte("Slot1") & 255;

			if (j >= 0 && j < contents.length)
			{
				this.contents[j] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
			}
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound q) {
		
		super.writeToNBT(q);
		q.setInteger("RF", power.getEnergyStored());
		q.setInteger("RF/t", EnergyPerTick);
		q.setInteger("Time", ticksSinceEvent);
		q.setInteger("incrementLimit", incrementLimit);
		q.setInteger("upgradeLevel", upgradeLevel);
		NBTTagList nbttaglist = new NBTTagList();
		
		q.setIntArray("prodAmt", produceAmount);

		for (int i = 0; i < contents.length; ++i) {
			if (this.contents[i] != null) {
				NBTTagCompound nbttagcompound1 = new NBTTagCompound();
				nbttagcompound1.setByte("Slot1", (byte)i);
				contents[i].writeToNBT(nbttagcompound1);
				nbttaglist.appendTag(nbttagcompound1);
			}
		}
		
		q.setTag("Items", nbttaglist);
		
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
	        setInventorySlotContents(i, null);
	}
	
	public boolean checkStack(int i, ItemStack j) {
		boolean b = false;
		ItemStack k = null;
		
		switch (i) {
		case 0: k = new ItemStack(Blocks.COBBLESTONE); 	break;
		case 1: k = new ItemStack(Blocks.STONE); 		break;
		case 2: k = new ItemStack(Blocks.SAND); 		break;
		case 3: k = new ItemStack(Blocks.GLASS); 		break;
		case 4: k = new ItemStack(Blocks.GRAVEL); 		break;
		case 5: k = new ItemStack(Items.FLINT); 		break;
		case 6: k = new ItemStack(Blocks.STONEBRICK); 	break;
		case 7: k = new ItemStack(Blocks.SANDSTONE); 	break;
		case 8: k = new ItemStack(Blocks.DIRT); 		break;
		}
		if (k != null && j != null) {
			if (k.getUnlocalizedName().equals(j.getUnlocalizedName())) {
				b = true;
			}
		}
		return b;
	}
	
	public boolean hasPower() {
		
		return (power.getEnergyStored() > 9*EnergyPerTick) ? true : false;
	}
	
	@Override
	public void update() {
		if (worldObj.isRemote) {
			return;
		}
		
		if (power.getEnergyStored() >= EnergyPerTick && ticksSinceEvent < 100 && !allZero()) {
			ticksSinceEvent++;
			power.setEnergyStored((power.getEnergyStored() - EnergyPerTick), false);
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
		
		IBlockState state = worldObj.getBlockState(getPos());
		worldObj.notifyBlockUpdate(getPos(), state, state, 3);
		this.markDirty();
		return;
	}
	
	//Passes in index of increment amount to look up, uses it to set stacks
	public void generate(int offset) {
		int make = produceAmount[offset];
		
		if (make >= 192) {
			contents[(4*offset+3)] = generateHelper(offset, 64);
			make -= 64;
		}
		
		if (make >= 128) {
			contents[(4*offset+2)] = generateHelper(offset, 64);
			make -= 64;
		}
		
		if (make >= 64) {
			contents[(4*offset+1)] = generateHelper(offset, 64);
			make -= 64;
		}
		
		if (make > 0) {
			contents[(4*offset)] = generateHelper(offset, make);
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
		default: return null;
		}
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
	
	private void readSyncableDataFromNBT(NBTTagCompound msgNBT) {
		power.setEnergyStored(msgNBT.getInteger("power"), false);
		ticksSinceEvent = msgNBT.getInteger("Progress");
		upgradeLevel = msgNBT.getInteger("Tier");
		produceAmount = msgNBT.getIntArray("prodAmt");
	}
	
	public void writeSyncableDataToNBT(NBTTagCompound msgNBT) {
		msgNBT.setInteger("power", power.getEnergyStored());
		msgNBT.setInteger("Progress", ticksSinceEvent);
		msgNBT.setInteger("Tier", upgradeLevel);
		msgNBT.setIntArray("prodAmt", produceAmount);
	}
	
	public void receieveMessageFromClient(NBTTagCompound msgNBT) {
		if(msgNBT.hasKey("index") && msgNBT.hasKey("amount")) {
			int index = msgNBT.getInteger("index");
			int amount = msgNBT.getInteger("amount");
			setProduction(index, amount);
			this.markDirty();
		} else {
			System.out.println("recieveMessageFromClient acted dumb");
		}
	}
	
	public int getProgress() {
		return ticksSinceEvent;
	}

	@Override
	public boolean canExtract() {
		return false;
	}

	@Override
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
		
		if (capability == CapabilityEnergy.ENERGY) {
			return true;
		}
		
		return super.hasCapability(capability, facing);
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (capability == CapabilityEnergy.ENERGY) {
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
}