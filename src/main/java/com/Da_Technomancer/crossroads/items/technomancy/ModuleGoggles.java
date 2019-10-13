package com.Da_Technomancer.crossroads.items.technomancy;

import com.Da_Technomancer.crossroads.API.MiscUtil;
import com.Da_Technomancer.crossroads.API.packets.CrossroadsPackets;
import com.Da_Technomancer.crossroads.API.packets.SendChatToClient;
import com.Da_Technomancer.crossroads.API.technomancy.EnumGoggleLenses;
import com.Da_Technomancer.crossroads.Crossroads;
import com.Da_Technomancer.crossroads.items.CRItems;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class ModuleGoggles extends ArmorItem{

	private static final IArmorMaterial TECHNOMANCY_MAT = new TechnoMat();

	public ModuleGoggles(){
		super(TECHNOMANCY_MAT, EquipmentSlotType.HEAD, CRItems.itemProp.maxStackSize(1));
		String name = "module_goggles";
		setRegistryName(name);
		CRItems.toRegister.add(this);
	}

	/**
	 * Value chosen at random.
	 */
	private static final int CHAT_ID = 718749;

	@Override
	public void onArmorTick(ItemStack stack, World world, PlayerEntity player){
		CompoundNBT nbt;
		if(!world.isRemote && (nbt = stack.getTag()) != null){
			ArrayList<ITextComponent> chat = new ArrayList<>();
			BlockRayTraceResult ray = MiscUtil.rayTrace(player, 8);
			for(EnumGoggleLenses lens : EnumGoggleLenses.values()){
				if(nbt.getBoolean(lens.name())){
					lens.doEffect(world, player, chat, ray);
				}
			}
			if(!chat.isEmpty()){
				CrossroadsPackets.sendPacketToPlayer((ServerPlayerEntity) player, new SendChatToClient(chat, CHAT_ID));
			}
		}
	}

	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn){
		tooltip.add("Lenses:");
		if(stack.hasTag()){
			for(EnumGoggleLenses lens : EnumGoggleLenses.values()){
				if(stack.getTag().contains(lens.name())){
					if(lens.shouldShowState()){
						tooltip.add('-' + lens.name() + "-" + (stack.getTag().getBoolean(lens.name()) ? "ENABLED" : "DISABLED"));
					}else{
						tooltip.add('-' + lens.name());
					}
				}
			}
		}else{
			tooltip.add("-NONE");
		}
	}

	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlotType slot, String type){
		StringBuilder path = new StringBuilder(Crossroads.MODID + ":textures/models/armor/goggles/goggle");
		if(stack.hasTag()){
			for(EnumGoggleLenses lens : EnumGoggleLenses.values()){
				if(stack.getTag().contains(lens.name())){
					path.append(lens.getTexturePath());
				}
			}
		}
		path.append(".png");
		return path.toString();
	}

	private static class TechnoMat implements IArmorMaterial{

		@Override
		public int getDurability(EquipmentSlotType slotIn){
			return 0;
		}

		@Override
		public int getDamageReductionAmount(EquipmentSlotType slotIn){
			return 0;
		}

		@Override
		public int getEnchantability(){
			return 0;
		}

		@Override
		public SoundEvent getSoundEvent(){
			return SoundEvents.ITEM_ARMOR_EQUIP_IRON;
		}

		@Override
		public Ingredient getRepairMaterial(){
			return Ingredient.EMPTY;
		}

		@Override
		public String getName(){
			return "technomancy";
		}

		@Override
		public float getToughness(){
			return 0;
		}
	}
}
