package disastermoo.immersiveevolution.common;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.Mod;

import disastermoo.immersiveevolution.common.blocks.multiblocks.tileentities.TECrusherTiered;

import static disastermoo.immersiveevolution.ImmersiveEvolution.MOD_ID;

@Mod.EventBusSubscriber(modid = MOD_ID)
public final class EvolutionContent
{
    public static void init()
    {
        registerTE(TECrusherTiered.class, "TECrusherMk1");
    }

    private static <T extends TileEntity> void registerTE(Class<T> te, String name)
    {
        TileEntity.register(MOD_ID + ":" + name, te);
    }
}
