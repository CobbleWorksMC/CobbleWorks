package curtis.Cobbleworks.module.farming;

import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

import com.infinityraider.agricraft.api.misc.IAgriHarvestable;
import com.mojang.authlib.GameProfile;

import curtis.Cobbleworks.Config;
import curtis.Cobbleworks.module.energy.customEnergyStorage;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockLog;
import net.minecraft.block.IGrowable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class TileHarvester extends TileEntity implements ITickable, IInventory, IEnergyStorage {
	
	public String customName;
	public ItemStack[] contents = new ItemStack[this.getSizeInventory()];
	public customEnergyStorage power = new customEnergyStorage(250000);
	public int idle = 0;
	public int radius = 3;
	public int harvestX, harvestY, harvestZ;
	public static final int max_radius = 9;
	public static final int rfCost = 200;
	public boolean firstTick = true;
	public boolean voidExtra = false;
	public boolean collectItems = true;
	public boolean logic = true;
	public boolean rightClick = true;
	public boolean tallPlant = true;
	public boolean tree = true;
	
	@Override
	public String getName() {
		return this.hasCustomName() ? this.customName : "Harvester";
	}

	@Override
	public boolean hasCustomName() {
		return this.customName == null ? false : true;
	}

	@Override
	public int receiveEnergy(int maxReceive, boolean simulate) {
		return power.receiveEnergy(maxReceive, simulate);
	}

	@Override
	public int extractEnergy(int maxExtract, boolean simulate) {
		return power.extractEnergy(maxExtract, simulate);
	}

	@Override
	public int getEnergyStored() {
		return power.getEnergyStored();
	}

	@Override
	public int getMaxEnergyStored() {
		return power.getMaxEnergyStored();
	}

	@Override
	public boolean canExtract() {
		return false;
	}

	@Override
	public boolean canReceive() {
		return true;
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

	@Override
	public int getSizeInventory() {
		return 18;
	}
	
	@Override
	public ItemStack getStackInSlot(int index) {
	    if (index < 0 || index >= this.getSizeInventory())
	        return null;
	    return this.contents[index];
	}
	
	@Override
	public ItemStack removeStackFromSlot(int index) {
		return ItemStackHelper.getAndRemove(this.contents, index);
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

	@Override
	public int getInventoryStackLimit() {
		return 64;
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
		return true;
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
		for (ItemStack stack : contents) {
			contents = null;
		}
	}

	@Override
	public void update() {
		
		if (worldObj.isRemote) {
			return;
		}
		
		if (firstTick) {
			firstTick = false;
			radius = 3;
			harvestX = pos.getX() + radius;
			harvestY = pos.getY();
			harvestZ = pos.getZ() + radius;
		}
		
		if (idle >= 10 && power.getEnergyStored() >= rfCost) {
			idle -= 10;
			
			Boolean powerCost = false;
			BlockPos harvest = new BlockPos(harvestX, harvestY, harvestZ);
			Block block = worldObj.getBlockState(harvest).getBlock();
			
			if (logic) {
				if (block instanceof BlockCrops) {
					if ((((BlockCrops)block).isMaxAge(worldObj.getBlockState(harvest)))) {
						List<ItemStack> drops = block.getDrops(worldObj, harvest, worldObj.getBlockState(harvest), 0);
						for (ItemStack drop : drops) {
							
							if (drop.getUnlocalizedName().contains("seed")) {
								--drop.stackSize;
							}
							
							ItemStack leftover = TileEntityHopper.putStackInInventoryAllSlots(this, drop, EnumFacing.UP); //LOL hopper code
							if (leftover != null) {
								if (leftover.stackSize > 0) {
									if (this.voidExtra) {
										worldObj.spawnEntityInWorld(new EntityItem(worldObj, harvestX, harvestY, harvestZ, leftover));
									}
								}
							}
						}
						
						worldObj.setBlockState(harvest, block.getDefaultState());
						powerCost = true;
					}
				}
			}
			
			if (rightClick) {
				TileEntity te = worldObj.getTileEntity(harvest);
				if (te != null) {
					if (te instanceof IAgriHarvestable) {
						if (((IAgriHarvestable) te).onHarvested(null)) {
							powerCost = true;
						}
					} 
				}
			}
			
			if (tallPlant) {
				BlockPos pos1 = new BlockPos(harvestX, harvestY + 1, harvestZ);
				
				if (block instanceof IPlantable && worldObj.getBlockState(pos1).getBlock() instanceof IPlantable) {
					
					worldObj.destroyBlock(pos1, true);
					powerCost = true;
				}
			}
			
			if (tree) {
				if (block instanceof BlockLog) {
					worldObj.destroyBlock(harvest, true);
					timber(harvest, worldObj.getBlockState(harvest), worldObj, 0);
					powerCost = true;
				}
			}
			
			if (harvestX > pos.getX() - radius) {
				--harvestX;
			} else if (harvestZ > pos.getZ() - radius) {
				harvestX = pos.getX() + radius;
				--harvestZ;
			} else {
				harvestX = pos.getX() + radius;
				harvestZ = pos.getZ() + radius;
				
				if (collectItems) {
					collectItems();
				}
			}
			
			if (powerCost) {
				power.setEnergyStored((int)(power.getEnergyStored() - rfCost), false);
			}
			
		}
		
		IBlockState state = worldObj.getBlockState(getPos());
		worldObj.notifyBlockUpdate(getPos(), state, state, 3);
		this.markDirty();
		
		++idle;
	}
	
	public void timber(BlockPos pos, IBlockState state, World worldIn, int i) {
		
		if (i > Config.maxRecursiveIterations || power.getEnergyStored() < rfCost) {
			return;
		}
		
		int j = i + 1;
		
		for (EnumFacing facing : new EnumFacing[] {EnumFacing.UP, EnumFacing.NORTH, EnumFacing.SOUTH, EnumFacing.WEST, EnumFacing.EAST}) {
			Block b1 = worldIn.getBlockState(pos.offset(facing)).getBlock();
			
			if (b1 instanceof BlockLog || b1 instanceof BlockLeaves) {
				worldIn.destroyBlock(pos.offset(facing), true);
				timber(pos.offset(facing), worldIn.getBlockState(pos.offset(facing)), worldIn, j);
				power.setEnergyStored((int)(power.getEnergyStored() - rfCost), false);
			}
		}
	}
	
	public void collectItems() {
		List<EntityItem> items = worldObj.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(pos.getX() - radius - 1, pos.getY() - 1, pos.getZ() - radius - 1, pos.getX() + radius + 1,  pos.getY() + 1,  pos.getZ() + radius + 1));
		
		for (EntityItem item : items) {
			ItemStack stack = item.getEntityItem();
			stack = TileEntityHopper.putStackInInventoryAllSlots(this, stack, EnumFacing.UP);
			
			if (!voidExtra) {
				
				if (stack == null) {
					item.setDead();
				} else if (stack.stackSize == 0) {
					item.setDead();
				} else {
					item.setEntityItemStack(stack);
				}
			} else {
				item.setDead();
			}
		}
	}
	
	public boolean canInteractWith(EntityPlayer playerIn) {
		return !isInvalid() && playerIn.getDistanceSq(pos.add(0.5D, 0.5D, 0.5D)) <= 64D;
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound q) {
		super.writeToNBT(q);
		
		q.setInteger("RF", power.getEnergyStored());
		q.setInteger("idle", idle);
		q.setInteger("X", harvestX);
		q.setInteger("Y", harvestY);
		q.setInteger("Z", harvestZ);
		q.setInteger("radius", radius);
		
		q.setBoolean("firstTick", firstTick);
		q.setBoolean("voidExtra", voidExtra);
		q.setBoolean("collectItems", collectItems);
		q.setBoolean("logic", logic);
		q.setBoolean("rightClick", rightClick);
		q.setBoolean("tallPlant", tallPlant);
		q.setBoolean("tree", tree);
		
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
		
		if (this.hasCustomName()) {
			q.setString("CustomName", this.customName);
		}
		
		return q;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound q) {
		super.readFromNBT(q);
		
		power.setEnergyStored(q.getInteger("RF"), false);
		idle = q.getInteger("idle");
		harvestX = q.getInteger("X");
		harvestY = q.getInteger("Y");
		harvestZ = q.getInteger("Z");
		radius = q.getInteger("radius");
		
		firstTick = q.getBoolean("firstTick");
		voidExtra = q.getBoolean("voidExtra");
		collectItems = q.getBoolean("collectItems");
		logic = q.getBoolean("logic");
		rightClick = q.getBoolean("rightClick");
		tallPlant = q.getBoolean("tallPlant");
		tree = q.getBoolean("tree");
		
		NBTTagList nbttaglist = q.getTagList("Items", 10);
		for (int i = 0; i < nbttaglist.tagCount(); ++i) {
			NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
			int j = nbttagcompound1.getByte("Slot1") & 255;

			if (j >= 0 && j < contents.length)
			{
				this.contents[j] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
			}
		}
		

		if (q.hasKey("CustomName", 8)) {
			customName = q.getString("CustomName");
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
		
		rightClick = msgNBT.getBoolean("rightClick");
		logic = msgNBT.getBoolean("logic");
		collectItems = msgNBT.getBoolean("collectItems");
		voidExtra = msgNBT.getBoolean("voidExtra");
		tallPlant = msgNBT.getBoolean("tallPlant");
		tree = msgNBT.getBoolean("tree");
		
		radius = msgNBT.getInteger("radius");
		power.setEnergyStored(msgNBT.getInteger("RF"), false);
		
		if (msgNBT.hasKey("customName")) {
			customName = msgNBT.getString("customName");
		}
		
		NBTTagList nbttaglist = msgNBT.getTagList("Items", 10);
		for (int i = 0; i < nbttaglist.tagCount(); ++i) {
			NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
			int j = nbttagcompound1.getByte("Slot1") & 255;

			if (j >= 0 && j < contents.length)
			{
				this.contents[j] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
			}
		}
	}
	
	protected void writeSyncableDataToNBT(NBTTagCompound msgNBT) {
		
		msgNBT.setBoolean("rightClick", rightClick);
		msgNBT.setBoolean("logic", logic);
		msgNBT.setBoolean("collectItems", collectItems);
		msgNBT.setBoolean("voidExtra", voidExtra);
		msgNBT.setBoolean("tallPlant", tallPlant);
		msgNBT.setBoolean("tree", tree);
		
		msgNBT.setInteger("radius", radius);
		msgNBT.setInteger("RF", power.getEnergyStored());
		
		if (customName != null) {
			if (customName != "") {
				msgNBT.setString("customName", customName);
			}
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
		
		msgNBT.setTag("Items", nbttaglist);
	}
	
	public void receiveMessageFromClient(NBTTagCompound msgNBT) {
		if (msgNBT.hasKey("b_id")) {
			
			int command = msgNBT.getInteger("b_id");
			//System.out.println("Command recieved: " + command);
			
			switch (command) {
			case 1: { if (radius < max_radius) { radius += 1; } break; }
			case 2: { if (radius > 1) { radius -= 1; } 			break; }
			case 3: { logic = !logic; 							break; }
			case 4: { rightClick = !rightClick; 				break; }
			case 5: { collectItems = !collectItems; 			break; }
			case 6: { voidExtra = !voidExtra; 					break; }
			case 7: { tallPlant = !tallPlant; 					break; }
			case 8: { tree = !tree; 							break; }
			}
		}
	}
}
