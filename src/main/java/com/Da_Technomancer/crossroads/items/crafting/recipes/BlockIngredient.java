package com.Da_Technomancer.crossroads.items.crafting.recipes;

import com.Da_Technomancer.crossroads.Crossroads;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.*;
import java.util.function.Predicate;

/**
 * This class is a bare-bones block version of net.minecraft.item.crafting.Ingredient
 * In the likely event that either vanilla or Forge adds a standard way to put blocks in recipe inputs, we will switch to that immediately
 */
public class BlockIngredient implements Predicate<BlockState>{

	private final List<IBlockList> keys;
	private boolean cacheValid = false;//Currently nothing invalidates the cache
	private final Collection<Block> matched = new HashSet<>();

	/**
	 * Everything in matched should be either a block tag, a block, an IBlockList, or a blockstate
	 * @param matched Everything this ingredient should match
	 */
	public BlockIngredient(Object... matched){
		if(matched.length == 1 && matched[0].getClass().isArray()){
			//Because of the unusually vague parameters for the constructor, it's easy to accidentally pass an array of values as an array of the array (due to it being a varArgs)
			//This detects that case, and corrects it rather than throwing an error
			matched = (Object[]) matched[0];
		}

		keys = new ArrayList<>(matched.length);
		for(Object key : matched){
			if(key instanceof IBlockList){
				keys.add((IBlockList) key);
			}else if(key instanceof Tag){
				try{
					Tag<Block> tag = (Tag<Block>) key;
					keys.add(new TagList(tag));
				}catch(ClassCastException e){
					Crossroads.logger.error("An illegal tag type was added to a BlockIngredient. Report to mod author!", e);
					throw e;
				}
			}else if(key instanceof Block){
				keys.add(new SingleList((Block) key));
			}else if(key instanceof BlockState){
				keys.add(new SingleList(((BlockState) key).getBlock()));
			}else{
				JsonParseException e = new JsonParseException("Illegal type added to BlockIngredient; Type: " + key.getClass() + "; Value: " + key.toString());
				Crossroads.logger.error("An illegal value was added to a BlockIngredient. Report to mod author!", e);
				throw e;
			}
		}
	}

	private void updateCache(){
		if(!cacheValid){
			matched.clear();
			keys.forEach(key -> matched.addAll(key.getMatched()));
			cacheValid = true;
		}
	}

	public void writeToBuffer(PacketBuffer buf){
		updateCache();
		buf.writeVarInt(matched.size());//Write how many Blocks this matches
		for(Block b : matched){
			buf.writeResourceLocation(b.getRegistryName());//Write the registry name of every matched block.
		}
	}

	public static BlockIngredient readFromBuffer(PacketBuffer buf){
		int count = buf.readVarInt();
		Block[] matched = new Block[count];
		for(int i = 0; i < count; i++){
			matched[i] = ForgeRegistries.BLOCKS.getValue(buf.readResourceLocation());
		}
		//Create a block ingredient with one large IBlockList that matches every block. Note this doesn't preserve Tag associations of the original definition
		return new BlockIngredient(new BlockList(matched));
	}

	public static BlockIngredient readFromJSON(JsonElement o){
		if(o.isJsonArray()){
			JsonArray array = (JsonArray) o;
			IBlockList[] lists = new IBlockList[array.size()];
			for(int i = 0; i < array.size(); i++){
				JsonElement el = array.get(i);
				if(el.isJsonObject()){
					lists[i] = readIngr((JsonObject) el);
				}else{
					throw new JsonParseException("Value in JSON array instead of JSON object");
				}
			}
			return new BlockIngredient((Object[]) lists);
		}else if(o.isJsonObject()){
			return new BlockIngredient(readIngr((JsonObject) o));
		}else{
			throw new JsonParseException("Value passed to BlockIngredient");
		}
	}

	private static IBlockList readIngr(JsonObject o){
		if(o.has("tag")){
			return new TagList(new BlockTags.Wrapper(new ResourceLocation(JSONUtils.getString(o, "tag"))));
		}else if(o.has("block")){
			return new SingleList(ForgeRegistries.BLOCKS.getValue(new ResourceLocation(JSONUtils.getString(o, "block"))));
		}else{
			throw new JsonParseException("No value defined in BlockIngredient");
		}
	}

	@Override
	public boolean test(BlockState blockState){
		updateCache();
		Block b = blockState.getBlock();
		return matched.contains(b);
	}

	private interface IBlockList{

		Collection<Block> getMatched();

	}

	private static class SingleList implements IBlockList{

		private final List<Block> matchL;

		public SingleList(Block matched){
			matchL = new ArrayList<>(1);
			matchL.add(matched);
			if(matched == null){
				throw new JsonParseException("No defined block in BlockIngredient");
			}
		}

		@Override
		public Collection<Block> getMatched(){
			return matchL;
		}
	}

	private static class TagList implements IBlockList{

		private final Tag<Block> tag;

		public TagList(Tag<Block> matched){
			tag = matched;
		}

		@Override
		public Collection<Block> getMatched(){
			return tag.getAllElements();
		}
	}

	private static class BlockList implements IBlockList{

		private final Collection<Block> blocks;

		public BlockList(Block... matched){
			blocks = Arrays.asList(matched);
		}

		@Override
		public Collection<Block> getMatched(){
			return blocks;
		}
	}
}