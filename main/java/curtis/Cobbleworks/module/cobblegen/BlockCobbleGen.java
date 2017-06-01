package curtis.Cobbleworks.module.cobblegen;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

import javax.annotation.Nullable;

import curtis.Cobbleworks.Cobbleworks;
import curtis.Cobbleworks.CommonProxy;
import curtis.Cobbleworks.module.cobblegen.upgrades.CobbleUpgrade;

public class BlockCobbleGen extends BlockContainer {

	public BlockCobbleGen() {
		super(Material.ANVIL);
		this.setUnlocalizedName(Cobbleworks.MODID + ".cobblegen");
		this.setRegistryName("cobblegen");
		this.setHardness(5.0F);
		this.setResistance(50.0F);
		this.setLightLevel(1.0F);
		this.setHarvestLevel("pickaxe", 2);
		this.setCreativeTab(CommonProxy.tabcobbleworks);
		
		GameRegistry.register(this);
		GameRegistry.register(new ItemBlock(this), this.getRegistryName());
		GameRegistry.registerTileEntity(TileEntityCobbleGen.class, Cobbleworks.MODID + "_cobblegen");
	}
	
	@SideOnly(Side.CLIENT)
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
		TileEntity te = worldIn.getTileEntity(pos);
		if(worldIn.isRemote){
			return true;
		} else {
			if (te instanceof TileEntityCobbleGen) {
				playerIn.openGui(Cobbleworks.instance, GuiProxy.GUI_ID_COBBLEGEN, worldIn, pos.getX(), pos.getY(), pos.getZ());
				return true;
			}
		}
		return false;
	}
	
	@Override
	public TileEntity createNewTileEntity(World world, int i) {
		return new TileEntityCobbleGen();
	}
	
	@Override
	public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
		
        return super.onBlockPlaced(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer);
        
    }		
	
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		
		TileEntity tileentity = worldIn.getTileEntity(pos);
		
			if (tileentity instanceof TileEntityCobbleGen) {
				
				InventoryHelper.dropInventoryItems(worldIn, pos, (TileEntityCobbleGen)tileentity);
				
				if (((TileEntityCobbleGen) tileentity).getLevel() > 0) {
					worldIn.spawnEntityInWorld(new EntityItem(worldIn, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(CommonProxy.up1, 1, 0)));
				}
				
				if (((TileEntityCobbleGen) tileentity).getLevel() > 1) {
					worldIn.spawnEntityInWorld(new EntityItem(worldIn, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(CommonProxy.up1, 1, 1)));
				}
				
				if (((TileEntityCobbleGen) tileentity).getLevel() > 2) {
					worldIn.spawnEntityInWorld(new EntityItem(worldIn, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(CommonProxy.up1, 1, 2)));
				}
				
				if (((TileEntityCobbleGen) tileentity).getLevel() > 3) {
					worldIn.spawnEntityInWorld(new EntityItem(worldIn, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(CommonProxy.up1, 1, 3)));
				}
				
				if (((TileEntityCobbleGen) tileentity).getLevel() > 4) {
					worldIn.spawnEntityInWorld(new EntityItem(worldIn, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(CommonProxy.up1, 1, 4)));
				}
				
				worldIn.updateComparatorOutputLevel(pos, this);
			}
			
			super.breakBlock(worldIn, pos, state);
	}
	
	@Override
    public boolean isNormalCube(IBlockState state, IBlockAccess world, BlockPos pos) {
		return true;
	}

    @Override
    public boolean isOpaqueCube(IBlockState state) {
    	return true;
	}

    @Override
    public boolean isFullCube(IBlockState state) {
    	return true;
    }

    @Override
    public boolean isVisuallyOpaque() {
    	return true;
	}

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
    	return EnumBlockRenderType.MODEL;
	}
}


