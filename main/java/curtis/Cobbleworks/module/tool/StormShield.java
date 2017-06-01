package curtis.Cobbleworks.module.tool;

import java.util.List;

import curtis.Cobbleworks.Cobbleworks;
import curtis.Cobbleworks.CommonProxy;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class StormShield extends Item {
	
	public StormShield() {
		
		this.setUnlocalizedName(Cobbleworks.MODID + ".stormShield");
		this.setRegistryName("stormShield");
		this.setCreativeTab(CommonProxy.tabcobbleworks);
		this.setMaxStackSize(1);
		
		GameRegistry.register(this);
	}
	
	@SideOnly(Side.CLIENT)
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
    public void addInformation(ItemStack stack, EntityPlayer caster, List<String> tooltip, boolean advanced) {
		tooltip.add("Cannot manually block");
		tooltip.add("Ignores durability loss");
		tooltip.add("Damage taken reduced by 35%");
		tooltip.add("25% chance to automatically block");
	}
	
	@SubscribeEvent
	public void handleDamage(LivingHurtEvent e) {
		
		if (e.getEntity() == null) {
			return;
		}
		
		if (!(e.getEntity() instanceof EntityPlayer)) {
			return;
		}
		
		if (e.getEntityLiving().worldObj.isRemote) {
			return;
		}
		
		ItemStack stack = ((EntityPlayer) e.getEntity()).getHeldItemOffhand();
		
		if (stack == null) {
			return;
		}
		
		if (!(stack.getItem() instanceof StormShield)) {
			return;
		}
		
		int i = Cobbleworks.rand.nextInt(99);
		
		if (i < 25) {
			e.setCanceled(true);
			return;
		} else {
			float d = e.getAmount() * .65f;
			e.setAmount(d);
			return;
		}
		
	}
}
