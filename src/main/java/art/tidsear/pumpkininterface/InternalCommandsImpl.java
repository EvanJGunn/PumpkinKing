package art.tidsear.pumpkininterface;

import art.tidsear.utility.ForgeGive;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import cpw.mods.fml.server.FMLServerHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockGrass;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandGive;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.server.CommandSummon;
import net.minecraft.command.server.CommandTeleport;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import art.tidsear.utility.Vector3f;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.RegistrySimple;
import net.minecraft.world.World;
import org.lwjgl.Sys;
import scala.collection.parallel.ParIterableLike;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class InternalCommandsImpl implements InternalCommands{
    private ForgeGive cmdGive;
    private CommandTeleport cmdTP;
    private ICommandSender sender;
    public InternalCommandsImpl() {
        cmdGive = new ForgeGive();
        cmdTP = new CommandTeleport();
        sender = new ICMSSender();
    }

    @Override
    public List<String> getServerPlayers() {
        List<String> players = new ArrayList<>();
        List<EntityPlayerMP> playerEntities = MinecraftServer.getServer().getConfigurationManager().playerEntityList;
        for (EntityPlayerMP p : playerEntities) {
            players.add(p.getDisplayName());
        }
        return players;
    }

    @Override
    public void teleportPlayer(String playerName, Vector3f newPos) {
        EntityPlayerMP playerEntity = playerNameToEntity(playerName);
        if (playerEntity == null) {
            System.out.println("Issue teleporting player, entity conversion was null");
            return;
        }
//        playerEntity.setPosition(newPos.getX(), newPos.getY(), newPos.getZ());
        cmdTP.processCommand(this.sender, new String[]{playerEntity.getDisplayName(), ""+newPos.getX(), ""+newPos.getY(), ""+newPos.getZ()});
    }

    @Override
    public void givePlayerItem(String playerName, String itemName) {
        cmdGive.processCommand(this.sender,new String[]{playerName, itemName});
    }

    @Override
    public void setPlayerSpawnLocation(String playerName, Vector3f spawnLoc) {
        EntityPlayerMP player = playerNameToEntity(playerName);
        if (player == null) {
            System.out.println("Issue setting player spawn location, entity conversion was null");
            return;
        }
        if (player != null) {
            player.setSpawnChunk(new ChunkCoordinates((int)spawnLoc.getX(),(int)spawnLoc.getY(),(int)spawnLoc.getZ()), true);
        }
    }

    @Override
    public UUID spawnEntity(String entityName, Vector3f pos) {
        try {
            Entity entity = EntityList.createEntityByName(entityName, FMLServerHandler.instance().getServer().getEntityWorld());
            entity.setLocationAndAngles(pos.getX(),pos.getY(),pos.getZ(),0,0);
            MinecraftServer.getServer().getEntityWorld().spawnEntityInWorld(entity);

            return entity.getUniqueID();
        } catch (Error e) {
            System.out.println(e.getMessage());
            // idk whatever man if you want to do summons wrong it aint my prob
        }
        return null;
    }

    @Override
    public void slayEntity(UUID uuid) {
        List<Entity> entities = MinecraftServer.getServer().getEntityWorld().loadedEntityList;
        for (Entity e:
             entities) {
            if (e.getUniqueID() == uuid){
                e.setDead();
            }
        }
    }

    @Override
    public boolean isEntityLiving(UUID uuid) {
        for (Object e:
             MinecraftServer.getServer().getEntityWorld().loadedEntityList) {
            Entity entity = ((Entity)e);
            if (entity.getUniqueID() == uuid) {
                if (entity.isEntityAlive()) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void setBlock(Vector3f pos, int ID) {
        MinecraftServer.getServer().getEntityWorld().setBlock((int) pos.getX(), (int) pos.getY(), (int) pos.getZ(), Block.getBlockById(ID));
    }

//    @Override
//    public String entityUUIDtoPlayer(UUID uuid) {
//        List<Entity> entities = Minecraft.getMinecraft().theWorld.loadedEntityList;
//        for (Entity e:
//             entities) {
//            if (e.getUniqueID() == uuid){
//                if (e instanceof EntityPlayerMP) {
//                    return ((EntityPlayerMP) e).getDisplayName();
//                }
//            }
//        }
//        return "";
//    }

    @Override
    public void sendMessageAll(String message) {
        List<EntityPlayerMP> playerEntities = MinecraftServer.getServer().getConfigurationManager().playerEntityList;
        for (EntityPlayerMP p : playerEntities) {
            p.addChatMessage(new ChatComponentText(message));
        }
    }

    // Probably belongs somewhere else
    private EntityPlayerMP playerNameToEntity(String playerName) {
        List<EntityPlayerMP> playerEntities = MinecraftServer.getServer().getConfigurationManager().playerEntityList;
        for (EntityPlayerMP p : playerEntities) {
            if (p.getDisplayName() == playerName) {
                return p;
            }
        }
        return null;
    }
}

class ICMSSender implements ICommandSender {

    @Override
    public String getCommandSenderName() {
        return "Game";
    }

    @Override
    public IChatComponent func_145748_c_() {
        return new ChatComponentText(this.getCommandSenderName());
    }

    @Override
    public void addChatMessage(IChatComponent p_145747_1_) {
        System.out.println(p_145747_1_.getUnformattedText());
    }

    @Override
    public boolean canCommandSenderUseCommand(int p_70003_1_, String p_70003_2_) {
        return true;
    }

    @Override
    public ChunkCoordinates getPlayerCoordinates() {
        return new ChunkCoordinates(0,0,0);
    }

    @Override
    public World getEntityWorld() {
        return MinecraftServer.getServer().getEntityWorld();
    }
}