package disastermoo.immersiveevolution.common;

import java.util.HashMap;
import java.util.UUID;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import blusunrize.immersiveengineering.api.Lib;
import disastermoo.immersiveevolution.common.blocks.multiblocks.tileentities.TETieredCrusher;

import static disastermoo.immersiveevolution.ImmersiveEvolution.MOD_ID;

@Mod.EventBusSubscriber(modid = MOD_ID)
public final class EventHandler
{
    private static HashMap<UUID, TETieredCrusher> crusherMap = new HashMap<>();

    public static void addCrushingEntity(EntityLivingBase entity, TETieredCrusher te)
    {
        crusherMap.put(entity.getUniqueID(), te);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onLivingDropsLowest(LivingDropsEvent event)
    {
        if (!event.isCanceled() && Lib.DMG_Crusher.equals(event.getSource().getDamageType()))
        {
            TETieredCrusher crusher = crusherMap.get(event.getEntityLiving().getUniqueID());
            if (crusher != null)
            {
                for (EntityItem item : event.getDrops())
                    if (item != null && !item.getItem().isEmpty())
                        crusher.doProcessOutput(item.getItem());
                crusherMap.remove(event.getEntityLiving().getUniqueID());
                event.setCanceled(true);
            }
        }
    }
}
