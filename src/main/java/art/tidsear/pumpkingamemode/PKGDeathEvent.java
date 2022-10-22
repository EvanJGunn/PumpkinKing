package art.tidsear.pumpkingamemode;

import art.tidsear.pumpkinking.PumpkinKingMod;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.entity.living.LivingDeathEvent;

public class PKGDeathEvent {
    @SubscribeEvent
    public void onLivingDeath(LivingDeathEvent lde) {
        System.out.println("Player died");
        if (lde.entity instanceof EntityPlayerMP) {
            PumpkinKingMod.pkGameMode.OnPlayerDeath(((EntityPlayerMP) lde.entity).getDisplayName());
        }
    }
}
