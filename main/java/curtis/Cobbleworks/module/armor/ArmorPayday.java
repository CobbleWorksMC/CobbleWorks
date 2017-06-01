package curtis.Cobbleworks.module.armor;

import java.util.List;

import curtis.Cobbleworks.Cobbleworks;
import curtis.Cobbleworks.CommonProxy;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ArmorPayday extends ItemArmor {
	
	public int dodgeChance;
	
	public ArmorPayday(EntityEquipmentSlot equipmentSlotIn, int renderIndexIn, int dodge) {
		
		super(CommonProxy.payday, renderIndexIn, equipmentSlotIn);
		
		this.dodgeChance = dodge;
		this.setUnlocalizedName(Cobbleworks.MODID + ".niceSuit" + equipmentSlotIn.getName());
		this.setRegistryName("niceSuit" + equipmentSlotIn.getName());
		this.setCreativeTab(CommonProxy.tabcobbleworks);
		GameRegistry.register(this);
	}
	
	@SideOnly(Side.CLIENT)
	public void initModel() {
		ModelResourceLocation model = new ModelResourceLocation(getRegistryName(), "inventory");
		ModelLoader.setCustomModelResourceLocation(this, 0, model);
	}
	
	@Override
	public final String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
		if (slot == EntityEquipmentSlot.LEGS) {
			return "cobbleworks:textures/armor/PAYDAY_layer_2.png";
		} else {
			return "cobbleworks:textures/armor/PAYDAY_layer_1.png";
		}
	}
	
	@Override
	public void onArmorTick(World world, EntityPlayer player, ItemStack stack) {
		
		if (world.isRemote) {
			return;
		}
		
		if (stack == null) {
			return;
		}
		
		if (stack.isItemDamaged()) {
			stack.setItemDamage(stack.getItemDamage() - 1);
		}
	}
	
	@SideOnly(Side.CLIENT)
	@Override
    public void addInformation(ItemStack stack, EntityPlayer caster, List<String> tooltip, boolean advanced) {
		tooltip.add("Adds a " + dodgeChance + "% chance to negate damage taken.");
	}
	
	@SubscribeEvent
	public void handleDamage(LivingHurtEvent e) {
		
		if (e.getEntity() == null) {
			return;
		}
		
		if (!(e.getEntity() instanceof EntityPlayer)) {
			return;
		}
		
		if (e.getEntity().worldObj.isRemote) {
			return;
		}
		
		Iterable<ItemStack> armor = ((EntityPlayer) e.getEntity()).getArmorInventoryList();
		
		int dodge = 0;
		
		for (ItemStack stack : armor) {
			if (stack != null) {
				if (stack.stackSize > 0) {
					if (stack.getItem() instanceof ArmorPayday) {
						dodge += ((ArmorPayday) stack.getItem()).dodgeChance;
					}
				}
			}
		}
		
		if (dodge > 0) {
			if (Cobbleworks.rand.nextInt(100) < dodge) {
				e.setCanceled(true);
			}
		}
	}
}
