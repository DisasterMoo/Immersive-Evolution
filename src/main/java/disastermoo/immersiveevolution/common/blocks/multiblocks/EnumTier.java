package disastermoo.immersiveevolution.common.blocks.multiblocks;

import java.util.Locale;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IStringSerializable;

import blusunrize.immersiveengineering.common.blocks.BlockIEBase;

public enum EnumTier implements IStringSerializable, BlockIEBase.IBlockEnum
{
    MARK_I,
    MARK_II,
    MARK_III,
    MARK_IV,
    MARK_V;

    public static EnumTier fromMeta(int meta)
    {
        return EnumTier.values()[meta];
    }

    public static EnumTier fromNBT(NBTTagCompound nbt)
    {
        if (nbt.hasKey("tier"))
        {
            return fromMeta(nbt.getInteger("tier"));
        }
        return MARK_I;
    }

    @Override
    public String getName()
    {
        return this.toString().toLowerCase(Locale.ENGLISH);
    }

    @Override
    public int getMeta()
    {
        return this.ordinal();
    }

    @Override
    public boolean listForCreative()
    {
        return false;
    }

    public boolean isAtLeast(EnumTier tier)
    {
        return this.ordinal() >= tier.ordinal();
    }

    public boolean isAtMost(EnumTier tier)
    {
        return this.ordinal() <= tier.ordinal();
    }

    public void toNBT(NBTTagCompound nbt)
    {
        nbt.setInteger("tier", getMeta());
    }
}
