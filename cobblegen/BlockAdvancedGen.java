package curtis.Cobbleworks.module.cobblegen;

import javax.annotation.Nullable;

import curtis.Cobbleworks.Cobbleworks;
import curtis.Cobbleworks.CommonProxy;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
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
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockAdvancedGen extends BlockContainer {

	public BlockAdvancedGen() {
		super(Material.ANVIL);
		this.setUnlocalizedName(Cobbleworks.MODID + ".advgen");
		this.setRegistryName("advgen");
		this.setHardness(5.0F);
		this.setResistance(50.0F);
		this.setLightLevel(1.0F);
		this.setHarvestLevel("pickaxe", 2);
		this.setCreativeTab(CommonProxy.tabcobbleworks);
		
		GameRegistry.register(this);
		GameRegistry.register(new ItemBlock(this), this.getRegistryName());
		GameRegistry.registerTileEntity(TileEntityAdvancedGen.class, Cobbleworks.MODID + "_advgen");
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityAdvancedGen();
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
			if (te instanceof TileEntityAdvancedGen) {
				playerIn.openGui(Cobbleworks.instance, GuiProxy.GUI_ID_COBBLEGEN, worldIn, pos.getX(), pos.getY(), pos.getZ());
				return true;
			}
		}
		return false;
	}
	
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {

        {
            TileEntity tileentity = worldIn.getTileEntity(pos);

            if (tileentity instanceof TileEntityCobbleGen)
            {
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
