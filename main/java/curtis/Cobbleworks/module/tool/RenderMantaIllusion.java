package curtis.Cobbleworks.module.tool;

import java.util.Map;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.authlib.minecraft.MinecraftProfileTexture.Type;

import curtis.Cobbleworks.Config;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerArrow;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.client.renderer.entity.layers.LayerCape;
import net.minecraft.client.renderer.entity.layers.LayerCustomHead;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.client.resources.SkinManager;
import net.minecraft.util.ResourceLocation;

public class RenderMantaIllusion extends RenderBiped<EntityMantaIllusion> {
	
	private static final ModelPlayer STEVE = new ModelPlayer(0, false);
	private static final ModelPlayer ALEX = new ModelPlayer(0, true);
	private static final ResourceLocation TEXTURE_STEVE = new ResourceLocation("textures/entity/steve.png");
	private static final ResourceLocation TEXTURE_ALEX = new ResourceLocation("textures/entity/alex.png");
	
	public RenderMantaIllusion(RenderManager renderManagerIn) {
		super(renderManagerIn, STEVE, 0.5F, 1.0F);
	}
	
	@Override
	public ModelPlayer getMainModel() {
		return (ModelPlayer)super.getMainModel();
	}
	
	@Override
	public void doRender(EntityMantaIllusion e, double x, double y, double z, float eYaw, float partialTickTime) {
		setModel(e);
		//this.bindEntityTexture(e);
		super.doRender(e, x, y, z, eYaw, partialTickTime);
	}
	
	public void setModel(EntityMantaIllusion emi) {
		boolean isAlex = "ALEX".equals(Config.renderMantasAs);
		if (isAlex) {
			mainModel = ALEX;
			modelBipedMain = ALEX;
		} else {
			mainModel = STEVE;
			modelBipedMain = STEVE;
		}
	}
	
	@Override
	protected ResourceLocation getEntityTexture(EntityMantaIllusion e) {
		
		
		if ("ALEX".equals(Config.renderMantasAs)) {
			return TEXTURE_ALEX;
		} else {
			return TEXTURE_STEVE;
		}
		
	}
}
