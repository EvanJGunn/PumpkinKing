package art.tidsear.pumpkinking;

import art.tidsear.pumpkin2pumpkin.LocalData;
import art.tidsear.pumpkin2pumpkin.PKServerDataMessage;
import art.tidsear.pumpkin2pumpkin.PKServerDataMessageHandler;
import art.tidsear.pumpkingamemode.*;
import art.tidsear.pumpkininterface.*;
import art.tidsear.pumpkinpoints.PointsOverlay;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.command.ICommandManager;
import net.minecraft.command.ServerCommandManager;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.Sys;

@Mod(modid = PumpkinKingMod.MODID, version = PumpkinKingMod.VERSION)
public class PumpkinKingMod
{
    public static final String MODID = "pumpkinking";
    public static final String VERSION = "0.01";

    // Yeah, singletons / Global variables aren't the best, but, whatever
    public static PKGameMode pkGameMode;

    public static SimpleNetworkWrapper snw;
    private static int nwDiscriminator = 5505732;

    // Each client has state they track that is updated by the server, also probably could be implemented better somehow idk
    public static LocalData myData;
    
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        System.out.println("Pumpkins");
        PumpkinKingModBlocks.RegisterBlocks();
        PumpkinKingModItems.RegisterItems();

        // I have reason to believe both functions will be called regardless?
        // IDK not interested in understanding this nonsense as long as it works
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
        scm.registerCommand(new CommandPlayerRespawn());
        scm.registerCommand(new CommandResetPKG());
        scm.registerCommand(new CommandPlayerPoints());
        scm.registerCommand(new CommandPKGConfig());

        FMLCommonHandler.instance().bus().register(new PKGMTickEvent());
        FMLCommonHandler.instance().bus().register(new PKGRespawnEvent());

        //FMLCommonHandler.instance().bus().register(new PKGDeathEvent());
        MinecraftForge.EVENT_BUS.register(new PKGDeathEvent());

        snw = NetworkRegistry.INSTANCE.newSimpleChannel(MODID);
        snw.registerMessage(PKServerDataMessageHandler.class, PKServerDataMessage.class, nwDiscriminator, Side.SERVER);
    }

    @SideOnly(Side.SERVER)
    private void serverStart() {
        pkGameMode = new PKGameModeImpl(new InternalCommandsImpl());
    }
    @SideOnly(Side.CLIENT)
    private void clientStart() {
        snw = NetworkRegistry.INSTANCE.newSimpleChannel(MODID);
        snw.registerMessage(PKServerDataMessageHandler.class, PKServerDataMessage.class, nwDiscriminator, Side.CLIENT);

        myData = new LocalData(0,"");
        MinecraftForge.EVENT_BUS.register(new PointsOverlay());
    }
}
