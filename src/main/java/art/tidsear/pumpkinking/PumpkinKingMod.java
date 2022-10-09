package art.tidsear.pumpkinking;

import art.tidsear.pumpkingamemode.PKGameMode;
import art.tidsear.pumpkingamemode.PKGameModeImpl;
import art.tidsear.pumpkininterface.CommandStartPKG;
import art.tidsear.pumpkininterface.InternalCommandsImpl;
import cpw.mods.fml.common.event.FMLServerStartedEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import net.minecraft.command.ICommandManager;
import net.minecraft.command.ServerCommand;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.init.Blocks;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import net.minecraft.server.MinecraftServer;

@Mod(modid = PumpkinKingMod.MODID, version = PumpkinKingMod.VERSION)
public class PumpkinKingMod
{
    public static final String MODID = "pumpkinking";
    public static final String VERSION = "0.01";

    // Yeah, singletons / Global variables aren't the best, but, whatever
    public static PKGameMode pkGameMode;
    
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        System.out.println("Pumpkins");
        pkGameMode = new PKGameModeImpl(new InternalCommandsImpl());
    }

    @EventHandler
    public void onServerStart(FMLServerStartingEvent event) {
        // Register PK related server commands
        MinecraftServer server = MinecraftServer.getServer();
        ICommandManager icm = server.getCommandManager();
        ServerCommandManager scm = (ServerCommandManager) icm;
        scm.registerCommand(new CommandStartPKG());
    }
}
