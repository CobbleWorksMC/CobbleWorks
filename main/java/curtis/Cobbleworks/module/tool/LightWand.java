package curtis.Cobbleworks.module.tool;

import java.util.List;

import curtis.Cobbleworks.Cobbleworks;
import curtis.Cobbleworks.CommonProxy;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class LightWand extends Item {
	
	public LightWand() {
		this.setUnlocalizedName(Cobbleworks.MODID + ".lightWand");
		this.setRegistryName("lightWand");
		this.setCreativeTab(CommonProxy.tabcobbleworks);
		GameRegistry.register(this);
	}
	
	public void initModel() {
		ModelResourceLocation model = new ModelResourceLocation(getRegistryName(), "inventory");
		ModelLoader.setCustomModelResourceLocation(this, 0, model);
	}
	
	@Override
	public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
		return false;
	}
	
	@Override
	public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
		return slotChanged;
	}
	
	@SideOnly(Side.CLIENT)
	@Override
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
		
		String text = "Use to place invisible lights. You can simply punch them to get rid of them.";
		
		tooltip.add(text);
	}
	
	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer caster, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		
		if (worldIn.isRemote) {
			return EnumActionResult.PASS;
		}
		
		if (caster == null) {
			return EnumActionResult.FAIL;
		}
		
		if (caster instanceof FakePlayer) {
			return EnumActionResult.FAIL;
		}
		
		if (worldIn.getBlockState(pos).getBlock() != null) {
			
			BlockPos castPos = pos.add(facing.getDirectionVec());
			
			if (worldIn.getBlockState(castPos).getBlock() == Blocks.AIR) {
				
				Block light = CommonProxy.lightSource;
				IBlockState state = light.getDefaultState();
				worldIn.setBlockState(castPos, state);
			}
		}
		
		return EnumActionResult.SUCCESS;
	}
	
	
}
