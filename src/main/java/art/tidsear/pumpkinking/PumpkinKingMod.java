package art.tidsear.pumpkinking;

import art.tidsear.pumpkin2pumpkin.LocalData;
import art.tidsear.pumpkin2pumpkin.PKServerDataMessage;
import art.tidsear.pumpkin2pumpkin.PKServerDataMessageHandler;
import art.tidsear.pumpkingamemode.*;
import art.tidsear.pumpkininterface.*;
import art.tidsear.pumpkinpoints.PointsOverlay;
import art.tidsear.utility.Vector3f;
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

import java.util.HashMap;
import java.util.Map;

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

    // We have a hacky way of tracking metadata for blocks for this mod
    // Metadata may or may not be removed for a location if the block is removed so yeah its very hacky but IDC, need to get this done
    public static Map<Vector3f, String> metadata;
    
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        PumpkinKingModBlocks.RegisterBlocks();
        PumpkinKingModItems.RegisterItems();
        metadata = new HashMap<>();

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
        scm.registerCommand(new CommandMobArea());
        scm.registerCommand(new CommandObjective());
        scm.registerCommand(new CommandPKDoor());
        scm.registerCommand(new CommandDestroyCore());

        FMLCommonHandler.instance().bus().register(new PKGMTickEvent());
        FMLCommonHandler.instance().bus().register(new PKGPlayerEvents());

        //FMLCommonHandler.instance().bus().register(new PKGDeathEvent());
        MinecraftForge.EVENT_BUS.register(new PKGDeathEvent());
        MinecraftForge.EVENT_BUS.register(new PKGFallEvent());

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

        myData = new LocalData();
        MinecraftForge.EVENT_BUS.register(new PointsOverlay());
    }
}
