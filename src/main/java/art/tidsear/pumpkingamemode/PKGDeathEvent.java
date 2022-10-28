package art.tidsear.pumpkingamemode;

import art.tidsear.pumpkinking.PumpkinKingMod;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.EntityDamageSource;
import net.minecraftforge.event.entity.living.LivingDeathEvent;

public class PKGDeathEvent {
    @SubscribeEvent
    public void onLivingDeath(LivingDeathEvent lde) {
        // Could maybe switch on instanceof? idk never done that kind of thing

        // A player has died
        if (lde.entity instanceof EntityPlayerMP) {
            System.out.println("Player died");
            PumpkinKingMod.pkGameMode.OnPlayerDeath(((EntityPlayerMP) lde.entity).getDisplayName());

            // Also send
            if (lde.source.getEntity() instanceof EntityPlayerMP) {
                PumpkinKingMod.pkGameMode.OnPlayerPlayerKill(((EntityPlayerMP) lde.source.getEntity()).getDisplayName(),((EntityPlayerMP) lde.entity).getDisplayName());
            }
            return;
        }

        // Player killed non player entity
        if (lde.source.getEntity() instanceof EntityPlayerMP) {
            PumpkinKingMod.pkGameMode.OnPlayerEntityKill(((EntityPlayerMP) lde.source.getEntity()).getDisplayName(), lde.entity.getUniqueID());
        }


        // If the player didn't kill the entity,
        PumpkinKingMod.pkGameMode.OnPlayerlessEntityDeath(lde.entity.getUniqueID());
    }
}
