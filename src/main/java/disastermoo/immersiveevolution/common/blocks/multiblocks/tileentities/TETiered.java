package disastermoo.immersiveevolution.common.blocks.multiblocks.tileentities;

import java.util.Iterator;

import net.minecraft.nbt.NBTTagCompound;

import blusunrize.immersiveengineering.api.ApiUtils;
import blusunrize.immersiveengineering.api.MultiblockHandler;
import blusunrize.immersiveengineering.api.crafting.IMultiblockRecipe;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityMultiblockMetal;
import disastermoo.immersiveevolution.EvolutionConfig;

public abstract class TETiered<T extends TETiered<T, R>, R extends IMultiblockRecipe> extends TileEntityMultiblockMetal<T, R>
{
    protected int cooldownRunOutEnergy;

    public TETiered(MultiblockHandler.IMultiblock multiblockInstance, int[] structureDimensions, int energyCapacity, boolean redstoneControl)
    {
        super(multiblockInstance, structureDimensions, energyCapacity, redstoneControl);
        cooldownRunOutEnergy = 0;
    }

    @Override
    public void readCustomNBT(NBTTagCompound nbt, boolean descPacket)
    {
        super.readCustomNBT(nbt, descPacket);
        cooldownRunOutEnergy = nbt.getInteger("cooldownRunOutEnergy");
    }

    @Override
    public void writeCustomNBT(NBTTagCompound nbt, boolean descPacket)
    {
        super.writeCustomNBT(nbt, descPacket);
        nbt.setInteger("cooldownRunOutEnergy", cooldownRunOutEnergy);
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
