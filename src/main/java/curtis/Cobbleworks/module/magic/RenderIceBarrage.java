package curtis.Cobbleworks.module.magic;

import org.lwjgl.opengl.GL11;

import curtis.Cobbleworks.Cobbleworks;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;

public class RenderIceBarrage extends Render<EntityIceBarrage> {

	public static final ResourceLocation ice = new ResourceLocation(Cobbleworks.MODID, "textures/blocks/ice.png");
	
	public RenderIceBarrage(RenderManager renderManager) {
		super(renderManager);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityIceBarrage entity) {
		return ice;
	}
	
	@Override
	public void setRenderOutlines(boolean b) {
		super.setRenderOutlines(false);
	}
	
	@Override
	public void doRender(EntityIceBarrage eib, double x, double y, double z, float entityYaw, float partialTicks) {
		super.doRender(eib, x, y, z, entityYaw, partialTicks);
		
		Tessellator t = Tessellator.getInstance();
		net.minecraft.client.renderer.VertexBuffer wr = t.getBuffer();
		
		GlStateManager.pushMatrix();
		GlStateManager.translate(x, y, z);
		//GlStateManager.enableBlend();
		
		wr.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		
		//GlStateManager.blendFunc(50-eib.ticksExisted, eib.ticksExisted);
		
		this.bindTexture(ice);
		//Side 0: down
		wr.pos(0, 0, 1).tex(0, 1).endVertex();
		wr.pos(0, 0, 0).tex(1, 1).endVertex();
		wr.pos(1, 0, 0).tex(1, 0).endVertex();
		wr.pos(1, 0, 1).tex(0, 0).endVertex();
		
		//Side 1: up
		wr.pos(1, 1, 1).tex(0, 1).endVertex();
		wr.pos(1, 1, 0).tex(1, 1).endVertex();
		wr.pos(0, 1, 0).tex(1, 0).endVertex();
		wr.pos(0, 1, 1).tex(0, 0).endVertex();
		
		//Side 2: north
		wr.pos(1, 0, 0).tex(0, 1).endVertex();
		wr.pos(0, 0, 0).tex(1, 1).endVertex();
		wr.pos(0, 1, 0).tex(1, 0).endVertex();
		wr.pos(1, 1, 0).tex(0, 0).endVertex();
		
		//Side 2: south
		wr.pos(0, 0, 1).tex(0, 1).endVertex();
		wr.pos(1, 0, 1).tex(1, 1).endVertex();
		wr.pos(1, 1, 1).tex(1, 0).endVertex();
		wr.pos(0, 1, 1).tex(0, 0).endVertex();
		
		//Side 3: west
		wr.pos(0, 0, 0).tex(0, 1).endVertex();
		wr.pos(0, 0, 1).tex(1, 1).endVertex();
		wr.pos(0, 1, 1).tex(1, 0).endVertex();
		wr.pos(0, 1, 0).tex(0, 0).endVertex();
		
		//Side 4: east
		wr.pos(1, 0, 1).tex(0, 1).endVertex();
		wr.pos(1, 0, 0).tex(1, 1).endVertex();
		wr.pos(1, 1, 0).tex(1, 0).endVertex();
		wr.pos(1, 1, 1).tex(0, 0).endVertex();
		
		//GlStateManager.scale(eib.textureHere.maxX-eib.textureHere.minX, eib.textureHere.maxY-eib.textureHere.minY, eib.textureHere.maxZ-eib.textureHere.minZ);
		
		t.draw();
		
		
		
		
		//GlStateManager.disableBlend();
		GlStateManager.popMatrix();
	}
}
