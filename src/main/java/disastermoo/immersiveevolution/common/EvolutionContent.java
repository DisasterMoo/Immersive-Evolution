package disastermoo.immersiveevolution.common;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.Mod;

import blusunrize.immersiveengineering.api.MultiblockHandler;
import blusunrize.immersiveengineering.common.blocks.BlockIEBase;
import disastermoo.immersiveevolution.common.blocks.multiblocks.EnumTier;
import disastermoo.immersiveevolution.common.blocks.multiblocks.TieredCrusher;
import disastermoo.immersiveevolution.common.blocks.multiblocks.tileentities.TETieredCrusher;

import static disastermoo.immersiveevolution.ImmersiveEvolution.MOD_ID;

@Mod.EventBusSubscriber(modid = MOD_ID)
public final class EvolutionContent
{
    public static BlockIEBase<EnumTier> CRUSHER;

    static
    {
        CRUSHER = new TieredCrusher.CrusherBlock();
    }

    public static void preInit()
    {
    }

    public static void init()
    {
        registerTE(TETieredCrusher.class, "TECrusherMk1");
        EvolutionRecipes.init();
    }

    private static <T extends TileEntity> void registerTE(Class<T> te, String name)
    {
        TileEntity.register(MOD_ID + ":" + name, te);
        for (EnumTier tier : EnumTier.values())
        {
            MultiblockHandler.registerMultiblock(TieredCrusher.getInstance(tier));
        }
    }
}
