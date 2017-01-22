package curtis.Cobbleworks.module.tool;

import javax.annotation.Nullable;

import curtis.Cobbleworks.Cobbleworks;
import curtis.Cobbleworks.CommonProxy;
import curtis.Cobbleworks.module.cobblegen.GuiProxy;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFurnace;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
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
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PocketFurnace extends Item implements IInventory {
	
	public String customName;
	private ItemStack[] contents = new ItemStack[3];
	private int currentBurn = 0;
	private int maxBurn = 0;
	private int cookTime = 0;
	private int maxCookTime = 200;
	
	private boolean flag = false;
	
	public PocketFurnace() {
		this.setUnlocalizedName(Cobbleworks.MODID + ".pocketFurnace");
		this.setRegistryName("pocketFurnace");
		this.setCreativeTab(CommonProxy.tabcobbleworks);
		this.setMaxStackSize(1);
		GameRegistry.register(this);
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
	
	@Override
	public void onCreated(ItemStack stack, World worldIn, EntityPlayer playerIn) {
		super.onCreated(stack, worldIn, playerIn);
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World worldIn, EntityPlayer caster, EnumHand hand) {
		if (worldIn.isRemote) {
			return new ActionResult(EnumActionResult.PASS, stack);
		}
		
		if (caster instanceof FakePlayer) {
			return new ActionResult(EnumActionResult.FAIL, stack);
		}
		
		if (!stack.hasTagCompound()) {
			stack.setTagCompound(new NBTTagCompound());
		}
		
		caster.openGui(Cobbleworks.instance, GuiProxy.GUI_ID_POCKET_FURNACE, worldIn, 0, 0, 0);
		
		return super.onItemRightClick(stack, worldIn, caster, hand);
	}
	
	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity caster, int itemSlot, boolean isSelected) {
		if (worldIn.isRemote) {
			return;
		}
		
		if (caster instanceof FakePlayer) {
			return;
		}
		
		if (!stack.hasTagCompound()) {
			stack.setTagCompound(new NBTTagCompound());
		} else {
			this.readFromNBT(stack.getTagCompound());
		}
		
		//Perform all major furnace functions		
		
		//Consume fuel, if needed
		if (!(this.currentBurn > 0) && this.canSmelt()) {
			this.consumeFuel();
		}
		
		//Tick burn time, if powered
		if (this.currentBurn > 0) {
			--this.currentBurn;
		}
		//Tick cook time, if powered and can smelt
		if (this.currentBurn > 0 && this.canSmelt()) {
			++this.cookTime;
		}
		
		//Smelt item, if done, or set cook time to zero
		if (this.currentBurn > 0 && this.canSmelt())
        {
            ++this.cookTime;

            if (this.cookTime == this.maxCookTime)
            {
                this.cookTime = 0;
                this.maxCookTime = 200;
                this.smeltItem();
            }
        }
        else
        {
            this.cookTime = 0;
        }
		
		//And save changes
		this.writeToNBT(stack.getTagCompound());
	}
	
	private void consumeFuel() {
		if (this.contents[1] == null) {
			return;
		}
		this.currentBurn = TileEntityFurnace.getItemBurnTime(this.contents[1]);
		this.maxBurn = this.currentBurn;
		--this.contents[1].stackSize;
	}

	//copied from TileEntityFurnace, and slightly modified
	public void smeltItem()
    {
        if (this.canSmelt())
        {
            ItemStack itemstack = FurnaceRecipes.instance().getSmeltingResult(this.contents[0]);

            if (this.contents[2] == null)
            {
                this.contents[2] = itemstack.copy();
            }
            else if (this.contents[2].getItem() == itemstack.getItem())
            {
                this.contents[2].stackSize += itemstack.stackSize; // Forge BugFix: Results may have multiple items
            }

            if (this.contents[0].getItem() == Item.getItemFromBlock(Blocks.SPONGE) && this.contents[0].getMetadata() == 1 && this.contents[1] != null && this.contents[1].getItem() == Items.BUCKET)
            {
                this.contents[1] = new ItemStack(Items.WATER_BUCKET);
            }

            --this.contents[0].stackSize;

            if (this.contents[0].stackSize <= 0)
            {
                this.contents[0] = null;
            }
        }
    }
	
	//copied from TileEntityFurnace, and slightly modified
	public void readFromNBT(NBTTagCompound compound) {
		NBTTagList nbttaglist = compound.getTagList("Items", 10);
        this.contents = new ItemStack[this.getSizeInventory()];

        for (int i = 0; i < nbttaglist.tagCount(); ++i)
        {
            NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
            int j = nbttagcompound.getByte("Slot");

            if (j >= 0 && j < this.contents.length)
            {
                this.contents[j] = ItemStack.loadItemStackFromNBT(nbttagcompound);
            }
        }

        this.currentBurn = compound.getInteger("BurnTime");
        this.cookTime = compound.getInteger("CookTime");
        this.maxCookTime = compound.getInteger("CookTimeTotal");
        this.maxBurn = TileEntityFurnace.getItemBurnTime(this.contents[1]);
	}
	
	@Override
	public int getSizeInventory() {
		return 3;
	}
	
	//copied from TileEntityFurnace, and slightly modified
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound.setInteger("BurnTime", this.currentBurn);
        compound.setInteger("CookTime", this.cookTime);
        compound.setInteger("CookTimeTotal", this.maxCookTime);
        NBTTagList nbttaglist = new NBTTagList();

        for (int i = 0; i < this.contents.length; ++i)
        {
            if (this.contents[i] != null)
            {
                NBTTagCompound nbttagcompound = new NBTTagCompound();
                nbttagcompound.setByte("Slot", (byte)i);
                this.contents[i].writeToNBT(nbttagcompound);
                nbttaglist.appendTag(nbttagcompound);
            }
        }

        compound.setTag("Items", nbttaglist);
        
        return compound;
	}
	
	//Copied from TileEntityFurnace and slightly modified
	private boolean canSmelt()
    {
        if (this.contents[0] == null)
        {
            return false;
        }
        else
        {
            ItemStack itemstack = FurnaceRecipes.instance().getSmeltingResult(this.contents[0]);
            if (itemstack == null) return false;
            if (this.contents[2] == null) return true;
            if (!this.contents[2].isItemEqual(itemstack)) return false;
            int result = contents[2].stackSize + itemstack.stackSize;
            return result <= getInventoryStackLimit() && result <= this.contents[2].getMaxStackSize(); //Forge BugFix: Make it respect stack sizes properly.
        }
    }
	
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public String getName() {
		return "Pocket Furnace";
	}

	@Override
	public boolean hasCustomName() {
		return false;
	}

	@Override
	public ITextComponent getDisplayName() {
		return null;
	}

	@Override
	public ItemStack getStackInSlot(int index) {
		
		if (index < 3 && index >= 0) {
			return contents[index];
		}
		
		return null;
	}

	@Override
	public ItemStack decrStackSize(int index, int count) {
		return ItemStackHelper.getAndSplit(this.contents, index, count);
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		return ItemStackHelper.getAndRemove(this.contents, index);
	}

	@Override
	public void setInventorySlotContents(int index, @Nullable ItemStack stack) {
        boolean flag = stack != null && stack.isItemEqual(this.contents[index]) && ItemStack.areItemStackTagsEqual(stack, this.contents[index]);
        this.contents[index] = stack;

        if (stack != null && stack.stackSize > this.getInventoryStackLimit())
        {
            stack.stackSize = this.getInventoryStackLimit();
        }

        if (index == 0 && !flag)
        {
            this.maxCookTime = 200;
            this.cookTime = 0;
            this.markDirty();
        }
    }

	@Override
	public void markDirty() {
		this.flag = true;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		return true;
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

	public int getField(int id)
    {
        switch (id)
        {
            case 0:
                return this.currentBurn;
            case 1:
                return this.maxBurn;
            case 2:
                return this.cookTime;
            case 3:
                return this.maxCookTime;
            default:
                return 0;
        }
    }

    public void setField(int id, int value)
    {
        switch (id)
        {
            case 0:
                this.currentBurn = value;
                break;
            case 1:
                this.maxBurn = value;
                break;
            case 2:
                this.cookTime = value;
                break;
            case 3:
                this.maxCookTime = value;
        }
    }

    public int getFieldCount()
    {
        return 4;
    }

    public void clear()
    {
        for (int i = 0; i < this.contents.length; ++i)
        {
            this.contents[i] = null;
        }
    }
}