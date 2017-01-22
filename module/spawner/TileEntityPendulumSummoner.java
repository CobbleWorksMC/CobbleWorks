package curtis.Cobbleworks.module.spawner;

import java.util.List;

import curtis.Cobbleworks.Cobbleworks;
import curtis.Cobbleworks.Config;
import curtis.Cobbleworks.module.spawner.ItemMobCard;
import curtis.Cobbleworks.module.spawner.MobRegistry;
import curtis.Cobbleworks.module.spawner.PendulumMob;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.SkeletonType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;

public class TileEntityPendulumSummoner extends TileEntity implements ITickable, IInventory {
	
	//Scale slots are 0 and 6, the rest are summon slots
	private ItemStack[] contents = new ItemStack[7];
	private String customName;
	//30 seconds per spawning cycle by default, configurable
	private int ticksSinceEvent = 0;
	
	@Override
	public String getName() {
		return customName == null ? "Pendulum Summoner" : customName;
	}

	@Override
	public boolean hasCustomName() {
		return customName == null ? false : true;
	}

	@Override
	public int getSizeInventory() {
		return 7;
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
	public ItemStack removeStackFromSlot(int index) {
		return ItemStackHelper.getAndRemove(this.contents, index);
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

	@Override
	public int getInventoryStackLimit() {
		return 1;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
    	return this.worldObj.getTileEntity(this.pos) != this ? false : player.getDistanceSq((double)this.pos.getX() + 0.5D, (double)this.pos.getY() + 0.5D, (double)this.pos.getZ() + 0.5D) <= 64.0D;
    }

	@Override
	public void openInventory(EntityPlayer player) {
	}

	@Override
	public void closeInventory(EntityPlayer player) {
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		return false;
	}

	@Override
	public int getField(int id) {
		return 0;
	}

	@Override
	public void setField(int id, int value) {
	}

	@Override
	public int getFieldCount() {
		return 0;
	}

	@Override
	public void clear() {
	    for (int i = 0; i < this.getSizeInventory(); i++)
	        setInventorySlotContents(i, null);
	}
	
	//Now for the actual meat of the logic
	
	@Override
	public void update() {
		if (worldObj.isRemote) {
			return;
		}
		
		this.ticksSinceEvent++;
		
		if (this.ticksSinceEvent >= Config.spawnerDelay) {
			this.ticksSinceEvent -= Config.spawnerDelay;
			this.pendulumSummon();
		}
		
		IBlockState state = worldObj.getBlockState(getPos());
		worldObj.notifyBlockUpdate(getPos(), state, state, 3);
		this.markDirty();
	}
	
	protected void pendulumSummon() {
		//List<EntityLivingBase> nearbyMobs;
		//nearbyMobs = this.worldObj.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(pos.getX()-4, pos.getY()-4, pos.getZ()-4, pos.getX()+4, pos.getY()+4, pos.getZ()+4));
		
		if (contents[0] == null || contents[6] == null) {
			return;
		}
		
		if (!(contents[0].getItem() instanceof ItemMobCard && contents[6].getItem() instanceof ItemMobCard)) {
			return;
		}
		
		ItemMobCard blueScale = (ItemMobCard) contents[0].getItem();
		ItemMobCard summon;
		ItemMobCard redScale = (ItemMobCard) contents[6].getItem();
		
		Entity elb;
		
		for (int i = 1; i < 6; i++) {
			if (contents[i] != null) {
				if (contents[i].getItem() instanceof ItemMobCard) {
					summon = (ItemMobCard) contents[i].getItem();
				
					if (isBetween(blueScale.getScale(contents[0]), summon.getLevel(contents[i]), redScale.getScale(contents[6]))) {
						
						elb = EntityList.createEntityByName(summon.getMob(contents[i]), worldObj);
						
						double x = Cobbleworks.rand.nextDouble() * 9 - 4.5d;
						double y = this.pos.getY();
						double z = Cobbleworks.rand.nextDouble() * 9 - 4.5d;
						
						x += this.pos.getX();
						z += this.pos.getZ();
						elb.setPosition(x, y, z);
						
						worldObj.spawnEntityInWorld(elb);
					}
				}
			}
		}
	}
	
	public boolean isBetween(int blue, int level, int red) {
		
		if (level > red && level < blue) {
			return true;
		} else if (level < red && level > blue) {
			return true;
		}
		
		return false;
	}
	
	//Data syncing stuff
	
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
	
	private void readSyncableDataFromNBT(NBTTagCompound q) {
		readFromNBT(q);
		/*
		this.ticksSinceEvent = q.getInteger("progress");
		if (q.hasKey("name")) {
			this.customName = q.getString("name");
		}
		
		NBTTagList nbttaglist = q.getTagList("Items", 10);
		for (int i = 0; i < nbttaglist.tagCount(); ++i) {
			NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
			int j = nbttagcompound1.getByte("Slot1") & 255;

			if (j >= 0 && j < contents.length)
			{
				this.contents[j] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
			}
		}*/
	}
	
	@Override
	public void readFromNBT(NBTTagCompound q) {

		super.readFromNBT(q);
		this.ticksSinceEvent = q.getInteger("progress");
		if (q.hasKey("name")) {
			this.customName = q.getString("name");
		}
		
		NBTTagList nbttaglist = q.getTagList("Items", 10);
		for (int i = 0; i < nbttaglist.tagCount(); ++i) {
			NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
			int j = nbttagcompound1.getByte("Slot1") & 255;

			if (j >= 0 && j < contents.length)
			{
				this.contents[j] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
			}
		}
	}
	
	public void writeSyncableDataToNBT(NBTTagCompound q) {
		writeToNBT(q);
		/*
		q.setInteger("progress", this.ticksSinceEvent);
		if (customName != null) {
			q.setString("name", this.customName);
		}
		
		NBTTagList nbttaglist = new NBTTagList();
		for (int i = 0; i < contents.length; ++i) {
			if (this.contents[i] != null) {
				NBTTagCompound nbttagcompound1 = new NBTTagCompound();
				nbttagcompound1.setByte("Slot1", (byte)i);
				contents[i].writeToNBT(nbttagcompound1);
				nbttaglist.appendTag(nbttagcompound1);
			}
		}
		
		q.setTag("Items", nbttaglist);*/
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound q) {
		
		super.writeToNBT(q);
		q.setInteger("progress", this.ticksSinceEvent);
		if (customName != null) {
			q.setString("name", this.customName);
		}
		
		NBTTagList nbttaglist = new NBTTagList();
		for (int i = 0; i < contents.length; ++i) {
			if (this.contents[i] != null) {
				NBTTagCompound nbttagcompound1 = new NBTTagCompound();
				nbttagcompound1.setByte("Slot1", (byte)i);
				contents[i].writeToNBT(nbttagcompound1);
				nbttaglist.appendTag(nbttagcompound1);
			}
		}
		
		q.setTag("Items", nbttaglist);
		
		return q;
	}
}
