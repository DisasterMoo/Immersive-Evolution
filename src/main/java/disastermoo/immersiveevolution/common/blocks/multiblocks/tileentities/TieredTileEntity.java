package disastermoo.immersiveevolution.common.blocks.multiblocks.tileentities;

import java.util.Iterator;

import net.minecraft.nbt.NBTTagCompound;

import blusunrize.immersiveengineering.api.ApiUtils;
import blusunrize.immersiveengineering.api.MultiblockHandler;
import blusunrize.immersiveengineering.api.crafting.IMultiblockRecipe;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityMultiblockMetal;
import disastermoo.immersiveevolution.EvolutionConfig;
import disastermoo.immersiveevolution.common.blocks.multiblocks.EnumTier;

@SuppressWarnings("WeakerAccess")
public abstract class TieredTileEntity<T extends TieredTileEntity<T, R>, R extends IMultiblockRecipe> extends TileEntityMultiblockMetal<T, R>
{
    protected int cooldownRunOutEnergy;
    protected EnumTier tier;

    public TieredTileEntity(MultiblockHandler.IMultiblock multiblockInstance, int[] structureDimensions, int energyCapacity, boolean redstoneControl, EnumTier tier)
    {
        super(multiblockInstance, structureDimensions, energyCapacity, redstoneControl);
        this.tier = tier;
        cooldownRunOutEnergy = 0;
    }

    @Override
    public void readCustomNBT(NBTTagCompound nbt, boolean descPacket)
    {
        super.readCustomNBT(nbt, descPacket);
        this.tier = EnumTier.fromMeta(nbt.getInteger("tier"));
        cooldownRunOutEnergy = nbt.getInteger("cooldownRunOutEnergy");
    }

    @Override
    public void writeCustomNBT(NBTTagCompound nbt, boolean descPacket)
    {
        super.writeCustomNBT(nbt, descPacket);
        nbt.setInteger("tier", tier.getMeta());
        nbt.setInteger("cooldownRunOutEnergy", cooldownRunOutEnergy);
    }

    public EnumTier getTier()
    {
        return tier;
    }

    @Override
    public void update()
    {
        ApiUtils.checkForNeedlessTicking(this);
        tickedProcesses = 0;
        if (cooldownRunOutEnergy-- < 0) cooldownRunOutEnergy = 0;
        if (world.isRemote || isDummy() || isRSDisabled() || cooldownRunOutEnergy > 0)
            return;

        int max = getMaxProcessPerTick();
        int i = 0;
        Iterator<MultiblockProcess<R>> processIterator = processQueue.iterator();
        tickedProcesses = 0;
        while (processIterator.hasNext() && i++ < max)
        {
            MultiblockProcess<R> process = processIterator.next();
            if (process.canProcess(this))
            {
                process.doProcessTick(this);
                tickedProcesses++;
                //noinspection ConstantConditions
                updateMasterBlock(null, true);
            }
            else if (this.energyStorage.extractEnergy(process.energyPerTick, true) < process.energyPerTick)
            {
                // Reset progress todo play sound
                process.processTick = 0;
                cooldownRunOutEnergy = 100;
                if (EvolutionConfig.GENERAL.voidInputOnPowerOutage)
                {
                    process.clearProcess = true;
                }
            }
            if (process.clearProcess)
                processIterator.remove();
        }
    }

    @Override
    public boolean shouldRenderAsActive()
    {
        return super.shouldRenderAsActive() && cooldownRunOutEnergy <= 0;
    }
}
