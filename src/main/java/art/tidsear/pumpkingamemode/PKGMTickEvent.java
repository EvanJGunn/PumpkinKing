package art.tidsear.pumpkingamemode;

import art.tidsear.pumpkinking.PumpkinKingMod;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;

import java.beans.EventHandler;

public class PKGMTickEvent {
    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent event) {
        // TODO Every tick right now, not sure how that will turn out
        PumpkinKingMod.pkGameMode.Update();
    }
}
