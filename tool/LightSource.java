package curtis.Cobbleworks.module.tool;

import java.util.Random;

import javax.annotation.Nullable;

import curtis.Cobbleworks.Cobbleworks;
import curtis.Cobbleworks.module.spawner.TileEntityPendulumSummoner;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class LightSource extends Block {

	public LightSource() {
		super(Material.GLASS);
		this.setUnlocalizedName(Cobbleworks.MODID + ".lightSource");
		this.setRegistryName("lightSource");
		this.setHardness(0.0F);
		this.setResistance(0.0F);
		this.setLightLevel(1.0F);
		this.needsRandomTick = true;
		
		GameRegistry.register(this);
		GameRegistry.register(new ItemBlock(this), this.getRegistryName());
	}
	
	@SideOnly(Side.CLIENT)
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }
	
	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(IBlockState state, World worldObj, BlockPos pos, Random rand) {
		EntityPlayer player = Minecraft.getMinecraft().thePlayer;
		
		if (player != null) {
			if (player.getHeldItem(EnumHand.MAIN_HAND) != null) {
				if (player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof LightWand) {
					worldObj.spawnParticle(EnumParticleTypes.FLAME, pos.getX()+.5, pos.getY()+.5, pos.getZ()+.5, (rand.nextDouble() - 0.5D) / 10.0D, (rand.nextDouble() - 0.5D) / 10.0D, (rand.nextDouble() - 0.5D) / 10.0D, new int[0]);
					worldObj.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, pos.getX()+.5, pos.getY()+.5, pos.getZ()+.5, 0.0D, 0.0D, 0.0D, new int[0]);
				}
			}
		}
	}
	
	@Override
	public int quantityDropped(Random random) {
		return 0;
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
    public EnumBlockRenderType getRenderType(IBlockState state) {
    	return EnumBlockRenderType.INVISIBLE;
	}
    
    @Override
    public boolean isBlockSolid(IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
    	return false;
    }
    
    @Override
    public boolean canDropFromExplosion(Explosion explosionIn) {
    	return false;
    }
    
    @Nullable
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, World worldIn, BlockPos pos) {
    	return this.NULL_AABB;
    }
    
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
    	return new AxisAlignedBB(.3F, .3F, .3F, .7F, .7F, .7F);
    }
}
