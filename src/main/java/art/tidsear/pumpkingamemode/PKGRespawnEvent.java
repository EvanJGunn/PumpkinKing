package art.tidsear.pumpkingamemode;

import art.tidsear.pumpkinking.PumpkinKingMod;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;

public class PKGRespawnEvent {
    @SubscribeEvent
    public void playerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        System.out.println("Player respawned");
        PumpkinKingMod.pkGameMode.OnPlayerRespawn(event.player.getDisplayName());
    }
}
