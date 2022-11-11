package art.tidsear.pumpkingamemode;

import art.tidsear.pumpkin2pumpkin.LocalData;
import art.tidsear.pumpkin2pumpkin.PKServerDataMessage;
import art.tidsear.pumpkinking.PumpkinKingMod;
import art.tidsear.pumpkinobjectives.Objective;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionHealth;
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
            // Do clients updating
            int playerPts = PumpkinKingMod.pkGameMode.GetPtsSystem().GetPoints(p.getDisplayName());
            String playerRole = PumpkinKingMod.pkGameMode.GetPlayerRole(p.getDisplayName());
            Objective playerObjective = PumpkinKingMod.pkGameMode.GetObjectiveManager().getObjective(p.getDisplayName());

            String objDesc = "";
            if (playerObjective != null) {
                objDesc = playerObjective.getDescription();
            } else {
                // Check if all objectives are completed
                if (PumpkinKingMod.pkGameMode.GetState() == PKState.UNLOCKED_PUMPKIN) {
                    objDesc = "YOU ARE NOT SAFE. DESTROY THE CORE. YOU ARE NOT SAFE. DESTROY THE CORE. YOU ARE NOT SAFE. DESTROY THE CORE.";
                }
            }

            int remainingTime = PumpkinKingMod.pkGameMode.GetTimeRemaining();
            int remainingTotal = PumpkinKingMod.pkGameMode.GetTotalRemainingObjectives();
            int playerTotal = PumpkinKingMod.pkGameMode.GetPlayerRemainingObjectives(p.getDisplayName());
            LocalData playerData = new LocalData(playerPts, playerTotal, remainingTotal, playerRole, objDesc, remainingTime);
            PumpkinKingMod.snw.sendTo(new PKServerDataMessage(playerData), p);

            // Solve world hunger
            p.addPotionEffect(new PotionEffect(23, 1, 16262178, true));

            if (PumpkinKingMod.pkGameMode.GetState() == PKState.UNLOCKED_PUMPKIN) {
                if (!PumpkinKingMod.pkGameMode.IsPK(p.getDisplayName())) {
                    // Invisibility for crew at end of game
                    p.addPotionEffect(new PotionEffect(14, 100, 1, false));
                }
            } else if (PumpkinKingMod.pkGameMode.GetState() == PKState.IDLE || PumpkinKingMod.pkGameMode.GetState() == PKState.COUNTDOWN) {
                // Prevent players from killing each other in spawn
                p.addPotionEffect(new PotionEffect(10, 1, 15, true));
            }

            if (PumpkinKingMod.pkGameMode.GetState() != PKState.COUNTDOWN && PumpkinKingMod.pkGameMode.IsPK(p.getDisplayName())) {
                // Jump & Speed
                if (p.inventory.currentItem == 0) {
                    p.addPotionEffect(new PotionEffect(8, 10, 5, true));
                    p.addPotionEffect(new PotionEffect(1, 10, 3, true));
                }
                // Regen
                if (p.inventory.currentItem == 1) {
                    p.addPotionEffect(new PotionEffect(10, 10, 4, true));
                }
                // Fire Resistance
                if (p.inventory.currentItem == 2) {
                    p.addPotionEffect(new PotionEffect(12, 10, 2, true));
                }
//                // Night Vision
//                if (p.inventory.currentItem == 3) {
//                    p.addPotionEffect(new PotionEffect(16, 100, 1, true));
//                }
            }
        }
    }

//    @SideOnly(Side.CLIENT)
//    @SubscribeEvent
//    public void onRenderTick(TickEvent.RenderTickEvent event) {

}
