package curtis.Cobbleworks.module.tool;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import curtis.Cobbleworks.Cobbleworks;
import curtis.Cobbleworks.CommonProxy;
import curtis.Cobbleworks.Config;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLog;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/*
 * This item's texture and all sound effects associated with it are taken from Dota 2.
 * */

public class SuperAxe extends ItemAxe {
	
	//private int cd = 0;
	private static final int maxCooldown = 900;

	public SuperAxe() {
		super(ToolMaterial.DIAMOND);
		//super(CommonProxy.materialStar); AHHHHHH! BUUUUUUUUUUUUUUUUUG!
		toolMaterial = CommonProxy.materialStar;
		damageVsEntity = 3.0F;
		attackSpeed = -1;
		this.setMaxDamage(CommonProxy.materialStar.getMaxUses());
		this.setUnlocalizedName(Cobbleworks.MODID + ".superAxe");
		this.setRegistryName("superAxe");
		this.setCreativeTab(CommonProxy.tabcobbleworks);
		GameRegistry.register(this);
	}
	
	public void initModel() {
		ModelResourceLocation model = new ModelResourceLocation(getRegistryName(), "inventory");
		ModelLoader.setCustomModelResourceLocation(this, 0, model);
	}
	
	@Override
	public void onCreated(ItemStack stack, World worldIn, EntityPlayer playerIn) {
		if (!stack.hasTagCompound()) {
			stack.setTagCompound(new NBTTagCompound());
			stack.getTagCompound().setInteger("cd", 0);
		}
	}

	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		
		if (worldIn.isRemote) {
			return;
		}
		
		if (!stack.hasTagCompound()) {
			stack.setTagCompound(new NBTTagCompound());
			stack.getTagCompound().setInteger("cd", 0);
		}
		
		int cd = stack.getTagCompound().getInteger("cd");
		
		if (cd > 0) {
			cd--;
		}
		//Sanity check
		if (cd < 0 || cd > SuperAxe.maxCooldown) {
			cd = SuperAxe.maxCooldown;
		}
		
		stack.getTagCompound().setInteger("cd", cd);
	}
	
	@Override
	public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
		return false;
	}
	
	@Override
	public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState state, BlockPos pos, EntityLivingBase user) {
		super.onBlockDestroyed(stack, worldIn, state, pos, user);
		if (worldIn.isRemote) {
			return true;
		}
		Block b = state.getBlock();
		if (b instanceof BlockLog) {
			timber(stack, worldIn, state, pos, user, 0);
		}
		return true;
	}
	
	public void timber(ItemStack stack, World worldIn, IBlockState state, BlockPos pos, EntityLivingBase user, int i) {
		if (i > Config.maxRecursiveIterations) {
			return;
		}
		int j = i+1;
		Block b = state.getBlock();
		for (EnumFacing facing : new EnumFacing[] {EnumFacing.DOWN, EnumFacing.UP, EnumFacing.NORTH, EnumFacing.SOUTH, EnumFacing.WEST, EnumFacing.EAST}) {
			Block b2 = worldIn.getBlockState(pos.offset(facing)).getBlock();
			if ((b2 instanceof BlockLog) && stack != null && stack.getItemDamage() < stack.getMaxDamage()) {
				worldIn.destroyBlock(pos.offset(facing), true);
				stack.damageItem(1, user);
				timber(stack, worldIn, state, pos.offset(facing), user, j);
			}
		}
		
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer caster, EnumHand hand) {
		
		if (!itemStackIn.hasTagCompound()) {
			return new ActionResult(EnumActionResult.FAIL, itemStackIn);
		}
		
		if (worldIn.isRemote) {
			return new ActionResult(EnumActionResult.PASS, itemStackIn);
		}
		
		if (caster instanceof FakePlayer) {
			return new ActionResult(EnumActionResult.FAIL, itemStackIn);
		}
		
		if (itemStackIn.getTagCompound().getInteger("cd")== 0) {
			itemStackIn.getTagCompound().setInteger("cd", SuperAxe.maxCooldown);
			worldIn.playSound(null, caster.getPosition(), CommonProxy.mantaSuccess, SoundCategory.PLAYERS, 0.5F, 1.0F);
			caster.clearActivePotions();
			spawnIllusions(worldIn, caster);
			
			return new ActionResult(EnumActionResult.SUCCESS, itemStackIn);
		} else {
			worldIn.playSound(null, caster.getPosition(), CommonProxy.cooldown, SoundCategory.PLAYERS, 0.5F, 1.0F);
			//System.out.println("You're still on cooldown for " + itemStackIn.getTagCompound().getInteger("cd") + " ticks.");
		}
		
		return new ActionResult(EnumActionResult.PASS, itemStackIn);
	}
	
	private void spawnIllusions(World worldIn, EntityPlayer caster) {
		
		BlockPos pos1 = caster.getPosition().offset(EnumFacing.NORTH);
		BlockPos pos2 = caster.getPosition().offset(EnumFacing.SOUTH);
		
		EntityMantaIllusion e1 = new EntityMantaIllusion(worldIn);
		e1.setPosition(pos1.getX(), pos1.getY(), pos1.getZ());
		e1.setOwner(caster);
		e1.setCustomNameTag(caster.getName());
		
		EntityMantaIllusion e2 = new EntityMantaIllusion(worldIn);
		e2.setPosition(pos2.getX(), pos2.getY(), pos2.getZ());
		e2.setOwner(caster);
		e2.setCustomNameTag(caster.getName());
		
		worldIn.spawnEntityInWorld(e1);
		worldIn.spawnEntityInWorld(e2);
	}

	@Override
	public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
		return slotChanged;
	}
	
	@SideOnly(Side.CLIENT)
	@Override
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
		if (stack.hasTagCompound()) {
			if (stack.getTagCompound().hasKey("cd")) {
				int cd = stack.getTagCompound().getInteger("cd");
				if (cd > 0) {
					tooltip.add("Cooldown remaining: " + cd + " ticks.");
				} else {
					tooltip.add("Ready to use. 45 second cooldown.");
				}
			}
		}
	}
	
	@Override
	public boolean shouldCauseBlockBreakReset(ItemStack oldStack, ItemStack newStack) {
		return false;
	}
}