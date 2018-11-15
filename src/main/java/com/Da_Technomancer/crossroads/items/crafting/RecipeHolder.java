package com.Da_Technomancer.crossroads.items.crafting;

import com.Da_Technomancer.crossroads.API.alchemy.AlchemyCore;
import com.Da_Technomancer.crossroads.API.alchemy.IReaction;
import com.Da_Technomancer.crossroads.API.alchemy.IReagent;
import com.Da_Technomancer.crossroads.API.alchemy.ITransparentReaction;
import com.Da_Technomancer.crossroads.API.beams.BeamUnit;
import com.Da_Technomancer.crossroads.integration.JEI.*;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Predicate;

public final class RecipeHolder{

	/**
	 * CraftingStack is input, the array is the outputs. HAVE NO MORE THAN 3 ITEMSTACKS IN THE ARRAY.
	 */
	public static final PredicateMap<ItemStack, ItemStack[]> millRecipes = new PredicateMap<>();

	/**
	 * CraftingStack is input, the ItemStack is the output
	 */
	public static final PredicateMap<ItemStack, ItemStack> stampMillRecipes = new PredicateMap<>(ItemStack.EMPTY);

	/**
	 * CraftingStack is input, the ItemStack is the output
	 */
	public static final PredicateMap<ItemStack, ItemStack> oreCleanserRecipes = new PredicateMap<>(ItemStack.EMPTY);

	/**
	 * CraftingStack is input, the FluidStack is the output, and the Integer is the amount of slag created
	 */
	public static final PredicateMap<ItemStack, Pair<FluidStack, Integer>> blastFurnaceRecipes = new PredicateMap<>();

	/**
	 * Fluid is input, Integer is the amount required, ItemStack is output,
	 * Double1 is maximum temperature, and Double2 is heat added on craft.
	 * 
	 */
	public static final HashMap<Fluid, Pair<Integer, Triple<ItemStack, Double, Double>>> fluidCoolingRecipes = new HashMap<>();

	/**
	 * Stores the heating crucible recipes. BE CAREFUL, the contained FluidStack is mutable. The ItemStack is the ingredient, FluidStack is output
	 */
	public static final PredicateMap<ItemStack, FluidStack> crucibleRecipes = new PredicateMap<>();

	/**
	 * ItemStack is input, Integer is the number of ticks the item lasts
	 */
	public static final PredicateMap<ItemStack, Integer> coolingRecipes = new PredicateMap<>(0);

	/**
	 * Item is input, beams unit is the beams extracted. For the Beam Extractor
	 */
	public static final HashMap<Item, BeamUnit> beamExtractRecipes = new HashMap<>();

	/**
	 * Stores the fusion beam conversion recipes. 
	 */
	public static final PredicateMap<IBlockState, BeamTransmute> fusionBeamRecipes = new PredicateMap<>();

	/**
	 * Stores the void-fusion beam conversion recipes. 
	 */
	public static final PredicateMap<IBlockState, BeamTransmute> vFusionBeamRecipes = new PredicateMap<>();

	/**
	 * The recipes for the Detailed Crafter that require technomancy to be unlocked.
	 * Recipes can have a null group (it is unused). 
	 * ONLY USE ShapedOreRecipe and ShapelessOreRecipe. Using any other type require changing the JEI integration (DetailedCrafterRecipeWrapper) or it will crash.
	 */
	public static final ArrayList<IRecipe> technomancyRecipes = new ArrayList<>();

	/**
	 * The recipes for the Detailed Crafter that require alchemy to be unlocked.
	 * Recipes can have a null group (it is unused). 
	 * ONLY USE ShapedOreRecipe and ShapelessOreRecipe. Using any other type require changing the JEI integration (DetailedCrafterRecipeWrapper) or it will crash.
	 */
	public static final ArrayList<IRecipe> alchemyRecipes = new ArrayList<>();

	public static final HashMap<String, ArrayList<IRecipeWrapper>> JEIWrappers = new HashMap<>();

	/**
	 * Converts the versions of the recipes used internally into fake recipes
	 * for JEI. Not called unless JEI is installed.
	 */
	public static void rebind(){
		ArrayList<IRecipeWrapper> currentRecipes = new ArrayList<IRecipeWrapper>();

		recipe: for(Entry<Predicate<ItemStack>, ItemStack[]> rec : millRecipes.entrySet()){
			if(!(rec.getKey() instanceof RecipePredicate) || ((RecipePredicate) (rec.getKey())).getMatchingList().isEmpty()){
				continue;
			}

			for(ItemStack s : rec.getValue()){
				if(s != null && s.isEmpty()){
					continue recipe;
				}
			}
			currentRecipes.add(new MillstoneRecipe(((RecipePredicate<ItemStack>) rec.getKey()).getMatchingList(), rec.getValue()));
		}
		JEIWrappers.put(MillstoneCategory.ID, currentRecipes);

		currentRecipes = new ArrayList<IRecipeWrapper>();
		for(Entry<Fluid, Pair<Integer, Triple<ItemStack, Double, Double>>> rec : fluidCoolingRecipes.entrySet()){
			currentRecipes.add(new FluidCoolingRecipe(rec));
		}
		JEIWrappers.put(FluidCoolingCategory.ID, currentRecipes);

		currentRecipes = new ArrayList<IRecipeWrapper>();
		for(IRecipe rec : technomancyRecipes){
			currentRecipes.add(new DetailedCrafterRecipe(rec, 0));
		}
		for(IRecipe rec : alchemyRecipes){
			currentRecipes.add(new DetailedCrafterRecipe(rec, 1));
		}
		JEIWrappers.put(DetailedCrafterCategory.ID, currentRecipes);

		currentRecipes = new ArrayList<IRecipeWrapper>();
		for(Entry<Predicate<ItemStack>, FluidStack> rec : crucibleRecipes.entrySet()){
			if(rec != null && rec.getKey() instanceof RecipePredicate){
				currentRecipes.add(new HeatingCrucibleRecipe((RecipePredicate<ItemStack>) rec.getKey(), rec.getValue()));
			}
		}
		JEIWrappers.put(HeatingCrucibleCategory.ID, currentRecipes);

		currentRecipes = new ArrayList<IRecipeWrapper>();
		for(Entry<Item, BeamUnit> rec : beamExtractRecipes.entrySet()){
			currentRecipes.add(new BeamExtractorRecipe(new ItemStack(rec.getKey(), 1, OreDictionary.WILDCARD_VALUE), rec.getValue()));
		}
		JEIWrappers.put(BeamExtractorCategory.ID, currentRecipes);

		currentRecipes = new ArrayList<IRecipeWrapper>();
		for(Map.Entry<Predicate<IBlockState>, BeamTransmute> rec : fusionBeamRecipes.entrySet()){
			currentRecipes.add(new FusionBeamRecipe(rec.getKey(), rec.getValue().state, rec.getValue().minPower, false));
		}
		for(Map.Entry<Predicate<IBlockState>, BeamTransmute> rec : vFusionBeamRecipes.entrySet()){
			currentRecipes.add(new FusionBeamRecipe(rec.getKey(), rec.getValue().state, rec.getValue().minPower, true));
		}
		JEIWrappers.put(FusionBeamCategory.ID, currentRecipes);

		currentRecipes = new ArrayList<IRecipeWrapper>();
		for(IReaction react : AlchemyCore.REACTIONS){
			if(react instanceof ITransparentReaction){
				currentRecipes.add(new ReactionRecipe((ITransparentReaction) react));
			}
		}
		JEIWrappers.put(ReactionCategory.ID, currentRecipes);

		currentRecipes = new ArrayList<IRecipeWrapper>();
		for(IReagent reag : AlchemyCore.REAGENTS){
			if(reag != null){
				currentRecipes.add(new ReagInfoRecipe(reag));
			}
		}
		JEIWrappers.put(ReagInfoCategory.ID, currentRecipes);

		currentRecipes = new ArrayList<IRecipeWrapper>();
		for(Entry<Predicate<ItemStack>, ItemStack> rec : stampMillRecipes.entrySet()){
			if(!(rec.getKey() instanceof RecipePredicate) || ((RecipePredicate) (rec.getKey())).getMatchingList().isEmpty()){
				continue;
			}
			currentRecipes.add(new StampMillRecipe(((RecipePredicate<ItemStack>) rec.getKey()).getMatchingList(), rec.getValue()));
		}
		JEIWrappers.put(StampMillCategory.ID, currentRecipes);

		currentRecipes = new ArrayList<IRecipeWrapper>();
		for(Entry<Predicate<ItemStack>, ItemStack> rec : oreCleanserRecipes.entrySet()){
			if(!(rec.getKey() instanceof RecipePredicate) || ((RecipePredicate) (rec.getKey())).getMatchingList().isEmpty()){
				continue;
			}
			currentRecipes.add(new OreCleanserRecipe(((RecipePredicate<ItemStack>) rec.getKey()).getMatchingList(), rec.getValue()));
		}
		JEIWrappers.put(OreCleanserCategory.ID, currentRecipes);

		currentRecipes = new ArrayList<IRecipeWrapper>();
		for(Entry<Predicate<ItemStack>, Pair<FluidStack, Integer>> rec : blastFurnaceRecipes.entrySet()){
			if(!(rec.getKey() instanceof RecipePredicate) || ((RecipePredicate) (rec.getKey())).getMatchingList().isEmpty()){
				continue;
			}
			currentRecipes.add(new BlastFurnaceRecipe(((RecipePredicate<ItemStack>) rec.getKey()).getMatchingList(), rec.getValue().getLeft(), rec.getValue().getRight()));
		}
		JEIWrappers.put(BlastFurnaceCategory.ID, currentRecipes);
	}
}
