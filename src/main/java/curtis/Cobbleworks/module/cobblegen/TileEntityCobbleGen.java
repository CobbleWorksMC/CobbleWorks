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
    private static final int[] tierRequired = {0, 1, 1, 1, 2, 2, 2, 3, 3};
	//public static final int SIZE = 9;
    private int incrementAmount = 1;
    public customEnergyStorage power = new customEnergyStorage(250000);
    private int EnergyPerTick = 0;
    private boolean[] enabled = {true, false, false, false, false, false, false, false, false};
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
		return 9;
	}

	@Override
	public ItemStack getStackInSlot(int index) {
	    if (index < 0 || index >= this.getSizeInventory())
	        return null;
	    return this.contents[index];
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
			this.upgradeLevel = i;
			//System.out.println("Successfully upgraded CobbleGen to level: " + this.upgradeLevel);
			if (this.upgradeLevel > 5) {
				this.upgradeLevel = 5;
			}
			this.ticksSinceEvent = 0;
			switch (i){
				case 0: this.incrementAmount = 1;	this.EnergyPerTick = 0; 	break;
				case 1: {
					this.incrementAmount = 2;
					this.EnergyPerTick = 32;
					this.toggleProduction(1);
					this.toggleProduction(2);
					this.toggleProduction(3);
					break;
				}
				case 2: {
					this.incrementAmount = 8;
					this.EnergyPerTick = 64;
					this.toggleProduction(4);
					this.toggleProduction(5);
					this.toggleProduction(6);
					break;
				}
				
				case 3: {
					this.incrementAmount = 16;
					this.EnergyPerTick = 128;
					this.toggleProduction(7);
					this.toggleProduction(8);
					break;
				}
				case 4: this.incrementAmount = 32;	this.EnergyPerTick = 256; 	break;
				case 5: this.incrementAmount = 64; 	this.EnergyPerTick = 512;	break;
			}
		}
	}
	public int getLevel() {
		return this.upgradeLevel;
	}
	
	public int getTierRequired(int i) {
		return this.tierRequired[i];
	}
	
	@Override
	public void readFromNBT(NBTTagCompound q)
	{
		super.readFromNBT(q);
		
		NBTTagList nbttaglist = q.getTagList("Items", 10);
		this.power.setEnergyStored(q.getInteger("RF"), false);
		this.contents = new ItemStack[this.getSizeInventory()];
		this.incrementAmount = q.getInteger("IncrementAmount");
		this.EnergyPerTick = q.getInteger("RF/t");
		this.ticksSinceEvent = q.getInteger("Time");
		//this.storage = q.getIntArray("storage");
		this.upgradeLevel = q.getInteger("upgradeLevel");
		//System.out.println("Power retrieved: " + q.getInteger("RF") + " RF is now " + this.power.getEnergyStored());
		
		for(int j = 0; j < 9; j++) {
			this.enabled[j] = q.getBoolean("Enable" + j);
		}
		
		if (q.hasKey("CustomName", 8))
		{
			this.customName = q.getString("CustomName");
		}

		for (int i = 0; i < nbttaglist.tagCount(); ++i)
		{
			NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
			int j = nbttagcompound1.getByte("Slot1") & 255;

			if (j >= 0 && j < this.contents.length)
			{
				this.contents[j] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
			}
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound q) {
		
		super.writeToNBT(q);
		q.setInteger("RF", this.power.getEnergyStored());
		q.setInteger("RF/t", this.EnergyPerTick);
		q.setInteger("Time", this.ticksSinceEvent);
		q.setInteger("IncrementAmount", this.incrementAmount);
		q.setInteger("upgradeLevel", this.upgradeLevel);
		NBTTagList nbttaglist = new NBTTagList();
		
		for(int j = 0; j < 9; j++) {
			q.setBoolean("Enable" + j, this.enabled[j]);
		}

		for (int i = 0; i < this.contents.length; ++i) {
			
			if (this.contents[i] != null) {
				
				NBTTagCompound nbttagcompound1 = new NBTTagCompound();
				nbttagcompound1.setByte("Slot1", (byte)i);
				this.contents[i].writeToNBT(nbttagcompound1);
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
	        this.setInventorySlotContents(i, null);
	    
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
		for(int i = 0; i < 9; i++) {
			if(this.power.getEnergyStored() < (this.numEnabled * this.EnergyPerTick) || this.numEnabled == 0) {
				return false;
			}
		}
		return true;
	}

	public boolean canProduce() {
		boolean result = true;
		for(int i = 0; i < 9; i++) {
			if (contents[i] != null) {
				if (this.contents[i].stackSize == 64 && this.enabled[i]) {
					return false;
				}
				if (!this.checkStack(i, this.contents[i]) && this.enabled[i]) {
					return false;
				}
			}
		}
		return result;
	}
	
	@Override
	public void update() {
		if (worldObj.isRemote) {
			return;
		} else {
			if(this.hasPower() && this.ticksSinceEvent < 100){
				this.ticksSinceEvent++;
				for (int i = 0; i < this.getSizeInventory(); i++) {
					if (this.enabled[i]) {
						this.power.setEnergyStored(this.power.getEnergyStored()-this.EnergyPerTick, false);
					}
				}
			}
			
			if(this.ticksSinceEvent == 100 && this.canProduce()){
				this.ticksSinceEvent = 0;
				
				for (int i = 0; i < this.getSizeInventory(); i++) {
					if (this.enabled[i]) {
						if (this.contents[i] == null) {
							switch(i) {
							case 0: this.contents[i] = new ItemStack(Blocks.COBBLESTONE, this.incrementAmount);	break;
							case 1: this.contents[i] = new ItemStack(Blocks.STONE, 		 this.incrementAmount); break;
							case 2: this.contents[i] = new ItemStack(Blocks.SAND, 		 this.incrementAmount); break;
							case 3: this.contents[i] = new ItemStack(Blocks.GLASS, 		 this.incrementAmount);	break;
							case 4: this.contents[i] = new ItemStack(Blocks.GRAVEL, 	 this.incrementAmount); break;
							case 5: this.contents[i] = new ItemStack(Items.FLINT, 		 this.incrementAmount); break;
							case 6: this.contents[i] = new ItemStack(Blocks.STONEBRICK,  this.incrementAmount);	break;
							case 7: this.contents[i] = new ItemStack(Blocks.SANDSTONE, 	 this.incrementAmount); break;
							case 8: this.contents[i] = new ItemStack(Blocks.DIRT, 		 this.incrementAmount); break;
							}
						} else if (this.checkStack(i, contents[i])) {
							this.contents[i].stackSize += this.incrementAmount;
							if (this.contents[i].stackSize >= 64) {
								this.contents[i].stackSize = 64;
							}
						}
					}
				} 
			}
		}
		
		if (this.ticksSinceEvent > 100 || this.ticksSinceEvent < 0) {
			this.ticksSinceEvent = 0;
			System.out.println("Debug time!");
		}
		
		IBlockState state = worldObj.getBlockState(getPos());
		worldObj.notifyBlockUpdate(getPos(), state, state, 3);
		//worldObj.markBlockRangeForRenderUpdate(getPos(), getPos());
		this.markDirty();
		return;
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
	
	public void toggleProduction(int i) {
		if(this.enabled[i]) {
			this.numEnabled--;
			this.enabled[i] = false;
		} else if(!this.enabled[i] && (this.upgradeLevel >= this.tierRequired[i])) {
			this.numEnabled++;
			this.enabled[i] = true;
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
		//System.out.println("You are the 23059871920875th caller of the function onDataPacket()");
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
		this.power.setEnergyStored(msgNBT.getInteger("power"), false);
		this.ticksSinceEvent = msgNBT.getInteger("Progress");
		this.upgradeLevel = msgNBT.getInteger("Tier");
		for(int i = 0; i < 9; i++) {
			this.enabled[i] = msgNBT.getBoolean("Enabled" + i);
		}
	}
	
	public void writeSyncableDataToNBT(NBTTagCompound msgNBT) {
		msgNBT.setInteger("power", this.power.getEnergyStored());
		msgNBT.setInteger("Progress", this.ticksSinceEvent);
		msgNBT.setInteger("Tier", this.upgradeLevel);
		for(int i = 0; i < 9; i++) {
			msgNBT.setBoolean("Enabled" + i, this.enabled[i]);
		}
		
	}
	
	public void receieveMessageFromClient(NBTTagCompound msgNBT) {
		if(msgNBT.hasKey("Toggle")) {
			int toggle = msgNBT.getInteger("Toggle");
			this.toggleProduction(toggle);
			this.markDirty();
		}
		
	}
	
	public int getProgress() {
		return this.ticksSinceEvent;
	}
	
	public boolean getAbility(int i){
		return enabled[i];
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
}