package com.Da_Technomancer.crossroads.fluids;

import com.Da_Technomancer.crossroads.Main;
import com.Da_Technomancer.crossroads.blocks.ModBlocks;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

public class BlockLiquidFat extends BlockFluidClassic{

	protected static final FluidLiquidFat LIQUID_FAT = new FluidLiquidFat();

	public BlockLiquidFat(){
		super(LIQUID_FAT, Material.WATER);
		LIQUID_FAT.setBlock(this);
		setUnlocalizedName(Main.MODID + "." + "blockLiquidFat");
		setRegistryName("blockLiquidFat");
		ModBlocks.toRegister.add(this);
	}

	@Override
	public int getLightOpacity(IBlockState state, IBlockAccess world, BlockPos pos){
		return 2;
	}

	/**
	 * For normal use.
	 */
	public static Fluid getLiquidFat(){
		return FluidRegistry.getFluid("liquidfat");
	}

	private static class FluidLiquidFat extends Fluid{

		private FluidLiquidFat(){
			super("liquidfat", new ResourceLocation(Main.MODID + ":blocks/liquidfat_still"), new ResourceLocation(Main.MODID + ":blocks/liquidfat_flow"));
			setUnlocalizedName("crossroads.liquidfat");
			setDensity(2000);
			setViscosity(2000);
		}
	}
}
