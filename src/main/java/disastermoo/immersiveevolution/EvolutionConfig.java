package disastermoo.immersiveevolution;


import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static disastermoo.immersiveevolution.ImmersiveEvolution.MOD_ID;

@Config(modid = MOD_ID, category = "")
@Mod.EventBusSubscriber(modid = MOD_ID)
@Config.LangKey("config." + MOD_ID)
public final class EvolutionConfig
{
    @Config.Comment("General settings")
    @Config.LangKey("config." + MOD_ID + ".general")
    public static final GeneralCFG GENERAL = new GeneralCFG();

    @Config.Comment("General settings")
    @Config.LangKey("config." + MOD_ID + ".crusher")
    public static final CrusherCFG CRUSHER = new CrusherCFG();

    @Config.Comment("General settings")
    @Config.LangKey("config." + MOD_ID + ".blast_furnace")
    public static final BlastFurnaceCFG BLAST_FURNACE = new BlastFurnaceCFG();

    @SubscribeEvent
    public static void onConfigChangedEvent(ConfigChangedEvent.OnConfigChangedEvent event)
    {
        if (event.getModID().equals(MOD_ID))
        {
            ConfigManager.sync(MOD_ID, Config.Type.INSTANCE);
        }
    }

    public static final class GeneralCFG
    {
        @Config.Comment("Void input on power outage?")
        @Config.LangKey("config." + MOD_ID + ".general.voidInputOnPowerOutage")
        public boolean voidInputOnPowerOutage = false;
    }

    public static final class CrusherCFG
    {
        @Config.Comment("How many ticks are needed to process a recipe of the same tier ?")
        @Config.RangeInt(min = 1)
        @Config.LangKey("config." + MOD_ID + ".crusher.ticks")
        public int ticks = 100;

        @Config.Comment("How much energy this machine can store? This value is a multiplier to the input")
        @Config.RangeInt(min = 1)
        @Config.LangKey("config." + MOD_ID + ".crusher.capacityMultiplier")
        public int capacityMultiplier = 10;

        @Config.Comment("How many RF/t are needed for Mk1 ?")
        @Config.RangeInt(min = 1)
        @Config.LangKey("config." + MOD_ID + ".crusher.energyMk1")
        public int energyMk1 = 160;

        @Config.Comment("How many RF/t are needed for Mk2 ?")
        @Config.RangeInt(min = 1)
        @Config.LangKey("config." + MOD_ID + ".crusher.energyMk2")
        public int energyMk2 = 640;

        @Config.Comment("How many RF/t are needed for Mk3 ?")
        @Config.RangeInt(min = 1)
        @Config.LangKey("config." + MOD_ID + ".crusher.energyMk3")
        public int energyMk3 = 2560;

        @Config.Comment("How many RF/t are needed for Mk4 ?")
        @Config.RangeInt(min = 1)
        @Config.LangKey("config." + MOD_ID + ".crusher.energyMk4")
        public int energyMk4 = 10240;

        @Config.Comment("How many RF/t are needed for Mk5 ?")
        @Config.RangeInt(min = 1)
        @Config.LangKey("config." + MOD_ID + ".crusher.energyMk5")
        public int energyMk5 = 51200;
    }

    public static final class BlastFurnaceCFG
    {
        @Config.Comment("How many ticks are needed to process a recipe of the same tier ?")
        @Config.RangeInt(min = 1)
        @Config.LangKey("config." + MOD_ID + ".blast_furnace.ticks")
        public int ticks = 100;

        @Config.Comment("How much energy this machine can store? This value is a multiplier to the input")
        @Config.RangeInt(min = 1)
        @Config.LangKey("config." + MOD_ID + ".blast_furnace.capacityMultiplier")
        public int capacityMultiplier = 10;

        @Config.Comment("How many RF/t are needed for Mk1 ?")
        @Config.RangeInt(min = 1)
        @Config.LangKey("config." + MOD_ID + ".blast_furnace.energyMk1")
        public int energyMk1 = 160;

        @Config.Comment("How many RF/t are needed for Mk2 ?")
        @Config.RangeInt(min = 1)
        @Config.LangKey("config." + MOD_ID + ".blast_furnace.energyMk2")
        public int energyMk2 = 640;

        @Config.Comment("How many RF/t are needed for Mk3 ?")
        @Config.RangeInt(min = 1)
        @Config.LangKey("config." + MOD_ID + ".blast_furnace.energyMk3")
        public int energyMk3 = 2560;

        @Config.Comment("How many RF/t are needed for Mk4 ?")
        @Config.RangeInt(min = 1)
        @Config.LangKey("config." + MOD_ID + ".blast_furnace.energyMk4")
        public int energyMk4 = 10240;

        @Config.Comment("How many RF/t are needed for Mk5 ?")
        @Config.RangeInt(min = 1)
        @Config.LangKey("config." + MOD_ID + ".blast_furnace.energyMk5")
        public int energyMk5 = 51200;
    }
}
