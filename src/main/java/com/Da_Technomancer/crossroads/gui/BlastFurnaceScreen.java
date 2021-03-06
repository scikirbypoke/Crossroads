package com.Da_Technomancer.crossroads.gui;

import com.Da_Technomancer.crossroads.API.templates.MachineGUI;
import com.Da_Technomancer.crossroads.Crossroads;
import com.Da_Technomancer.crossroads.gui.container.BlastFurnaceContainer;
import com.Da_Technomancer.crossroads.tileentities.rotary.BlastFurnaceTileEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class BlastFurnaceScreen extends MachineGUI<BlastFurnaceContainer, BlastFurnaceTileEntity>{

	private static final ResourceLocation TEXTURE = new ResourceLocation(Crossroads.MODID, "textures/gui/container/blast_furnace_gui.png");

	public BlastFurnaceScreen(BlastFurnaceContainer cont, PlayerInventory playerInv, ITextComponent text){
		super(cont, playerInv, text);
	}

	@Override
	public void init(){
		super.init();
		initFluidManager(0, 63, 70);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(MatrixStack matrix, float partialTicks, int mouseX, int mouseY){
		Minecraft.getInstance().getTextureManager().bindTexture(TEXTURE);

		blit(matrix, guiLeft, guiTop, 0, 0, xSize, ySize);
		blit(matrix, guiLeft + 25, guiTop + 38, 176, 0, 38 * container.progRef.get() / BlastFurnaceTileEntity.REQUIRED_PRG, 14);
		fill(matrix, guiLeft + 50, guiTop + 36 - container.carbRef.get() * 16 / BlastFurnaceTileEntity.CARBON_LIMIT, guiLeft + 52, guiTop + 36, 0xFF000000);

		super.drawGuiContainerBackgroundLayer(matrix, partialTicks, mouseX, mouseY);
	}
}
