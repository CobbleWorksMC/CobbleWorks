package curtis.Cobbleworks.module.farming;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import com.infinityraider.agricraft.api.soil.IAgriSoil;
import com.infinityraider.agricraft.api.util.FuzzyStack;

import curtis.Cobbleworks.Cobbleworks;
import curtis.Cobbleworks.CommonProxy;
import curtis.Cobbleworks.Config;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFarmland;
import net.minecraft.block.IGrowable;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockHydroponicSoil extends BlockFarmland /*implements IAgriSoil*/ {
	
	//For now, this block just uses the vanilla farmland textures, this will probably change in the future.
	
	public BlockHydroponicSoil() {
		super();
		this.setDefaultState(this.blockState.getBaseState().withProperty(MOISTURE, Integer.valueOf(7)));
		this.setUnlocalizedName(Cobbleworks.MODID + ".farmland");
		this.setRegistryName("farmland");
		this.setCreativeTab(CommonProxy.tabcobbleworks);
		this.setHardness(1.0f);
		this.setResistance(5.0f);
		this.setHarvestLevel("shovel", 0);
		
		GameRegistry.register(this);
		GameRegistry.register(new ItemBlock(this), this.getRegistryName());
	}
	
	@SideOnly(Side.CLIENT)
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }
	
	public boolean canSustainPlant(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing direction, net.minecraftforge.common.IPlantable plantable) {
		return true;
	}
	
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
		
		int i = ((Integer)state.getValue(MOISTURE)).intValue();
		
		if (i != 7) {
			worldIn.setBlockState(pos, state.withProperty(MOISTURE, Integer.valueOf(7)), 2);
		}
		
		//Learned how to do this from the Actually Additions worms
		if (Config.soilGrowChance > 0) {
			
			if (rand.nextInt(Config.soilGrowChance) == 0) {
				
				BlockPos pos2 = pos.up();
				
				if (!worldIn.isAirBlock(pos2)) {
					
					IBlockState state2 = worldIn.getBlockState(pos2);
					Block block = state2.getBlock();
					
					if (block instanceof IGrowable || block instanceof IPlantable) {
						
						block.updateTick(worldIn, pos2, state2, rand);
						block.updateTick(worldIn, pos2, state2, rand);
						block.updateTick(worldIn, pos2, state2, rand); //called this three times, for good measure
						
						IBlockState state3 = worldIn.getBlockState(pos2);
						
						if (state3.getBlock().getMetaFromState(state3) != state2.getBlock().getMetaFromState(state2)) {
							worldIn.playEvent(2005, pos2, 0);
						}
					}
				}
			}
		}
	}
	
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, ItemStack stack) {
		return this.getDefaultState();
	}
	
	public void onFallenUpon(World worldIn, BlockPos pos, Entity entityIn, float fallDistance) {
		
		entityIn.fall(fallDistance, 1.0F);
	}
	
	private boolean hasWater(World worldIn, BlockPos pos) {
		return true;
	}
	
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn) {
		
	}
	
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return Item.getItemFromBlock(this);
	}
	
	public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
		return new ItemStack(CommonProxy.farmland);
	}

	/*
	 * TODO: actually make soil compatible with agricraft crop sticks
	@Override
	public String getId() {
		return "cobbleworks:farmland";
	}

	@Override
	public String getName() {
		return "Hydroponic Soil";
	}

	@Override
	public Collection<FuzzyStack> getVarients() {
		List stacks = new ArrayList<FuzzyStack>();
		
		stacks.add(new FuzzyStack(new ItemStack(CommonProxy.farmland)));
		
		return stacks;
	}
	*/
}
