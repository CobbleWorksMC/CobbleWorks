package curtis.Cobbleworks.module.tool;

import javax.annotation.Nullable;

import curtis.Cobbleworks.Cobbleworks;
import curtis.Cobbleworks.CommonProxy;
import curtis.Cobbleworks.module.cobblegen.GuiProxy;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFurnace;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/*
 * much of this code is copied from TileEntityFurnace
 * */

public class PocketFurnace extends Item implements IInventory {
	
	ItemStack reference;
	
	//equal to currentItemBurnTime
	private int maxBurnTime = 0;
	//equal to furnaceBurnTime
	private int burnTime = 0;
	private int maxCookTime = 0;
	private int cookTime = 0;
	
	private ItemStack[] inventory = new ItemStack[3];
	
	private String customName;
	
	public PocketFurnace() {
		this.setUnlocalizedName(Cobbleworks.MODID + ".pocketFurnace");
		this.setRegistryName("pocketFurnace");
		this.setCreativeTab(CommonProxy.tabcobbleworks);
		this.setMaxStackSize(1);
		this.initModel();
		GameRegistry.register(this);
	}
	
	public PocketFurnace(ItemStack is) {
		this.reference = is;
		
		readFromNBT(is.getTagCompound());
	}
	
	@Override
	public int getMaxItemUseDuration(ItemStack stack) {
		return 1;
	}
	
	@SideOnly(Side.CLIENT)
	public void initModel() {
		ModelResourceLocation model = new ModelResourceLocation(getRegistryName(), "inventory");
		ModelLoader.setCustomModelResourceLocation(this, 0, model);
	}
	
	public int getBurningTime() {
		return burnTime;
	}
	
	public int getMaxBurningTime() {
		return maxBurnTime;
	}
	
	public int getCookingTime() {
		return cookTime;
	}
	
	public int getMaxCookingTime() {
		return maxCookTime;
	}
	
	@Override
	public void onCreated(ItemStack stack, World worldIn, EntityPlayer playerIn) {
		if (!stack.hasTagCompound()) {
			stack.setTagCompound(new NBTTagCompound());
			stack.getTagCompound().setInteger("maxBurnTime", 0);
			stack.getTagCompound().setInteger("burnTime", 0);
			stack.getTagCompound().setInteger("maxCookTime", 0);
			stack.getTagCompound().setInteger("cookTime", 0);
			for (int i = 0; i < 3; i++) {
				inventory[i].writeToNBT(stack.getTagCompound());
			}
		}
	}

	@Override
	public String getName() {
		return this.hasCustomName() ? this.customName : "Pocket Furnace";
	}

	@Override
	public boolean hasCustomName() {
		return customName == null ? true : false;
	}

	@Override
	public int getSizeInventory() {
		return 3;
	}

	@Override
	public ItemStack getStackInSlot(int index) {
	    if (index < 0 || index >= this.getSizeInventory())
	        return null;
	    return this.inventory[index];
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
		return ItemStackHelper.getAndRemove(this.inventory, index);
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
        boolean flag = stack != null && stack.isItemEqual(this.inventory[index]) && ItemStack.areItemStackTagsEqual(stack, this.inventory[index]);
        this.inventory[index] = stack;

        if (stack != null && stack.stackSize > this.getInventoryStackLimit()) {
            stack.stackSize = this.getInventoryStackLimit();
        }

        if (index == 0 && !flag) {
            this.maxCookTime = this.getCookTime(stack);
            this.cookTime = 0;
            this.markDirty();
        }
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public void markDirty() {
		for (int qwer = 0; qwer < this.getSizeInventory(); qwer++) {
			if (getStackInSlot(qwer) == null) {
				inventory[qwer] = null;
			}
			if (getStackInSlot(qwer) != null) {
				if (getStackInSlot(qwer).stackSize == 0) {
					inventory[qwer] = null;
				}
			}
		}
		writeToNBT(reference.getTagCompound());
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		return true;
	}

	@Override
	public void openInventory(EntityPlayer player) {}

	@Override
	public void closeInventory(EntityPlayer player) {}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		return false;
	}

	public int getField(int id) {
        switch (id) {
            case 0: return this.burnTime;
            case 1: return this.maxBurnTime;
            case 2: return this.cookTime;
            case 3: return this.maxCookTime;
            default: return 0;
        }
    }

	@Override
	public void setField(int id, int value) {
        switch (id) {
            case 0: this.burnTime = value; 		break;
            case 1: this.maxBurnTime = value; 	break;
            case 2: this.cookTime = value; 		break;
            case 3: this.maxCookTime = value;	break;
        }
    }

	@Override
	public int getFieldCount() {
		return 4;
	}

	public void clear() {
	    for (int i = 0; i < this.getSizeInventory(); i++)
	        setInventorySlotContents(i, null);
	    
	}

	@Override
	public ITextComponent getDisplayName() {
		return this.hasCustomName() ? new TextComponentString(this.customName) : new TextComponentString("Pocket Furnace");
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer caster, EnumHand hand) {
		if (worldIn.isRemote) {
			return new ActionResult(EnumActionResult.PASS, itemStackIn);
		}
		
		if (caster instanceof FakePlayer) {
			return new ActionResult(EnumActionResult.FAIL, itemStackIn);
		}
		
		if (!itemStackIn.hasTagCompound()) {
			return new ActionResult(EnumActionResult.FAIL, itemStackIn);
		}
		
		if (!caster.isSneaking()) {
			caster.openGui(Cobbleworks.MODID, GuiProxy.GUI_ID_POCKET_FURNACE, worldIn, 0, 0, 0);
		}
		
		return super.onItemRightClick(itemStackIn, worldIn, caster, hand);
	}
	
	public void readFromNBT(NBTTagCompound nbt) {
		this.maxBurnTime = nbt.getInteger("maxBurnTime");
		this.burnTime = nbt.getInteger("burnTime");
		this.maxCookTime = nbt.getInteger("maxCookTime");
		this.cookTime = nbt.getInteger("cookTime");
		NBTTagList nbttaglist = nbt.getTagList("Items", 10);
		for (int i = 0; i < nbttaglist.tagCount(); ++i) {
			NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
			int j = nbttagcompound1.getByte("Slot1") & 255;

			if (j >= 0 && j < inventory.length)
			{
				this.inventory[j] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
			}
		}
	}
	
	public void writeToNBT(NBTTagCompound nbt) {
		nbt.setInteger("maxBurnTime", 0);
		nbt.setInteger("burnTime", 0);
		nbt.setInteger("maxCookTime", 0);
		nbt.setInteger("cookTime", 0);
		NBTTagList nbttaglist = new NBTTagList();
		for (int i = 0; i < inventory.length; ++i) {
			if (this.inventory[i] != null) {
				NBTTagCompound nbttagcompound1 = new NBTTagCompound();
				nbttagcompound1.setByte("Slot1", (byte)i);
				inventory[i].writeToNBT(nbttagcompound1);
				nbttaglist.appendTag(nbttagcompound1);
			}
		}
		nbt.setTag("Items", nbttaglist);
	}
	
	/**
     * Furnace isBurning
     */
    public boolean isBurning() {
        return this.burnTime > 0;
    }
	
	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		//System.out.println("is updating?");
		//if (worldIn.isRemote) {
		//	return;
		//}
		
		if (!stack.hasTagCompound()) {
			stack.setTagCompound(new NBTTagCompound());
		}
		
		//this.readFromNBT(stack.getTagCompound());
		/*
		if (!this.isBurning() && this.canSmelt()) {
			if (this.inventory[1] != null) {
				System.out.println("burning item");
				this.burnTime = getItemBurnTime(this.inventory[1]);
                this.maxBurnTime = this.burnTime;
                this.inventory[1].stackSize--;
			}
		}
		
		if (this.isBurning() && this.canSmelt()) {
			System.out.println("incrementing cook time");
			this.cookTime++;
		}
		
		if (this.cookTime == 200 && this.canSmelt()) {
			System.out.println("smelting item");
			this.smeltItem();
			this.cookTime = 0;
		} 
		
		if (!this.canSmelt() && this.cookTime > 0) {
			System.out.println("setting cook time to zero");
			this.cookTime = 0;
		}
		
		if (this.isBurning()) {
			this.burnTime--;
			System.out.println("The flames will rise!");
		}
		
		this.writeToNBT(stack.getTagCompound());
		*/
		
		boolean flag = this.isBurning();
        boolean flag1 = false;
		
		if (this.isBurning() || this.inventory[1] != null && this.inventory[0] != null) {
			System.out.println("going into the logic");
            if (!this.isBurning() && this.canSmelt()) {
            	System.out.println("was not burning but can smelt");
                this.burnTime = getItemBurnTime(this.inventory[1]);
                this.maxBurnTime = this.burnTime;

                if (this.isBurning()) {
                	System.out.println("is now burning. setting flag to true");
                	flag1 = true;
                	
                    if (this.inventory[1] != null) {
                    	System.out.println("decrementing fuel");
                        --this.inventory[1].stackSize;

                        if (this.inventory[1].stackSize == 0) {
                        	System.out.println("something had a stacksize of zero");
                            this.inventory[1] = inventory[1].getItem().getContainerItem(inventory[1]);
                        }
                    }
                }
            }
            
            if (this.isBurning() && this.canSmelt()) {
            	System.out.println("incrementing cook time");
                ++this.cookTime;

                if (this.cookTime == this.maxCookTime) {
                	System.out.println("smelting item");
                    this.cookTime = 0;
                    this.maxCookTime = this.getCookTime(this.inventory[0]);
                    this.smeltItem();
                    flag1 = true;
                } else {
                	System.out.println("setting cook time to zero");
                    this.cookTime = 0;
                }
            } else if (!this.isBurning() && this.cookTime > 0) {
            	System.out.println("doing a math for something to do with cookTime");
                this.cookTime = MathHelper.clamp_int(this.cookTime - 2, 0, this.maxCookTime);
            }

            if (flag != this.isBurning()) {
            	System.out.println("marking flag1 true");
                flag1 = true;
            }
        }
        if (flag1) {
        	System.out.println("marking dirty");
        	this.writeToNBT(stack.getTagCompound());
        }
    }
	
	public int getCookTime(@Nullable ItemStack stack) {
        return 200;
    }

    /**
     * Returns true if the furnace can smelt an item, i.e. has a source item, destination stack isn't full, etc.
     */
    private boolean canSmelt() {
        if (this.inventory[0] == null) {
            return false;
        } else {
            ItemStack itemstack = FurnaceRecipes.instance().getSmeltingResult(this.inventory[0]);
            if (itemstack == null) return false;
            if (this.inventory[2] == null) return true;
            if (!this.inventory[2].isItemEqual(itemstack)) return false;
            int result = inventory[2].stackSize + itemstack.stackSize;
            return result <= getInventoryStackLimit() && result <= this.inventory[2].getMaxStackSize(); //Forge BugFix: Make it respect stack sizes properly.
        }
    }

    /**
     * Turn one item from the furnace source stack into the appropriate smelted item in the furnace result stack
     */
    public void smeltItem() {
        if (this.canSmelt())  {
            ItemStack itemstack = FurnaceRecipes.instance().getSmeltingResult(this.inventory[0]);

            if (this.inventory[2] == null) {
                this.inventory[2] = itemstack.copy();
            }
            else if (this.inventory[2].getItem() == itemstack.getItem()) {
                this.inventory[2].stackSize += itemstack.stackSize; // Forge BugFix: Results may have multiple items
            }

            if (this.inventory[0].getItem() == Item.getItemFromBlock(Blocks.SPONGE) && this.inventory[0].getMetadata() == 1 && this.inventory[1] != null && this.inventory[1].getItem() == Items.BUCKET) {
                this.inventory[1] = new ItemStack(Items.WATER_BUCKET);
            }

            --this.inventory[0].stackSize;

            if (this.inventory[0].stackSize <= 0) {
                this.inventory[0] = null;
            }
        }
    }
    
    public static int getItemBurnTime(ItemStack stack) {
        if (stack == null) {
            return 0;
        } else {
            Item item = stack.getItem();

            if (item instanceof ItemBlock && Block.getBlockFromItem(item) != Blocks.AIR)  {
                Block block = Block.getBlockFromItem(item);

                if (block == Blocks.WOODEN_SLAB) {
                    return 150;
                }

                if (block.getDefaultState().getMaterial() == Material.WOOD) {
                    return 300;
                }

                if (block == Blocks.COAL_BLOCK)  {
                    return 16000;
                }
            }

            if (item instanceof ItemTool && "WOOD".equals(((ItemTool)item).getToolMaterialName())) return 200;
            if (item instanceof ItemSword && "WOOD".equals(((ItemSword)item).getToolMaterialName())) return 200;
            if (item instanceof ItemHoe && "WOOD".equals(((ItemHoe)item).getMaterialName())) return 200;
            if (item == Items.STICK) return 100;
            if (item == Items.COAL) return 1600;
            if (item == Items.LAVA_BUCKET) return 20000;
            if (item == Item.getItemFromBlock(Blocks.SAPLING)) return 100;
            if (item == Items.BLAZE_ROD) return 2400;
            return net.minecraftforge.fml.common.registry.GameRegistry.getFuelValue(stack);
        }
    }
}