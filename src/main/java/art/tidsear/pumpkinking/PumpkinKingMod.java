package art.tidsear.pumpkinking;

import art.tidsear.pumpkingamemode.PKGMTickEvent;
import art.tidsear.pumpkingamemode.PKGameMode;
import art.tidsear.pumpkingamemode.PKGameModeImpl;
import art.tidsear.pumpkininterface.*;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.registry.ExistingSubstitutionException;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.tileentity.TileEntitySignRenderer;
import net.minecraft.command.ICommandManager;
import net.minecraft.command.ServerCommandManager;
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
        PumpkinKingModBlocks.RegisterBlocks();
        PumpkinKingModItems.RegisterItems();

        if (FMLCommonHandler.instance().getSide().equals(Side.SERVER)) {
            serverStart();
        }
        else if (FMLCommonHandler.instance().getSide().equals(Side.CLIENT)) {
            clientStart();
        }
    }

    @EventHandler
    public void onServerStart(FMLServerStartingEvent event) {
        // Register PK related server commands
        MinecraftServer server = MinecraftServer.getServer();
        ICommandManager icm = server.getCommandManager();
        ServerCommandManager scm = (ServerCommandManager) icm;
        scm.registerCommand(new CommandStartPKG());
        scm.registerCommand(new CommandLobbySpawn());
        scm.registerCommand(new CommandPKSpawn());
        scm.registerCommand(new CommandPlayerSpawn());

        FMLCommonHandler.instance().bus().register(new PKGMTickEvent());
    }

    @SideOnly(Side.SERVER)
    private void serverStart() {
        pkGameMode = new PKGameModeImpl(new InternalCommandsImpl());
    }
    @SideOnly(Side.CLIENT)
    private void clientStart() {
        // ClientRegistry.bindTileEntitySpecialRenderer(StoreSignTileEntity.class, new TileEntitySignRenderer());
    }
}
