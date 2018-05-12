package com.Da_Technomancer.crossroads.entity;

import com.Da_Technomancer.crossroads.Main;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;

public class RenderFlyingMachine extends Render<EntityFlyingMachine>{

	private static final ResourceLocation TEXTURE = new ResourceLocation(Main.MODID, "textures/entities/flying_machine.png");

	protected RenderFlyingMachine(RenderManager renderManager){
		super(renderManager);
	}

	@Nullable
	@Override
	protected ResourceLocation getEntityTexture(EntityFlyingMachine entity){
		return TEXTURE;
	}

	@Override
	public void doRender(EntityFlyingMachine entity, double x, double y, double z, float entityYaw, float partialTicks){
		GlStateManager.pushMatrix();
		GlStateManager.pushAttrib();
		GlStateManager.disableLighting();
		GlStateManager.translate(x, y, z);
		GlStateManager.rotate(entityYaw + 90, 0, 1, 0);

		bindEntityTexture(entity);

		BufferBuilder buf = Tessellator.getInstance().getBuffer();

		GlStateManager.pushMatrix();
		GlStateManager.translate(0, 0.5D, 0);
		GlStateManager.rotate(-entity.getAngle() * 180F / (float) Math.PI, 1, 0, 0);


		buf.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);

		//Axle
		buf.pos(-0.5D, 0.125D, -0.125D).tex(0, 1).endVertex();
		buf.pos(-0.5D, 0.125D, 0.125D).tex(0, 0.9375D).endVertex();
		buf.pos(0.5D, 0.125D, 0.125D).tex(0.25D, 0.9375D).endVertex();
		buf.pos(0.5D, 0.125D, -0.125D).tex(0.25D, 1).endVertex();

		buf.pos(0.5D, -0.125D, -0.125D).tex(0.25D, 1).endVertex();
		buf.pos(0.5D, -0.125D, 0.125D).tex(0.25D, 0.9375D).endVertex();
		buf.pos(-0.5D, -0.125D, 0.125D).tex(0, 0.9375D).endVertex();
		buf.pos(-0.5D, -0.125D, -0.125D).tex(0, 1).endVertex();

		buf.pos(0.5D, -0.125D, 0.125D).tex(0.25D, 1).endVertex();
		buf.pos(0.5D, 0.125D, 0.125D).tex(0.25D, 0.9375D).endVertex();
		buf.pos(-0.5D, 0.125D, 0.125D).tex(0, 0.9375D).endVertex();
		buf.pos(-0.5D, -0.125D, 0.125D).tex(0, 1).endVertex();

		buf.pos(-0.5D, -0.125D, -0.125D).tex(0, 1).endVertex();
		buf.pos(-0.5D, 0.125D, -0.125D).tex(0, 0.9375D).endVertex();
		buf.pos(0.5D, 0.125D, -0.125D).tex(0.25D, 0.9375D).endVertex();
		buf.pos(0.5D, -0.125D, -0.125D).tex(0.25D, 1).endVertex();

		buf.pos(0.5D, -0.125D, -0.125D).tex(0, 0.875D).endVertex();
		buf.pos(0.5D, 0.125D, -0.125D).tex(0.0625D, 0.875D).endVertex();
		buf.pos(0.5D, 0.125D, 0.125D).tex(0.0625D, 0.9375D).endVertex();
		buf.pos(0.5D, -0.125D, 0.125D).tex(0, 0.9375D).endVertex();

		buf.pos(-0.5D, -0.125D, 0.125D).tex(0, 0.9375D).endVertex();
		buf.pos(-0.5D, 0.125D, 0.125D).tex(0.0625D, 0.9375D).endVertex();
		buf.pos(-0.5D, 0.125D, -0.125D).tex(0.0625D, 0.875D).endVertex();
		buf.pos(-0.5D, -0.125D, -0.125D).tex(0, 0.875D).endVertex();

		//Gravity plates
		buf.pos(-0.35D, 0.2D, -0.35D).tex(0.5D, 1).endVertex();
		buf.pos(-0.35D, 0.2D, 0.35D).tex(0.5D, 0.75D).endVertex();
		buf.pos(0.35D, 0.2D, 0.35D).tex(0.75D, 0.75D).endVertex();
		buf.pos(0.35D, 0.2D, -0.35D).tex(0.75D, 1).endVertex();

		buf.pos(0.35D, -0.2D, -0.35D).tex(0.5D, 1).endVertex();
		buf.pos(0.35D, -0.2D, 0.35D).tex(0.5D, 0.75D).endVertex();
		buf.pos(-0.35D, -0.2D, 0.35D).tex(0.25D, 0.75D).endVertex();
		buf.pos(-0.35D, -0.2D, -0.35D).tex(0.25D, 1).endVertex();

		buf.pos(0.35D, -0.2D, 0.35D).tex(1, 1).endVertex();
		buf.pos(0.35D, 0.2D, 0.35D).tex(1, 0.90625D).endVertex();
		buf.pos(-0.35D, 0.2D, 0.35D).tex(0.75D, 0.90625D).endVertex();
		buf.pos(-0.35D, -0.2D, 0.35D).tex(0.75D, 1).endVertex();

		buf.pos(-0.35D, -0.2D, -0.35D).tex(0.75D, 1).endVertex();
		buf.pos(-0.35D, 0.2D, -0.35D).tex(0.75D, 0.90625D).endVertex();
		buf.pos(0.35D, 0.2D, -0.35D).tex(1, 0.90625D).endVertex();
		buf.pos(0.35D, -0.2D, -0.35D).tex(1, 1).endVertex();

		buf.pos(0.35D, -0.2D, 0.35D).tex(1, 1).endVertex();
		buf.pos(0.35D, -0.2D, -0.35D).tex(0.75D, 1).endVertex();
		buf.pos(0.35D, 0.2D, -0.35D).tex(0.75D, 0.90625D).endVertex();
		buf.pos(0.35D, 0.2D, 0.35D).tex(1, 0.90625D).endVertex();

		buf.pos(-0.35D, -0.2D, -0.35D).tex(1, 1).endVertex();
		buf.pos(-0.35D, -0.2D, 0.35D).tex(0.75D, 1).endVertex();
		buf.pos(-0.35D, 0.2D, 0.35D).tex(0.75D, 0.90625D).endVertex();
		buf.pos(-0.35D, 0.2D, -0.35D).tex(1, 0.90625D).endVertex();

		Tessellator.getInstance().draw();
		GlStateManager.popMatrix();

		buf.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);

		//End boxes
		buf.pos(-0.5D, 0.7D, -0.2D).tex(0.25D, 0.5D).endVertex();
		buf.pos(-0.5D, 0.7D, 0.2D).tex(0, 0.5D).endVertex();
		buf.pos(-0.5D, 0.3D, 0.2D).tex(0, 0.75D).endVertex();
		buf.pos(-0.5D, 0.3D, -0.2D).tex(0.25D, 0.75D).endVertex();

		buf.pos(0.5D, 0.3D, -0.2D).tex(0.25D, 0.75D).endVertex();
		buf.pos(0.5D, 0.3D, 0.2D).tex(0, 0.75D).endVertex();
		buf.pos(0.5D, 0.7D, 0.2D).tex(0, 0.5D).endVertex();
		buf.pos(0.5D, 0.7D, -0.2D).tex(0.25D, 0.5D).endVertex();

		buf.pos(0.7D, 0.7D, -0.2D).tex(0.25D, 0.5D).endVertex();
		buf.pos(0.7D, 0.7D, 0.2D).tex(0, 0.5D).endVertex();
		buf.pos(0.7D, 0.3D, 0.2D).tex(0, 0.75D).endVertex();
		buf.pos(0.7D, 0.3D, -0.2D).tex(0.25D, 0.75D).endVertex();

		buf.pos(-0.7D, 0.3D, -0.2D).tex(0.25D, 0.75D).endVertex();
		buf.pos(-0.7D, 0.3D, 0.2D).tex(0, 0.75D).endVertex();
		buf.pos(-0.7D, 0.7D, 0.2D).tex(0, 0.5D).endVertex();
		buf.pos(-0.7D, 0.7D, -0.2D).tex(0.25D, 0.5D).endVertex();

		buf.pos(-0.7D, 0.7D, -0.2D).tex(0.25D, 0.75D).endVertex();
		buf.pos(-0.7D, 0.7D, 0.2D).tex(0, 0.75D).endVertex();
		buf.pos(-0.5D, 0.7D, 0.2D).tex(0, 0.5D).endVertex();
		buf.pos(-0.5D, 0.7D, -0.2D).tex(0.25D, 0.5D).endVertex();

		buf.pos(0.5D, 0.7D, -0.2D).tex(0.25D, 0.75D).endVertex();
		buf.pos(0.5D, 0.7D, 0.2D).tex(0, 0.75D).endVertex();
		buf.pos(0.7D, 0.7D, 0.2D).tex(0, 0.5D).endVertex();
		buf.pos(0.7D, 0.7D, -0.2D).tex(0.25D, 0.5D).endVertex();

		buf.pos(-0.5D, 0.3D, -0.2D).tex(0.25D, 0.5D).endVertex();
		buf.pos(-0.5D, 0.3D, 0.2D).tex(0, 0.5D).endVertex();
		buf.pos(-0.7D, 0.3D, 0.2D).tex(0, 0.75D).endVertex();
		buf.pos(-0.7D, 0.3D, -0.2D).tex(0.25D, 0.75D).endVertex();

		buf.pos(0.7D, 0.3D, -0.2D).tex(0.25D, 0.5D).endVertex();
		buf.pos(0.7D, 0.3D, 0.2D).tex(0, 0.5D).endVertex();
		buf.pos(0.5D, 0.3D, 0.2D).tex(0, 0.75D).endVertex();
		buf.pos(0.5D, 0.3D, -0.2D).tex(0.25D, 0.75D).endVertex();

		buf.pos(-0.7D, 0.3D, -0.2D).tex(0.25D, 0.5D).endVertex();
		buf.pos(-0.7D, 0.7D, -0.2D).tex(0, 0.5D).endVertex();
		buf.pos(-0.5D, 0.7D, -0.2D).tex(0, 0.75D).endVertex();
		buf.pos(-0.5D, 0.3D, -0.2D).tex(0.25D, 0.75D).endVertex();

		buf.pos(0.5D, 0.3D, -0.2D).tex(0.25D, 0.5D).endVertex();
		buf.pos(0.5D, 0.7D, -0.2D).tex(0, 0.5D).endVertex();
		buf.pos(0.7D, 0.7D, -0.2D).tex(0, 0.75D).endVertex();
		buf.pos(0.7D, 0.3D, -0.2D).tex(0.25D, 0.75D).endVertex();

		buf.pos(-0.5D, 0.3D, 0.2D).tex(0.25D, 0.75D).endVertex();
		buf.pos(-0.5D, 0.7D, 0.2D).tex(0, 0.75D).endVertex();
		buf.pos(-0.7D, 0.7D, 0.2D).tex(0, 0.5D).endVertex();
		buf.pos(-0.7D, 0.3D, 0.2D).tex(0.25D, 0.5D).endVertex();

		buf.pos(0.7D, 0.3D, 0.2D).tex(0.25D, 0.75D).endVertex();
		buf.pos(0.7D, 0.7D, 0.2D).tex(0, 0.75D).endVertex();
		buf.pos(0.5D, 0.7D, 0.2D).tex(0, 0.5D).endVertex();
		buf.pos(0.5D, 0.3D, 0.2D).tex(0.25D, 0.5D).endVertex();


		//Legs
		//Leg
		buf.pos(-0.7D, 0.7D, -0.325D).tex(0, 0.875D).endVertex();
		buf.pos(-0.7D, 0.7D, -0.2D).tex(0, 0.9375D).endVertex();
		buf.pos(-0.575D, 0.7D, -0.2D).tex(0.0625D, 0.9375D).endVertex();
		buf.pos(-0.575D, 0.7D, -0.325D).tex(0.0625D, 0.875D).endVertex();

		buf.pos(-0.575D, 0, -0.325D).tex(0.0625D, 0.875D).endVertex();
		buf.pos(-0.575D, 0, -0.2D).tex(0.0625D, 0.9375D).endVertex();
		buf.pos(-0.7D, 0, -0.2D).tex(0, 0.9375D).endVertex();
		buf.pos(-0.7D, 0, -0.325D).tex(0, 0.875D).endVertex();

		buf.pos(-0.575D, 0.7D, -0.325D).tex(0, 1).endVertex();
		buf.pos(-0.575D, 0D, -0.325D).tex(0.25D, 1).endVertex();
		buf.pos(-0.7D, 0D, -0.325D).tex(0.25D, 0.9375D).endVertex();
		buf.pos(-0.7D, 0.7D, -0.325D).tex(0, 0.9375D).endVertex();

		buf.pos(-0.7D, 0.7D, -0.2D).tex(0, 0.9375D).endVertex();
		buf.pos(-0.7D, 0D, -0.2D).tex(0.25D, 0.9375D).endVertex();
		buf.pos(-0.575D, 0D, -0.2D).tex(0.25D, 1).endVertex();
		buf.pos(-0.575D, 0.7D, -0.2D).tex(0, 1).endVertex();

		buf.pos(-0.7D, 0.7D, -0.325D).tex(0, 0.9375D).endVertex();
		buf.pos(-0.7D, 0D, -0.325D).tex(0.25D, 0.9375D).endVertex();
		buf.pos(-0.7D, 0D, -0.2D).tex(0.25D, 1).endVertex();
		buf.pos(-0.7D, 0.7D, -0.2D).tex(0, 1).endVertex();

		buf.pos(-0.575D, 0.7D, -0.2D).tex(0, 1).endVertex();
		buf.pos(-0.575D, 0D, -0.2D).tex(0.25D, 1).endVertex();
		buf.pos(-0.575D, 0D, -0.325D).tex(0.25D, 0.9375D).endVertex();
		buf.pos(-0.575D, 0.7D, -0.325D).tex(0, 0.9375D).endVertex();

		//Leg
		buf.pos(0.575D, 0.7D, -0.325D).tex(0.0625D, 0.875D).endVertex();
		buf.pos(0.575D, 0.7D, -0.2D).tex(0.0625D, 0.9375D).endVertex();
		buf.pos(0.7D, 0.7D, -0.2D).tex(0, 0.9375D).endVertex();
		buf.pos(0.7D, 0.7D, -0.325D).tex(0, 0.875D).endVertex();

		buf.pos(0.7D, 0, -0.325D).tex(0, 0.875D).endVertex();
		buf.pos(0.7D, 0, -0.2D).tex(0, 0.9375D).endVertex();
		buf.pos(0.575D, 0, -0.2D).tex(0.0625D, 0.9375D).endVertex();
		buf.pos(0.575D, 0, -0.325D).tex(0.0625D, 0.875D).endVertex();

		buf.pos(0.7D, 0.7D, -0.325D).tex(0, 0.9375D).endVertex();
		buf.pos(0.7D, 0D, -0.325D).tex(0.25D, 0.9375D).endVertex();
		buf.pos(0.575D, 0D, -0.325D).tex(0.25D, 1).endVertex();
		buf.pos(0.575D, 0.7D, -0.325D).tex(0, 1).endVertex();

		buf.pos(0.575D, 0.7D, -0.2D).tex(0, 1).endVertex();
		buf.pos(0.575D, 0D, -0.2D).tex(0.25D, 1).endVertex();
		buf.pos(0.7D, 0D, -0.2D).tex(0.25D, 0.9375D).endVertex();
		buf.pos(0.7D, 0.7D, -0.2D).tex(0, 0.9375D).endVertex();

		buf.pos(0.7D, 0.7D, -0.2D).tex(0, 1).endVertex();
		buf.pos(0.7D, 0D, -0.2D).tex(0.25D, 1).endVertex();
		buf.pos(0.7D, 0D, -0.325D).tex(0.25D, 0.9375D).endVertex();
		buf.pos(0.7D, 0.7D, -0.325D).tex(0, 0.9375D).endVertex();

		buf.pos(0.575D, 0.7D, -0.325D).tex(0, 0.9375D).endVertex();
		buf.pos(0.575D, 0D, -0.325D).tex(0.25D, 0.9375D).endVertex();
		buf.pos(0.575D, 0D, -0.2D).tex(0.25D, 1).endVertex();
		buf.pos(0.575D, 0.7D, -0.2D).tex(0, 1).endVertex();

		//Leg
		buf.pos(0.7D, 0.7D, 0.325D).tex(0, 0.875D).endVertex();
		buf.pos(0.7D, 0.7D, 0.2D).tex(0, 0.9375D).endVertex();
		buf.pos(0.575D, 0.7D, 0.2D).tex(0.0625D, 0.9375D).endVertex();
		buf.pos(0.575D, 0.7D, 0.325D).tex(0.0625D, 0.875D).endVertex();

		buf.pos(0.575D, 0, 0.325D).tex(0.0625D, 0.875D).endVertex();
		buf.pos(0.575D, 0, 0.2D).tex(0.0625D, 0.9375D).endVertex();
		buf.pos(0.7D, 0, 0.2D).tex(0, 0.9375D).endVertex();
		buf.pos(0.7D, 0, 0.325D).tex(0, 0.875D).endVertex();

		buf.pos(0.575D, 0.7D, 0.325D).tex(0, 1).endVertex();
		buf.pos(0.575D, 0D, 0.325D).tex(0.25D, 1).endVertex();
		buf.pos(0.7D, 0D, 0.325D).tex(0.25D, 0.9375D).endVertex();
		buf.pos(0.7D, 0.7D, 0.325D).tex(0, 0.9375D).endVertex();

		buf.pos(0.7D, 0.7D, 0.2D).tex(0, 0.9375D).endVertex();
		buf.pos(0.7D, 0D, 0.2D).tex(0.25D, 0.9375D).endVertex();
		buf.pos(0.575D, 0D, 0.2D).tex(0.25D, 1).endVertex();
		buf.pos(0.575D, 0.7D, 0.2D).tex(0, 1).endVertex();

		buf.pos(0.7D, 0.7D, 0.325D).tex(0, 0.9375D).endVertex();
		buf.pos(0.7D, 0D, 0.325D).tex(0.25D, 0.9375D).endVertex();
		buf.pos(0.7D, 0D, 0.2D).tex(0.25D, 1).endVertex();
		buf.pos(0.7D, 0.7D, 0.2D).tex(0, 1).endVertex();

		buf.pos(0.575D, 0.7D, 0.2D).tex(0, 1).endVertex();
		buf.pos(0.575D, 0D, 0.2D).tex(0.25D, 1).endVertex();
		buf.pos(0.575D, 0D, 0.325D).tex(0.25D, 0.9375D).endVertex();
		buf.pos(0.575D, 0.7D, 0.325D).tex(0, 0.9375D).endVertex();

		//Leg
		buf.pos(-0.575D, 0.7D, 0.325D).tex(0.0625D, 0.875D).endVertex();
		buf.pos(-0.575D, 0.7D, 0.2D).tex(0.0625D, 0.9375D).endVertex();
		buf.pos(-0.7D, 0.7D, 0.2D).tex(0, 0.9375D).endVertex();
		buf.pos(-0.7D, 0.7D, 0.325D).tex(0, 0.875D).endVertex();

		buf.pos(-0.7D, 0, 0.325D).tex(0, 0.875D).endVertex();
		buf.pos(-0.7D, 0, 0.2D).tex(0, 0.9375D).endVertex();
		buf.pos(-0.575D, 0, 0.2D).tex(0.0625D, 0.9375D).endVertex();
		buf.pos(-0.575D, 0, 0.325D).tex(0.0625D, 0.875D).endVertex();

		buf.pos(-0.7D, 0.7D, 0.325D).tex(0, 0.9375D).endVertex();
		buf.pos(-0.7D, 0D, 0.325D).tex(0.25D, 0.9375D).endVertex();
		buf.pos(-0.575D, 0D, 0.325D).tex(0.25D, 1).endVertex();
		buf.pos(-0.575D, 0.7D, 0.325D).tex(0, 1).endVertex();

		buf.pos(-0.575D, 0.7D, 0.2D).tex(0, 1).endVertex();
		buf.pos(-0.575D, 0D, 0.2D).tex(0.25D, 1).endVertex();
		buf.pos(-0.7D, 0D, 0.2D).tex(0.25D, 0.9375D).endVertex();
		buf.pos(-0.7D, 0.7D, 0.2D).tex(0, 0.9375D).endVertex();

		buf.pos(-0.7D, 0.7D, 0.2D).tex(0, 1).endVertex();
		buf.pos(-0.7D, 0D, 0.2D).tex(0.25D, 1).endVertex();
		buf.pos(-0.7D, 0D, 0.325D).tex(0.25D, 0.9375D).endVertex();
		buf.pos(-0.7D, 0.7D, 0.325D).tex(0, 0.9375D).endVertex();

		buf.pos(-0.575D, 0.7D, 0.325D).tex(0, 0.9375D).endVertex();
		buf.pos(-0.575D, 0D, 0.325D).tex(0.25D, 0.9375D).endVertex();
		buf.pos(-0.575D, 0D, 0.2D).tex(0.25D, 1).endVertex();
		buf.pos(-0.575D, 0.7D, 0.2D).tex(0, 1).endVertex();

		//Supports
		//Support
		buf.pos(-0.575D, 1.2D, 0.0625D).tex(0.0625D, 0.875D).endVertex();
		buf.pos(-0.575D, 1.2D, -0.0625D).tex(0.0625D, 0.9375D).endVertex();
		buf.pos(-0.7D, 1.2D, -0.0625D).tex(0, 0.9375D).endVertex();
		buf.pos(-0.7D, 1.2D, 0.0625D).tex(0, 0.875D).endVertex();

		buf.pos(-0.7D, 1.2D, 0.0625D).tex(0, 0.9375D).endVertex();
		buf.pos(-0.7D, 0.7D, 0.0625D).tex(0.25D, 0.9375D).endVertex();
		buf.pos(-0.575D, 0.7D, 0.0625D).tex(0.25D, 1).endVertex();
		buf.pos(-0.575D, 1.2D, 0.0625D).tex(0, 1).endVertex();

		buf.pos(-0.575D, 1.2D, -0.0625D).tex(0, 1).endVertex();
		buf.pos(-0.575D, 0.7D, -0.0625D).tex(0.25D, 1).endVertex();
		buf.pos(-0.7D, 0.7D, -0.0625D).tex(0.25D, 0.9375D).endVertex();
		buf.pos(-0.7D, 1.2D, -0.0625D).tex(0, 0.9375D).endVertex();

		buf.pos(-0.7D, 1.2D, -0.0625D).tex(0, 1).endVertex();
		buf.pos(-0.7D, 0.7D, -0.0625D).tex(0.25D, 1).endVertex();
		buf.pos(-0.7D, 0.7D, 0.0625D).tex(0.25D, 0.9375D).endVertex();
		buf.pos(-0.7D, 1.2D, 0.0625D).tex(0, 0.9375D).endVertex();

		buf.pos(-0.575D, 1.2D, 0.0625D).tex(0, 0.9375D).endVertex();
		buf.pos(-0.575D, 0.7D, 0.0625D).tex(0.25D, 0.9375D).endVertex();
		buf.pos(-0.575D, 0.7D, -0.0625D).tex(0.25D, 1).endVertex();
		buf.pos(-0.575D, 1.2D, -0.0625D).tex(0, 1).endVertex();

		//Support
		buf.pos(0.7D, 1.2D, 0.0625D).tex(0, 0.875D).endVertex();
		buf.pos(0.7D, 1.2D, -0.0625D).tex(0, 0.9375D).endVertex();
		buf.pos(0.575D, 1.2D, -0.0625D).tex(0.0625D, 0.9375D).endVertex();
		buf.pos(0.575D, 1.2D, 0.0625D).tex(0.0625D, 0.875D).endVertex();

		buf.pos(0.575D, 1.2D, 0.0625D).tex(0, 1).endVertex();
		buf.pos(0.575D, 0.7D, 0.0625D).tex(0.25D, 1).endVertex();
		buf.pos(0.7D, 0.7D, 0.0625D).tex(0.25D, 0.9375D).endVertex();
		buf.pos(0.7D, 1.2D, 0.0625D).tex(0, 0.9375D).endVertex();

		buf.pos(0.7D, 1.2D, -0.0625D).tex(0, 0.9375D).endVertex();
		buf.pos(0.7D, 0.7D, -0.0625D).tex(0.25D, 0.9375D).endVertex();
		buf.pos(0.575D, 0.7D, -0.0625D).tex(0.25D, 1).endVertex();
		buf.pos(0.575D, 1.2D, -0.0625D).tex(0, 1).endVertex();

		buf.pos(0.7D, 1.2D, 0.0625D).tex(0, 0.9375D).endVertex();
		buf.pos(0.7D, 0.7D, 0.0625D).tex(0.25D, 0.9375D).endVertex();
		buf.pos(0.7D, 0.7D, -0.0625D).tex(0.25D, 1).endVertex();
		buf.pos(0.7D, 1.2D, -0.0625D).tex(0, 1).endVertex();

		buf.pos(0.575D, 1.2D, -0.0625D).tex(0, 1).endVertex();
		buf.pos(0.575D, 0.7D, -0.0625D).tex(0.25D, 1).endVertex();
		buf.pos(0.575D, 0.7D, 0.0625D).tex(0.25D, 0.9375D).endVertex();
		buf.pos(0.575D, 1.2D, 0.0625D).tex(0, 0.9375D).endVertex();

		//Seat
		buf.pos(0.7D, 1.3D, 0.4D).tex(0.25D, 0.5D).endVertex();
		buf.pos(0.7D, 1.3D, -0.4D).tex(0.25D, 0.75D).endVertex();
		buf.pos(-0.7D, 1.3D, -0.4D).tex(0, 0.75D).endVertex();
		buf.pos(-0.7D, 1.3D, 0.4D).tex(0, 0.5D).endVertex();

		buf.pos(-0.7D, 1.2D, 0.4D).tex(0, 0.5D).endVertex();
		buf.pos(-0.7D, 1.2D, -0.4D).tex(0, 0.75D).endVertex();
		buf.pos(0.7D, 1.2D, -0.4D).tex(0.25D, 0.75D).endVertex();
		buf.pos(0.7D, 1.2D, 0.4D).tex(0.25D, 0.5D).endVertex();

		buf.pos(-0.7D, 1.3D, 0.4D).tex(0, 0.5D).endVertex();
		buf.pos(-0.7D, 1.2D, 0.4D).tex(0.25D, 0.5D).endVertex();
		buf.pos(0.7D, 1.2D, 0.4D).tex(0.25D, 0.75D).endVertex();
		buf.pos(0.7D, 1.3D, 0.4D).tex(0, 0.75D).endVertex();

		buf.pos(0.7D, 1.3D, -0.4D).tex(0, 0.75D).endVertex();
		buf.pos(0.7D, 1.2D, -0.4D).tex(0.25D, 0.75D).endVertex();
		buf.pos(-0.7D, 1.2D, -0.4D).tex(0.25D, 0.5D).endVertex();
		buf.pos(-0.7D, 1.3D, -0.4D).tex(0, 0.5D).endVertex();

		buf.pos(-0.7D, 1.3D, -0.4D).tex(0, 0.75D).endVertex();
		buf.pos(-0.7D, 1.2D, -0.4D).tex(0.25D, 0.75D).endVertex();
		buf.pos(-0.7D, 1.2D, 0.4D).tex(0.25D, 0.5D).endVertex();
		buf.pos(-0.7D, 1.3D, 0.4D).tex(0, 0.5D).endVertex();

		buf.pos(0.7D, 1.3D, 0.4D).tex(0, 0.5D).endVertex();
		buf.pos(0.7D, 1.2D, 0.4D).tex(0.25D, 0.5D).endVertex();
		buf.pos(0.7D, 1.2D, -0.4D).tex(0.25D, 0.75D).endVertex();
		buf.pos(0.7D, 1.3D, -0.4D).tex(0, 0.75D).endVertex();



		Tessellator.getInstance().draw();


		GlStateManager.enableLighting();
		GlStateManager.popAttrib();
		GlStateManager.popMatrix();
	}
}