package com.Da_Technomancer.crossroads.items.crafting.recipes;

import com.Da_Technomancer.crossroads.items.CRItems;
import com.Da_Technomancer.crossroads.items.crafting.CraftingUtil;
import com.Da_Technomancer.crossroads.items.crafting.RecipeHolder;
import com.google.gson.JsonObject;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;

public class BoboRec implements IRecipe<IInventory>{

	private final ResourceLocation id;
	private final String group;
	private final Ingredient[] ingr;
	private final ItemStack output;

	public BoboRec(ResourceLocation location, String name, Ingredient[] input, ItemStack output){
		id = location;
		group = name;
		ingr = input;
		this.output = output;
	}

	@Override
	public boolean matches(IInventory inv, World worldIn){
		if(inv.getSizeInventory() != 3){
			return false;
		}
		//Known issue: this will pass if one input meets 2+ ingredients, even if the third input is irrelevant
		//No default Crossroads recipes have this issue- it would be silly to add a recipe that does
		for(Ingredient input : ingr){
			boolean pass = false;
			for(int i = 0; i < 3; i++){
				if(input.test(inv.getStackInSlot(i))){
					pass = true;
					break;
				}
			}
			if(!pass){
				return false;
			}
		}
		return true;
	}

	@Override
	public NonNullList<Ingredient> getIngredients(){
		NonNullList<Ingredient> nonnulllist = NonNullList.create();
		nonnulllist.add(ingr[0]);
		nonnulllist.add(ingr[1]);
		nonnulllist.add(ingr[2]);
		return nonnulllist;
	}

	@Override
	public ItemStack getCraftingResult(IInventory inv){
		return getRecipeOutput().copy();
	}

	@Override
	public boolean canFit(int width, int height){
		return true;
	}

	@Override
	public ItemStack getRecipeOutput(){
		return output;
	}

	@Override
	public ItemStack getIcon(){
		return new ItemStack(CRItems.boboRod);
	}

	@Override
	public ResourceLocation getId(){
		return id;
	}

	@Override
	public String getGroup(){
		return group;
	}

	@Override
	public IRecipeSerializer<?> getSerializer(){
		return RecipeHolder.BOBO_SERIAL;
	}

	@Override
	public IRecipeType<?> getType(){
		return RecipeHolder.BOBO_TYPE;
	}

	public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<BoboRec>{

		@Override
		public BoboRec read(ResourceLocation recipeId, JsonObject json){
			//Normal specification of recipe group and ingredient
			String s = JSONUtils.getString(json, "group", "");

			//3 inputs, named input_a, input_b, and input_c
			Ingredient[] ingr = new Ingredient[3];
			ingr[0] = CraftingUtil.getIngredient(json, "input_a", false);
			ingr[1] = CraftingUtil.getIngredient(json, "input_b", false);
			ingr[2] = CraftingUtil.getIngredient(json, "input_c", false);

			//Specify output
			ItemStack output = CraftingUtil.getItemStack(json, "output", false, true);
			return new BoboRec(recipeId, s, ingr, output);
		}

		@Nullable
		@Override
		public BoboRec read(ResourceLocation recipeId, PacketBuffer buffer){
			String s = buffer.readString(Short.MAX_VALUE);
			Ingredient[] inputs = new Ingredient[3];
			for(int i = 0; i < 3; i++){
				inputs[i] = Ingredient.read(buffer);
			}
			ItemStack output = buffer.readItemStack();
			return new BoboRec(recipeId, s, inputs, output);
		}

		@Override
		public void write(PacketBuffer buffer, BoboRec recipe){
			buffer.writeString(recipe.getGroup());
			for(Ingredient ingr : recipe.ingr){
				ingr.write(buffer);
			}
			buffer.writeItemStack(recipe.output);
		}
	}
}
