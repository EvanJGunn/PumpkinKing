package art.tidsear.pumpkinpoints;

import art.tidsear.pumpkinking.PumpkinKingMod;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import org.lwjgl.Sys;

import java.awt.*;
import java.util.Objects;

public class PointsOverlay {
    Color translucentBlack = new Color(0,0,0,0.7f);
    int trbl = translucentBlack.getRGB();
    @SubscribeEvent
    public void onRenderGameOverlay(RenderGameOverlayEvent.Text event) {
        int scaledHeight = event.resolution.getScaledHeight();
        int scaledWidth = event.resolution.getScaledWidth();
        String role = PumpkinKingMod.myData.playerRole;

        // Would render this all the time, but the UI is already cluttered by Decimation
        if(Minecraft.getMinecraft().gameSettings.keyBindPlayerList.getIsKeyPressed()) {
            // Draw backdrop (Specifically designed to go over decimation character overlay, unfortunately translucency might not work out bc of that)
            Gui.drawRect(10,0+10,130,120,trbl);

            // Draw Player points in black box
            Minecraft.getMinecraft().ingameGUI.drawString(Minecraft.getMinecraft().fontRenderer,"Points: " + String.valueOf(PumpkinKingMod.myData.playerPoints), 15, 0+15, Color.ORANGE.getRGB());

            // Draw remaining objectives
            Minecraft.getMinecraft().ingameGUI.drawString(Minecraft.getMinecraft().fontRenderer,"ObjectiveTotal: " + String.valueOf(PumpkinKingMod.myData.objectivesTotalCount), 15, 0+25, Color.WHITE.getRGB());
            Minecraft.getMinecraft().ingameGUI.drawString(Minecraft.getMinecraft().fontRenderer,"YourCount: " + String.valueOf(PumpkinKingMod.myData.objectivesCount), 15, 0+35, Color.WHITE.getRGB());

            //Draw Current Objective
            String objective = "Objective: None";

            if (Objects.equals(role, "King")) {
                objective = "Objective: Protect the pumpkin core, kill humans, hinder human objectives.";
            } else if (Objects.equals(role, "Crew") || Objects.equals(role, "Lobby")) {
                // TODO Added lobby here for testing/debug purposes
                String obj = PumpkinKingMod.myData.objective;
                if (!obj.equals("") && !obj.equals(null)) {
                    objective = "Objective: " + obj;
                }
            }


            // Draw objectives
            Minecraft.getMinecraft().fontRenderer.drawSplitString(objective, 15, 0+45, 105, Color.WHITE.getRGB());
        }


        // Draw Player Role All the time
        int roleColor = Color.WHITE.getRGB();
        String roleText = "No Role";

        // Probably want constants or enum for this role idk
        if(Objects.equals(role, "King")) {
            roleColor = Color.RED.getRGB();
            roleText = "Pumpkin King";
        } else if (Objects.equals(role, "Crew")) {
            roleColor = Color.GREEN.getRGB();
            roleText = "Crew";
        }
        Minecraft.getMinecraft().ingameGUI.drawCenteredString(Minecraft.getMinecraft().fontRenderer, roleText, scaledWidth/2, scaledHeight-50, roleColor);

        // Draw effect Letters
        if(Objects.equals(role, "King")) {
            Minecraft.getMinecraft().ingameGUI.drawString(Minecraft.getMinecraft().fontRenderer, "JMP", (scaledWidth/2) - 90, scaledHeight-30, Color.WHITE.getRGB());

            Minecraft.getMinecraft().ingameGUI.drawString(Minecraft.getMinecraft().fontRenderer, "RGN", (scaledWidth/2) - 70, scaledHeight-30, Color.WHITE.getRGB());

            Minecraft.getMinecraft().ingameGUI.drawString(Minecraft.getMinecraft().fontRenderer, "RES", (scaledWidth/2) - 50, scaledHeight-30, Color.WHITE.getRGB());
        }

        // Draw time remaining
        Minecraft.getMinecraft().ingameGUI.drawString(Minecraft.getMinecraft().fontRenderer, "Apotheosis >"+convertSecondToTime(PumpkinKingMod.myData.timeRemaining), 10,scaledHeight-20,Color.ORANGE.getRGB());
    }

    private String convertSecondToTime(int seconds) {
        int remainderSeconds = seconds % 60;
        int minutes = seconds / 60; // java rounds down

        String remainderStr = String.valueOf(remainderSeconds);
        if (remainderSeconds < 10) {
            remainderStr = "0"+remainderStr;
        }

        return String.format("%s:%s",String.valueOf(minutes),remainderStr);
    }
}