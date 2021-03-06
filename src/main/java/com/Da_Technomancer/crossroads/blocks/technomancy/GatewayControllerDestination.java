package com.Da_Technomancer.crossroads.blocks.technomancy;

import com.Da_Technomancer.crossroads.API.CRProperties;
import com.Da_Technomancer.crossroads.API.technomancy.IGateway;
import com.Da_Technomancer.crossroads.blocks.CRBlocks;
import com.Da_Technomancer.crossroads.tileentities.technomancy.GatewayControllerDestinationTileEntity;
import com.Da_Technomancer.essentials.ESConfig;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.ContainerBlock;
import net.minecraft.block.material.PushReaction;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class GatewayControllerDestination extends ContainerBlock{

	public GatewayControllerDestination(){
		super(CRBlocks.getMetalProperty());
		String name = "gateway_controller_destination";
		setRegistryName(name);
		CRBlocks.toRegister.add(this);
		CRBlocks.blockAddQue(this);
		setDefaultState(getDefaultState().with(CRProperties.ACTIVE, false));
	}

	@Nullable
	@Override
	public TileEntity createNewTileEntity(IBlockReader worldIn){
		return new GatewayControllerDestinationTileEntity();
	}

	@Override
	public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving){
		TileEntity te;
		if(!worldIn.isRemote && worldIn.isBlockPowered(pos) && (te = worldIn.getTileEntity(pos)) instanceof GatewayControllerDestinationTileEntity){
			((GatewayControllerDestinationTileEntity) te).redstoneInput();
		}
	}

	@Override
	public BlockRenderType getRenderType(BlockState state){
		//If this is formed into a multiblock, we let the TESR on the top handle all rendering
		return state.get(CRProperties.ACTIVE) ? BlockRenderType.ENTITYBLOCK_ANIMATED : BlockRenderType.MODEL;
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder){
		builder.add(CRProperties.ACTIVE);//ACTIVE is whether this is formed into a multiblock
	}

	@Override
	public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult ray){
		ItemStack held = player.getHeldItem(hand);
		if(!state.get(CRProperties.ACTIVE) && ESConfig.isWrench(held)){
			//Attempt to form the multiblock
			TileEntity te = world.getTileEntity(pos);
			if(te instanceof GatewayControllerDestinationTileEntity){
				((GatewayControllerDestinationTileEntity) te).assemble(player);
				return ActionResultType.SUCCESS;
			}
		}
		return ActionResultType.PASS;
	}

	@Override
	public void onReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean isMoving){
		TileEntity te = world.getTileEntity(pos);
		if(newState.getBlock() != state.getBlock() && te instanceof IGateway){
			((IGateway) te).dismantle();//Shutdown the multiblock
		}
		super.onReplaced(state, world, pos, newState, isMoving);
	}

	@Override
	public void addInformation(ItemStack stack, @Nullable IBlockReader world, List<ITextComponent> tooltip, ITooltipFlag flag){
		tooltip.add(new TranslationTextComponent("tt.crossroads.gateway.desc"));
		tooltip.add(new TranslationTextComponent("tt.crossroads.gateway.destination"));
		tooltip.add(new TranslationTextComponent("tt.crossroads.gateway.destination.redial"));
	}

	@Override
	public PushReaction getPushReaction(BlockState state){
		return PushReaction.BLOCK;//Some mods make TileEntities piston moveable. That would be really bad for this block
	}
}
