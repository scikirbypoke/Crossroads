package com.Da_Technomancer.crossroads.items;

import com.Da_Technomancer.crossroads.API.Capabilities;
import com.Da_Technomancer.crossroads.API.rotary.IAxleHandler;
import com.Da_Technomancer.crossroads.API.rotary.RotaryUtil;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nullable;
import java.util.List;

public class HandCrank extends Item{

	protected HandCrank(){
		this("hand_crank");
	}

	protected HandCrank(String name){
		super(new Properties().group(CRItems.TAB_CROSSROADS).maxStackSize(1));
		setRegistryName(name);
		CRItems.toRegister.add(this);
	}

	protected int getRate(){
		return 100;
	}

	@Override
	public ActionResultType onItemUse(ItemUseContext context){
		TileEntity te = context.getWorld().getTileEntity(context.getPos());
		LazyOptional<IAxleHandler> axleOpt;
		Direction side = context.getFace().getOpposite();
		if(te != null && (axleOpt = te.getCapability(Capabilities.AXLE_CAPABILITY, side)).isPresent()){
			double signMult = -1;
			if(context.getPlayer() != null && context.getPlayer().isSneaking()){
				signMult *= -1;
			}
			signMult *= RotaryUtil.getCCWSign(side);
			axleOpt.orElseThrow(NullPointerException::new).addEnergy(getRate() * signMult, true);
			return ActionResultType.SUCCESS;
		}
		return ActionResultType.PASS;
	}

	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn){
		tooltip.add(new TranslationTextComponent("tt.crossroads.crank.desc", getRate()));
		tooltip.add(new TranslationTextComponent("tt.crossroads.crank.back", getRate()));
	}
}
