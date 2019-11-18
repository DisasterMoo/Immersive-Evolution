package disastermoo.immersiveevolution;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLFingerprintViolationEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import org.apache.logging.log4j.Logger;

@Mod(modid = ImmersiveEvolution.MODID, name = ImmersiveEvolution.NAME, version = ImmersiveEvolution.VERSION, dependencies = "required-after:immersiveengineering@[0.12,);", certificateFingerprint = ImmersiveEvolution.SIGNING_KEY)
public class ImmersiveEvolution
{
    public static final String MODID = "immersiveevolution";
    public static final String NAME = "Immersive Evolution";
    public static final String VERSION = "@VERSION@";
    public static final String SIGNING_KEY = "@FINGERPRINT@";

    private static Logger logger;

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
        logger.warn("Invalid fingerprint detected! This means this jar file has been modified externally and is not supported");
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        // TOP initialization (if present) todo
        //FMLInterModComms.sendFunctionMessage("theoneprobe", "getTheOneProbe", "tfctech.compat.waila.TOPPlugin");
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        logger = event.getModLog();
        //NetworkRegistry.INSTANCE.registerGuiHandler(this, new TechGuiHandler());
        network = NetworkRegistry.INSTANCE.newSimpleChannel(MODID);
        int id = 0;
        //network.registerMessage(new PacketTileEntityUpdate.Handler(), PacketTileEntityUpdate.class, ++id, Side.CLIENT);
    }
}
