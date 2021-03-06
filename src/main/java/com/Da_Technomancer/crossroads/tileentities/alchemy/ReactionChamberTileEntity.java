package com.Da_Technomancer.crossroads.tileentities.alchemy;


import com.Da_Technomancer.crossroads.API.Capabilities;
import com.Da_Technomancer.crossroads.API.alchemy.*;
import com.Da_Technomancer.crossroads.API.heat.HeatUtil;
import com.Da_Technomancer.crossroads.API.heat.IHeatHandler;
import com.Da_Technomancer.crossroads.Crossroads;
import com.Da_Technomancer.crossroads.blocks.alchemy.ReactionChamber;
import com.Da_Technomancer.crossroads.render.CRRenderUtil;
import com.Da_Technomancer.crossroads.tileentities.electric.TeslaCoilTopTileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.registries.ObjectHolder;

@ObjectHolder(Crossroads.MODID)
public class ReactionChamberTileEntity extends AlchemyReactorTE{

	@ObjectHolder("reaction_chamber")
	private static TileEntityType<ReactionChamberTileEntity> type = null;

	private int energy = 0;
	private static final int ENERGY_CAPACITY = 20;
	public static final int DRAIN = 10;
	public static final int CAPACITY = 200;

	public ReactionChamberTileEntity(){
		super(type);
	}

	public ReactionChamberTileEntity(boolean glass){
		super(type, glass);
	}

	@Override
	protected void initHeat(){
		if(!init){
			init = true;
			cableTemp = HeatUtil.convertBiomeTemp(world, pos);
		}
	}

	public ReagentMap getMap(){
		return contents;
	}

	public void writeContentNBT(ItemStack stack){
		contents = ReactionChamber.getReagants(stack);
		dirtyReag = true;
	}

	public float getReds(){
		return Math.min(CAPACITY, contents.getTotalQty());
	}

	@Override
	public void tick(){
		if(world.isRemote){
			return;
		}

		if(energy >= DRAIN){
			energy -= DRAIN;
			//Spawn random electric arcs between the walls of the block
			if(world.rand.nextInt(10) == 0){
				Vector3d[] arcPos = new Vector3d[2];
				for(int i = 0; i < 2; i++){
					float u = world.rand.nextFloat() * 0.8F + 0.1F;
					float v = world.rand.nextFloat() * 0.8F + 0.1F;
					switch(world.rand.nextInt(6)){
						case 0:
							arcPos[i] = new Vector3d(u, v, 0.1);
							break;
						case 1:
							arcPos[i] = new Vector3d(u, v, 0.9);
							break;
						case 2:
							arcPos[i] = new Vector3d(u, 0.1, v);
							break;
						case 3:
							arcPos[i] = new Vector3d(u, 0.9, v);
							break;
						case 4:
							arcPos[i] = new Vector3d(0.1, u, v);
							break;
						case 5:
							arcPos[i] = new Vector3d(0.9, u, v);
							break;
					}
				}
				CRRenderUtil.addArc(world, arcPos[0].add(pos.getX(), pos.getY(), pos.getZ()), arcPos[1].add(pos.getX(), pos.getY(), pos.getZ()), 1, 0F, TeslaCoilTopTileEntity.COLOR_CODES[(int) (world.getGameTime() % 3)]);
			}
		}

		super.tick();
	}

	@Override
	public boolean isCharged(){
		return energy >= 10 || super.isCharged();
	}

	@Override
	public int transferCapacity(){
		return CAPACITY;
	}

	@Override
	protected boolean useCableHeat(){
		return true;
	}

	@Override
	protected EnumTransferMode[] getModes(){
		return new EnumTransferMode[] {EnumTransferMode.BOTH, EnumTransferMode.BOTH, EnumTransferMode.BOTH, EnumTransferMode.BOTH, EnumTransferMode.BOTH, EnumTransferMode.BOTH};
	}

	@Override
	protected void performTransfer(){
		EnumTransferMode[] modes = getModes();
		for(int i = 0; i < 6; i++){
			if(modes[i].isOutput()){
				Direction side = Direction.byIndex(i);
				TileEntity te = world.getTileEntity(pos.offset(side));
				LazyOptional<IChemicalHandler> otherOpt;
				if(contents.getTotalQty() <= 0 || te == null || !(otherOpt = te.getCapability(Capabilities.CHEMICAL_CAPABILITY, side.getOpposite())).isPresent()){
					continue;
				}

				IChemicalHandler otherHandler = otherOpt.orElseThrow(NullPointerException::new);
				EnumContainerType cont = otherHandler.getChannel(side.getOpposite());
				if(otherHandler.getMode(side.getOpposite()) == EnumTransferMode.BOTH && modes[i] == EnumTransferMode.BOTH){
					continue;
				}

				if(contents.getTotalQty() != 0){
					if(otherHandler.insertReagents(contents, side.getOpposite(), handler)){
						correctReag();
						markDirty();
					}
				}
			}
		}
	}

	@Override
	public void read(BlockState state, CompoundNBT nbt){
		super.read(state, nbt);
		energy = nbt.getInt("ener");
	}

	@Override
	public CompoundNBT write(CompoundNBT nbt){
		super.write(nbt);
		nbt.putInt("ener", energy);
		return nbt;
	}

	@Override
	public void remove(){
		super.remove();
		heatOpt.invalidate();
		itemOpt.invalidate();
		energyOpt.invalidate();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side){
		if(cap == Capabilities.CHEMICAL_CAPABILITY){
			return (LazyOptional<T>) chemOpt;
		}
		if(cap == Capabilities.HEAT_CAPABILITY){
			return (LazyOptional<T>) heatOpt;
		}
		if(cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY){
			return (LazyOptional<T>) itemOpt;
		}
		if(cap == CapabilityEnergy.ENERGY && (side == null || side.getAxis() != Direction.Axis.Y)){
			return (LazyOptional<T>) energyOpt;
		}
		return super.getCapability(cap, side);
	}

	private final LazyOptional<IHeatHandler> heatOpt = LazyOptional.of(HeatHandler::new);
	private final LazyOptional<IItemHandler> itemOpt = LazyOptional.of(ItemHandler::new);
	private final LazyOptional<IEnergyStorage> energyOpt  = LazyOptional.of(EnergyHandler::new);

	private class EnergyHandler implements IEnergyStorage{

		@Override
		public int receiveEnergy(int maxReceive, boolean simulate){
			int toMove = Math.min(ENERGY_CAPACITY - energy, maxReceive);

			if(!simulate && toMove > 0){
				energy += toMove;
				markDirty();
			}

			return toMove;
		}

		@Override
		public int extractEnergy(int maxExtract, boolean simulate){
			return 0;
		}

		@Override
		public int getEnergyStored(){
			return energy;
		}

		@Override
		public int getMaxEnergyStored(){
			return ENERGY_CAPACITY;
		}

		@Override
		public boolean canExtract(){
			return false;
		}

		@Override
		public boolean canReceive(){
			return true;
		}
	}

	private class HeatHandler implements IHeatHandler{

		@Override
		public double getTemp(){
			initHeat();
			return cableTemp;
		}

		@Override
		public void setTemp(double tempIn){
			init = true;
			cableTemp = tempIn;
			dirtyReag = true;
			markDirty();
		}

		@Override
		public void addHeat(double tempChange){
			initHeat();
			cableTemp += tempChange;
			dirtyReag = true;
			markDirty();
		}
	}
}
