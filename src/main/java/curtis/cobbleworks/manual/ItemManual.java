package curtis.cobbleworks.manual;

import java.util.List;

import javax.annotation.Nullable;

import curtis.cobbleworks.Cobbleworks;
import curtis.cobbleworks.CommonProxy;
import curtis.cobbleworks.gui.GuiProxy;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemManual extends Item {

int majorPage = 0, minorPage = 0;
	
	public ItemManual() {
		this.setUnlocalizedName(Cobbleworks.MODID + ".guideBook");
		this.setRegistryName("guideBook");
		this.setCreativeTab(CommonProxy.tabcobbleworks);
		this.setMaxStackSize(1);
		//GameRegistry.register(this);
	}
	
	@SideOnly(Side.CLIENT)
	public void initModel() {
		ModelResourceLocation model = new ModelResourceLocation(getRegistryName(), "inventory");
		ModelLoader.setCustomModelResourceLocation(this, 0, model);
	}
	
	@Override
	public void onCreated(ItemStack stack, World worldIn, EntityPlayer caster) {
		if (!stack.hasTagCompound()) {
			stack.setTagCompound(new NBTTagCompound());
			stack.getTagCompound().setInteger("major", 0);
			stack.getTagCompound().setInteger("minor", 0);
		}
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
			stack.getTagCompound().setInteger("major", 0);
			stack.getTagCompound().setInteger("minor", 0);
		}
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
    public void addInformation(ItemStack is, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		tooltip.add("Contains everything you need to know about the mod.");
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer caster, EnumHand hand) {
		
		ItemStack is = caster.getHeldItem(hand);
		
		if (!is.hasTagCompound()) {
			return new ActionResult(EnumActionResult.FAIL, is);
		}
		
		if (worldIn.isRemote) {
			return new ActionResult(EnumActionResult.PASS, is);
		}
		
		if (caster instanceof FakePlayer) {
			return new ActionResult(EnumActionResult.FAIL, is);
		}
		
		caster.openGui(Cobbleworks.instance, GuiProxy.GUI_ID_BOOK, worldIn, 0, 0, 0);
		
		return new ActionResult(EnumActionResult.SUCCESS, is);
	}

}
