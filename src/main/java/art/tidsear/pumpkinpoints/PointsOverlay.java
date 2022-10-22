package art.tidsear.pumpkinpoints;

import art.tidsear.pumpkinking.PumpkinKingMod;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class PointsOverlay {
    @SubscribeEvent
    public void onRenderGameOverlay(RenderGameOverlayEvent event) {
        // Draw points
        // event.resolution.getScaledHeight() etc etc
        Minecraft.getMinecraft().fontRenderer.drawString(String.valueOf(PumpkinKingMod.myData.playerPoints), 50, 50, Color.ORANGE.getRGB());
        // Draw Role

        // TODO Draw Current Objective

        // TODO Draw Remaining Number Of Objectives
    }
}
