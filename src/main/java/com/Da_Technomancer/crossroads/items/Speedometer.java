package com.Da_Technomancer.crossroads.items;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

import com.Da_Technomancer.crossroads.Main;

import java.util.List;

public class Speedometer extends Item{

	public Speedometer(){
		String name = "speedometer";
		setUnlocalizedName(Main.MODID + "." + name);
		setRegistryName(name);
		ModItems.toRegister.add(this);
		ModItems.itemAddQue(this);
		setCreativeTab(ModItems.TAB_CROSSROADS);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag advanced){
		tooltip.add("THIS ITEM IS BEING REMOVED! Craft any you have into omnimeters!");
	}
}
