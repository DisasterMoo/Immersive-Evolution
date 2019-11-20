package disastermoo.immersiveevolution;

import org.apache.logging.log4j.Logger;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLFingerprintViolationEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;

import disastermoo.immersiveevolution.common.EvolutionContent;

@SuppressWarnings("WeakerAccess")
@Mod(modid = ImmersiveEvolution.MOD_ID, name = ImmersiveEvolution.NAME, version = ImmersiveEvolution.VERSION, dependencies = "required-after:immersiveengineering@[0.12,);", certificateFingerprint = ImmersiveEvolution.SIGNING_KEY)
public class ImmersiveEvolution
{
    public static final String MOD_ID = "immersiveevolution";
    public static final String NAME = "Immersive Evolution";
    public static final String VERSION = "@VERSION@";
    public static final String SIGNING_KEY = "@FINGERPRINT@";

    private static Logger logger;
    private static boolean signedBuild = true;

    private SimpleNetworkWrapper network;

    @Mod.Instance
    private static ImmersiveEvolution instance = null;

    public static SimpleNetworkWrapper getNetwork()
    {
        return instance.network;
    }

    public static Logger getLog()
    {
        return logger;
    }

    public static ImmersiveEvolution getInstance()
    {
        return instance;
    }

    @EventHandler
    public void onFingerprintViolation(FMLFingerprintViolationEvent event)
    {
        if (!event.isDirectory())
        {
            signedBuild = false;
        }
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        // TOP initialization (if present) todo
        //FMLInterModComms.sendFunctionMessage("theoneprobe", "getTheOneProbe", "tfctech.compat.waila.TOPPlugin");
        EvolutionContent.init();
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        logger = event.getModLog();
        //NetworkRegistry.INSTANCE.registerGuiHandler(this, new TechGuiHandler());
        network = NetworkRegistry.INSTANCE.newSimpleChannel(MOD_ID);
        int id = 0;
        //network.registerMessage(new PacketTileEntityUpdate.Handler(), PacketTileEntityUpdate.class, ++id, Side.CLIENT);
        if (!signedBuild)
        {
            logger.error("INVALID FINGERPRINT DETECTED! This means this jar file has been compromised and are not supported.");
        }
        EvolutionContent.preInit();
    }
}
