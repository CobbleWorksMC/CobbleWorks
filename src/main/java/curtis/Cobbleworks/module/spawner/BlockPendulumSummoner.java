package curtis.Cobbleworks.module.spawner;

import javax.annotation.Nullable;

import curtis.Cobbleworks.Cobbleworks;
import curtis.Cobbleworks.CommonProxy;
import curtis.Cobbleworks.module.cobblegen.GuiProxy;
import curtis.Cobbleworks.module.cobblegen.TileEntityCobbleGen;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockPendulumSummoner extends BlockContainer {

	public BlockPendulumSummoner() {
		super(Material.GLASS);
		this.setUnlocalizedName(Cobbleworks.MODID + ".pendulumSummoner");
		this.setRegistryName("pendulumSummoner");
		this.setHardness(5.0F);
		this.setResistance(50.0F);
		this.setLightLevel(1.0F);
		this.setHarvestLevel("pickaxe", 2);
		this.setCreativeTab(CommonProxy.tabcobbleworks);
		
		GameRegistry.register(this);
		GameRegistry.register(new ItemBlock(this), this.getRegistryName());
		GameRegistry.registerTileEntity(TileEntityPendulumSummoner.class, Cobbleworks.MODID + "_pendulumSummoner");
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
		
		if (worldIn.isRemote) {
			return true;
		}
		
		TileEntity te = worldIn.getTileEntity(pos);
		if (te instanceof TileEntityPendulumSummoner) {
			playerIn.openGui(Cobbleworks.MODID, GuiProxy.GUI_ID_SPAWNER, worldIn, pos.getX(), pos.getY(), pos.getZ());
			return true;
		}
		
		return false;
	}
	
	@SideOnly(Side.CLIENT)
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityPendulumSummoner.class, new PendulumSummonerTESR());
    }
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityPendulumSummoner();
	}
	
	@Override
    public boolean isNormalCube(IBlockState state, IBlockAccess world, BlockPos pos) {
		return false;
	}

    @Override
    public boolean isOpaqueCube(IBlockState state) {
    	return false;
	}

    @Override
    public boolean isFullCube(IBlockState state) {
    	return false;
    }

    @Override
    public boolean isVisuallyOpaque() {
    	return false;
	}
    
    @Override
	public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
		
        return super.onBlockPlaced(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer);
    }
    
    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
    	
    	TileEntity te = worldIn.getTileEntity(pos);
    	if (te instanceof TileEntityPendulumSummoner) {
    		InventoryHelper.dropInventoryItems(worldIn, pos, (TileEntityPendulumSummoner)te);
    	}
    	
    	super.breakBlock(worldIn, pos, state);
    }
}
