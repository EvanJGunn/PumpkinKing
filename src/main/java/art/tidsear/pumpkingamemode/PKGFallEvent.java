package art.tidsear.pumpkingamemode;

import art.tidsear.pumpkinking.PumpkinKingMod;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.entity.living.LivingFallEvent;

public class PKGFallEvent {
    @SideOnly(Side.SERVER)
    @SubscribeEvent
    public void onLivingFall(LivingFallEvent event) {
        if (event.entityLiving instanceof EntityPlayerMP) {
            if (PumpkinKingMod.pkGameMode.IsPK(((EntityPlayerMP) event.entityLiving).getDisplayName())) {
                event.distance = 0;
            }
        }
    }
}
