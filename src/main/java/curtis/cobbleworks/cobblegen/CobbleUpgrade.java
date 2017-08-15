package curtis.cobbleworks.cobblegen;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import curtis.cobbleworks.Cobbleworks;
import curtis.cobbleworks.CommonProxy;

public class CobbleUpgrade extends Item {
	
	public CobbleUpgrade() {
		this.setMaxStackSize(1);
		this.setHasSubtypes(true);
		this.setUnlocalizedName(Cobbleworks.MODID + ".upgrade");
		this.setRegistryName("cobbleUpgrade");
		this.setCreativeTab(CommonProxy.tabcobbleworks);
		//GameRegistry.register(this);
	}
	
	@SideOnly(Side.CLIENT)
	public void initModel() {
		ModelResourceLocation m1 = new ModelResourceLocation(getRegistryName() + "1", "inventory");
		ModelResourceLocation m2 = new ModelResourceLocation(getRegistryName() + "2", "inventory");
		ModelResourceLocation m3 = new ModelResourceLocation(getRegistryName() + "3", "inventory");
		ModelResourceLocation m4 = new ModelResourceLocation(getRegistryName() + "4", "inventory");
		ModelResourceLocation m5 = new ModelResourceLocation(getRegistryName() + "5", "inventory");
		
		ModelBakery.registerItemVariants(this, m1, m2, m3, m4, m5);
		
		ModelLoader.setCustomMeshDefinition(this, stack -> {
			switch(this.getMetadata(stack)) {
			case 0: return m1;
			case 1: return m2;
			case 2: return m3;
			case 3: return m4;
			case 4: return m5;
			}
			return null;
		});
	}
	
	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> subItems) {
		
		if (isInCreativeTab(CommonProxy.tabcobbleworks) && (tab == CommonProxy.tabcobbleworks)) {
			subItems.add(new ItemStack(CommonProxy.up1, 1, 0));
			subItems.add(new ItemStack(CommonProxy.up1, 1, 1));
			subItems.add(new ItemStack(CommonProxy.up1, 1, 2));
			subItems.add(new ItemStack(CommonProxy.up1, 1, 3));
			subItems.add(new ItemStack(CommonProxy.up1, 1, 4));
		}
	}
	
	@Override
	public void addInformation(ItemStack is, @Nullable World worldIn, List<String> list, ITooltipFlag flagIn) {
		int w = is.getItemDamage();
		int q = 1 + is.getItemDamage();
		list.add("Upgrades a Cobbleworks machine to tier " + q + ".");
		list.add("Increased power consumption per tick.");
		if (is.getItemDamage() <= 2) {
			list.add("Enables production of new items.");
		}
		list.add("Use on a cobblegen of Tier " + w + " to upgrade it to Tier " + q + ".");
	}
	
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		
		TileEntity te = worldIn.getTileEntity(pos);
		ItemStack stack = player.getHeldItem(hand);
		
		if (stack == null) {
			return EnumActionResult.PASS;
		}
		
		int level = stack.getItemDamage() + 1;
		if (te instanceof TileEntityCobblegen) {
			TileEntityCobblegen u = (TileEntityCobblegen)te;
			
			if (u.getLevel() == (level-1)) {
				u.setLevel(level);
				stack.setCount(stack.getCount() - 1);;
				//System.out.println("Upgraded a cobblegen block to level " + level);
				return EnumActionResult.SUCCESS;
			}
		} 
		
		return EnumActionResult.PASS;
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack) {
		
		return super.getUnlocalizedName() + stack.getItemDamage();
	}
}
