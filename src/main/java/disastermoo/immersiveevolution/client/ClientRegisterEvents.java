package disastermoo.immersiveevolution.client;

import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import disastermoo.immersiveevolution.client.render.TESRCrusherTiered;
import disastermoo.immersiveevolution.common.blocks.multiblocks.tileentities.TECrusherTiered;

import static disastermoo.immersiveevolution.ImmersiveEvolution.MOD_ID;

@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber(value = Side.CLIENT, modid = MOD_ID)
public final class ClientRegisterEvents
{
    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event)
    {
        // TESRs //
        ClientRegistry.bindTileEntitySpecialRenderer(TECrusherTiered.class, new TESRCrusherTiered());
    }
}
