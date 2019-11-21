package disastermoo.immersiveevolution.common;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.Mod;

import blusunrize.immersiveengineering.api.MultiblockHandler;
import blusunrize.immersiveengineering.common.blocks.BlockIEBase;
import blusunrize.immersiveengineering.common.blocks.metal.BlockTypes_MetalMultiblock;
import disastermoo.immersiveevolution.common.blocks.TieredMultiblocks;
import disastermoo.immersiveevolution.common.blocks.multiblocks.TieredCrusher;
import disastermoo.immersiveevolution.common.blocks.multiblocks.tileentities.TETieredCrusher;

import static disastermoo.immersiveevolution.ImmersiveEvolution.MOD_ID;

@Mod.EventBusSubscriber(modid = MOD_ID)
public final class EvolutionContent
{
    public static BlockIEBase<BlockTypes_MetalMultiblock> MULTIBLOCKS;

    static
    {
        MULTIBLOCKS = new TieredMultiblocks();
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
        for (int i = 1; i <= 5; i++)
        {
            MultiblockHandler.registerMultiblock(TieredCrusher.getInstance(i));
        }
    }
}
