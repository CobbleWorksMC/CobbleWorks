package curtis.cobbleworks.cobblegen;

import javax.annotation.Nullable;
import javax.swing.plaf.basic.BasicComboBoxUI.ItemHandler;

import curtis.cobbleworks.Cobbleworks;
import curtis.cobbleworks.CommonProxy;
import curtis.cobbleworks.gui.GuiProxy;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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
	}
	
	@SideOnly(Side.CLIENT)
    public void initModel() {
		//Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(Item.getItemFromBlock(this), 0, new ModelResourceLocation(Cobbleworks.MODID + ":" + getRegistryName(), "inventory"));
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(CommonProxy.cobblegen), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		TileEntity te = worldIn.getTileEntity(pos);
		if(worldIn.isRemote){
			return true;
		} else {
			if (te instanceof TileEntityCobblegen) {
				playerIn.openGui(Cobbleworks.instance, GuiProxy.GUI_ID_COBBLEGEN, worldIn, pos.getX(), pos.getY(), pos.getZ());
				return true;
			}
		}
		return false;
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityCobblegen();
	}
	
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		
		TileEntity tileentity = worldIn.getTileEntity(pos);
		
			if (tileentity instanceof TileEntityCobblegen) {
				
				NonNullList<ItemStack> inventory = ((TileEntityCobblegen) tileentity).getInv();
				
				for (int i = 0; i < inventory.size(); ++i) {
					ItemStack itemstack = inventory.get(i);
		            
		            if (!itemstack.isEmpty()) {
		            	worldIn.spawnEntity(new EntityItem(worldIn, pos.getX(), pos.getY(), pos.getZ(), itemstack));
		            }
				}
				
				if (((TileEntityCobblegen) tileentity).getLevel() > 0) {
					worldIn.spawnEntity(new EntityItem(worldIn, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(CommonProxy.up1, 1, 0)));
				}
				
				if (((TileEntityCobblegen) tileentity).getLevel() > 1) {
					worldIn.spawnEntity(new EntityItem(worldIn, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(CommonProxy.up1, 1, 1)));
				}
				
				if (((TileEntityCobblegen) tileentity).getLevel() > 2) {
					worldIn.spawnEntity(new EntityItem(worldIn, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(CommonProxy.up1, 1, 2)));
				}
				
				if (((TileEntityCobblegen) tileentity).getLevel() > 3) {
					worldIn.spawnEntity(new EntityItem(worldIn, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(CommonProxy.up1, 1, 3)));
				}
				
				if (((TileEntityCobblegen) tileentity).getLevel() > 4) {
					worldIn.spawnEntity(new EntityItem(worldIn, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(CommonProxy.up1, 1, 4)));
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
    public EnumBlockRenderType getRenderType(IBlockState state) {
    	return EnumBlockRenderType.MODEL;
	}
}
