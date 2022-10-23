package art.tidsear.pumpkinpoints;

import art.tidsear.pumpkinking.PumpkinKingMod;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

import java.awt.*;
import java.util.Objects;

public class PointsOverlay {
    Color translucentBlack = new Color(0,0,0,0.7f);
    int trbl = translucentBlack.getRGB();
    @SubscribeEvent
    public void onRenderGameOverlay(RenderGameOverlayEvent.Text event) {
        int scaledHeight = event.resolution.getScaledHeight();
        int scaledWidth = event.resolution.getScaledWidth();

        // Draw backdrop (Specifically designed to go over decimation character overlay, unfortunately translucency might not work out bc of that)
        Gui.drawRect(scaledWidth-130,0+10,scaledWidth-10,70,trbl);

        // Draw Player points in black box
        Minecraft.getMinecraft().ingameGUI.drawString(Minecraft.getMinecraft().fontRenderer,"Points: " + String.valueOf(PumpkinKingMod.myData.playerPoints), scaledWidth-120, 0+15, Color.ORANGE.getRGB());

        // Draw Player Role
        int roleColor = Color.WHITE.getRGB();
        String role = PumpkinKingMod.myData.playerRole;
        String roleText = "No Role";

        // Probably want constants or enum for this role idk
        if(Objects.equals(role, "King")) {
            roleColor = Color.RED.getRGB();
            roleText = "You Are The Pumpkin King";
        } else if (Objects.equals(role, "Crew")) {
            roleColor = Color.GREEN.getRGB();
            roleText = "You Are Crew";
        }
        Minecraft.getMinecraft().ingameGUI.drawCenteredString(Minecraft.getMinecraft().fontRenderer, roleText, scaledWidth/2, scaledHeight-50, roleColor);

        // TODO Draw Current Objective
        String objective = "Objective: None";

        if (Objects.equals(role, "King")) {
            objective = "Objective: Protect the pumpkin core, kill humans, hinder human objectives.";
        } else if (Objects.equals(role, "Crew")) {

        }

        // Will probably want split string for Objectives
        Minecraft.getMinecraft().fontRenderer.drawSplitString(objective, scaledWidth-120, 0+25, 110, Color.WHITE.getRGB());

        // TODO Draw Remaining Number Of Objectives, Probably want to put this above current objective
    }
}
