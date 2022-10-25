package art.tidsear.pumpkingamemode;

import art.tidsear.pumpkin2pumpkin.LocalData;
import art.tidsear.pumpkin2pumpkin.PKServerDataMessage;
import art.tidsear.pumpkinking.PumpkinKingMod;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import scala.collection.parallel.ParIterableLike;

import java.awt.*;
import java.beans.EventHandler;
import java.util.List;
import java.util.Objects;

public class PKGMTickEvent {
    @SideOnly(Side.SERVER)
    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent event) {
        // TODO Every tick right now, not sure how that will turn out
        PumpkinKingMod.pkGameMode.Update();

        // Update clients pk data
        List<EntityPlayerMP> playerEntities = MinecraftServer.getServer().getConfigurationManager().playerEntityList;
        for (EntityPlayerMP p : playerEntities) {
            int playerPts = PumpkinKingMod.pkGameMode.GetPtsSystem().GetPoints(p.getDisplayName());
            String playerRole = PumpkinKingMod.pkGameMode.GetPlayerRole(p.getDisplayName());
            LocalData playerData = new LocalData(playerPts, playerRole);
            PumpkinKingMod.snw.sendTo(new PKServerDataMessage(playerData), p);
        }
    }

//    @SideOnly(Side.CLIENT)
//    @SubscribeEvent
//    public void onRenderTick(TickEvent.RenderTickEvent event) {

}
