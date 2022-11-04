package art.tidsear.pumpkingamemode;

import art.tidsear.pumpkinking.PumpkinKingMod;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;

public class PKGPlayerEvents {
    @SideOnly(Side.SERVER)
    @SubscribeEvent
    public void playerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        System.out.println("Player respawned");
        PumpkinKingMod.pkGameMode.OnPlayerRespawn(event.player.getDisplayName());
    }

    @SideOnly(Side.SERVER)
    @SubscribeEvent
    public void playerLoggedInEvent(PlayerEvent.PlayerLoggedInEvent event) {
        PumpkinKingMod.pkGameMode.OnPlayerJoinGame(event.player.getDisplayName());
    }
}
