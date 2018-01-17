package com.Da_Technomancer.crossroads.items.crafting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.annotation.Nonnull;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;

import com.Da_Technomancer.crossroads.API.magic.MagicUnit;
import com.Da_Technomancer.crossroads.integration.JEI.ArcaneExtractorCategory;
import com.Da_Technomancer.crossroads.integration.JEI.ArcaneExtractorRecipe;
import com.Da_Technomancer.crossroads.integration.JEI.DetailedCrafterCategory;
import com.Da_Technomancer.crossroads.integration.JEI.DetailedCrafterRecipe;
import com.Da_Technomancer.crossroads.integration.JEI.FluidCoolingCategory;
import com.Da_Technomancer.crossroads.integration.JEI.FluidCoolingRecipe;
import com.Da_Technomancer.crossroads.integration.JEI.FusionBeamCategory;
import com.Da_Technomancer.crossroads.integration.JEI.FusionBeamRecipe;
import com.Da_Technomancer.crossroads.integration.JEI.GrindstoneCategory;
import com.Da_Technomancer.crossroads.integration.JEI.GrindstoneRecipe;
import com.Da_Technomancer.crossroads.integration.JEI.HeatExchangerCategory;
import com.Da_Technomancer.crossroads.integration.JEI.HeatExchangerRecipe;
import com.Da_Technomancer.crossroads.integration.JEI.HeatingCrucibleCategory;
import com.Da_Technomancer.crossroads.integration.JEI.HeatingCrucibleRecipe;

import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

public final class RecipeHolder{

	/**
	 * CraftingStack is input, the array is the outputs. HAVE NO MORE THAN 3 ITEMSTACKS IN THE ARRAY.
	 * 
	 */
	public static final HashMap<ICraftingStack<ItemStack>, ItemStack[]> grindRecipes = new HashMap<ICraftingStack<ItemStack>, ItemStack[]>();

	/**
	 * Block is input, blockstate is the created block, Double1 is heat created, Double2 is the limit, boolean is whether to show this recipe in JEI. 
	 * 
	 */
	public static final HashMap<Block, Pair<Boolean, Triple<IBlockState, Double, Double>>> envirHeatSource = new HashMap<Block, Pair<Boolean, Triple<IBlockState, Double, Double>>>();

	/**
	 * Fluid is input, Integer is the amount required, ItemStack is output,
	 * Double1 is maximum temperature, and Double2 is heat added on craft.
	 * 
	 */
	public static final HashMap<Fluid, Pair<Integer, Triple<ItemStack, Double, Double>>> fluidCoolingRecipes = new HashMap<Fluid, Pair<Integer, Triple<ItemStack, Double, Double>>>();

	/**
	 * Stores the heating crucible recipes. BE CAREFUL, the contained FluidStack is mutable. The ICraftingStack is the ingredient, FluidStack is output, and String is the unmelted texture
	 */
	public static final ArrayList<Triple<ICraftingStack<ItemStack>, FluidStack, String>> heatingCrucibleRecipes = new ArrayList<Triple<ICraftingStack<ItemStack>, FluidStack, String>>();
	
	/**
	 * A list of all recipes, Item Array are the ingredients, and itemstack is
	 * output. A list for poisonous potato recipes and mashed potato recipes.
	 * 
	 * Under no condition is anyone to add support for the Bobo recipes in JEI (or any other recipe helper). 
	 */
	protected static final ArrayList<Pair<ICraftingStack<ItemStack>[], ItemStack>> brazierBoboRecipes = new ArrayList<Pair<ICraftingStack<ItemStack>[], ItemStack>>();

	/**
	 * Item is input, magic unit is the magic extracted. For the Arcane Extractor
	 */
	public static final HashMap<Item, MagicUnit> magExtractRecipes = new HashMap<Item, MagicUnit>();
	
	/**
	 * Stores the fusion beam conversion recipes. 
	 */
	public static final CraftStackMap<IBlockState, BlockCraftingStack, BeamTransmute> fusionBeamRecipes = new CraftStackMap<IBlockState, BlockCraftingStack, BeamTransmute>();
	
	/**
	 * Stores the void-fusion beam conversion recipes. 
	 */
	public static final CraftStackMap<IBlockState, BlockCraftingStack, BeamTransmute> vFusionBeamRecipes = new CraftStackMap<IBlockState, BlockCraftingStack, BeamTransmute>();

	/**
	 * The recipes for the Detailed Crafter that require technomancy to be unlocked.
	 * Recipes can have a null group (it is unused). 
	 * ONLY USE ShapedOreRecipe and ShapelessOreRecipe. Using any other type require changing the JEI integration (DetailedCrafterRecipeWrapper) or it will crash.
	 */
	public static final ArrayList<IRecipe> technomancyRecipes = new ArrayList<IRecipe>();
	
	/**
	 * The recipes for the Detailed Crafter that require alchemy to be unlocked.
	 * Recipes can have a null group (it is unused). 
	 * ONLY USE ShapedOreRecipe and ShapelessOreRecipe. Using any other type require changing the JEI integration (DetailedCrafterRecipeWrapper) or it will crash.
	 */
	public static final ArrayList<IRecipe> alchemyRecipes = new ArrayList<IRecipe>();

	public static final HashMap<String, ArrayList<IRecipeWrapper>> JEIWrappers = new HashMap<String, ArrayList<IRecipeWrapper>>();

	/**
	 * Converts the versions of the recipes used internally into fake recipes
	 * for JEI. Not called unless JEI is installed.
	 */
	public static void rebind(){
		ArrayList<IRecipeWrapper> currentRecipes = new ArrayList<IRecipeWrapper>();
		for(Entry<ICraftingStack<ItemStack>, ItemStack[]> rec : grindRecipes.entrySet()){
			currentRecipes.add(new GrindstoneRecipe(rec));
		}
		JEIWrappers.put(GrindstoneCategory.ID, currentRecipes);
		
		currentRecipes = new ArrayList<IRecipeWrapper>();
		for(Entry<Fluid, Pair<Integer, Triple<ItemStack, Double, Double>>> rec : fluidCoolingRecipes.entrySet()){
			currentRecipes.add(new FluidCoolingRecipe(rec));
		}
		JEIWrappers.put(FluidCoolingCategory.ID, currentRecipes);
		
		currentRecipes = new ArrayList<IRecipeWrapper>();
		for(Entry<Block, Pair<Boolean, Triple<IBlockState, Double, Double>>> rec : envirHeatSource.entrySet()){
			if(rec.getValue().getLeft()){
				currentRecipes.add(new HeatExchangerRecipe(rec));
			}
		}
		JEIWrappers.put(HeatExchangerCategory.ID, currentRecipes);
		
		currentRecipes = new ArrayList<IRecipeWrapper>();
		for(IRecipe rec : technomancyRecipes){
			currentRecipes.add(new DetailedCrafterRecipe(rec, 0));
		}
		for(IRecipe rec : alchemyRecipes){
			currentRecipes.add(new DetailedCrafterRecipe(rec, 1));
		}
		JEIWrappers.put(DetailedCrafterCategory.ID, currentRecipes);
		
		currentRecipes = new ArrayList<IRecipeWrapper>();
		for(Triple<ICraftingStack<ItemStack>, FluidStack, String> rec : heatingCrucibleRecipes){
			currentRecipes.add(new HeatingCrucibleRecipe(rec.getLeft(), rec.getMiddle()));
		}
		JEIWrappers.put(HeatingCrucibleCategory.ID, currentRecipes);
		
		currentRecipes = new ArrayList<IRecipeWrapper>();
		for(Entry<Item, MagicUnit> rec : magExtractRecipes.entrySet()){
			currentRecipes.add(new ArcaneExtractorRecipe(new ItemStack(rec.getKey(), 1, OreDictionary.WILDCARD_VALUE), rec.getValue()));
		}
		JEIWrappers.put(ArcaneExtractorCategory.ID, currentRecipes);
		
		currentRecipes = new ArrayList<IRecipeWrapper>();
		for(Pair<BlockCraftingStack, BeamTransmute> rec : fusionBeamRecipes.entrySet()){
			currentRecipes.add(new FusionBeamRecipe(rec.getKey(), rec.getRight().state, rec.getRight().minPower, false));
		}
		for(Pair<BlockCraftingStack, BeamTransmute> rec : vFusionBeamRecipes.entrySet()){
			currentRecipes.add(new FusionBeamRecipe(rec.getKey(), rec.getRight().state, rec.getRight().minPower, true));
		}
		JEIWrappers.put(FusionBeamCategory.ID, currentRecipes);
	}

	@Nonnull
	public static ItemStack recipeMatch(ArrayList<EntityItem> itemEnt){
		if(itemEnt == null){
			return ItemStack.EMPTY;
		}

		ArrayList<ItemStack> items = new ArrayList<ItemStack>();

		for(EntityItem it : itemEnt){
			if(it.getItem().isEmpty() || it.getItem().getCount() != 1){
				return ItemStack.EMPTY;
			}
			items.add(it.getItem());
		}

		if(items.size() != 3){
			return ItemStack.EMPTY;
		}

		for(Pair<ICraftingStack<ItemStack>[], ItemStack> craft : brazierBoboRecipes){
			ArrayList<ItemStack> itemCop = new ArrayList<ItemStack>();
			itemCop.addAll(items);

			for(ICraftingStack<ItemStack> cStack : craft.getLeft()){
				for(ItemStack stack : items){
					if(itemCop.contains(stack) && cStack.softMatch(stack)){
						itemCop.remove(stack);
						break;
					}
				}

				if(itemCop.size() == 0){
					return craft.getRight();
				}
			}
		}

		return ItemStack.EMPTY;
	}
}
