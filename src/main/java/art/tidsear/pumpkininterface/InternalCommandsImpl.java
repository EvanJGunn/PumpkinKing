package art.tidsear.pumpkininterface;

import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandGive;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.server.CommandSummon;
import net.minecraft.command.server.CommandTeleport;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import art.tidsear.utility.Vector3f;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.World;
import scala.collection.parallel.ParIterableLike;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class InternalCommandsImpl implements InternalCommands{
    private CommandGive cmdGive;
    private CommandTeleport cmdTP;
    private CommandSummon cmdSMN;
    private ICommandSender sender;
    public InternalCommandsImpl() {
        cmdGive = new CommandGive();
        cmdTP = new CommandTeleport();
        sender = new ICMSSender();
        cmdSMN = new CommandSummon();
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
            player.setSpawnChunk(new ChunkCoordinates((int)spawnLoc.getX(),(int)spawnLoc.getY(),(int)spawnLoc.getZ()), false);
        }
    }

    @Override
    public void spawnEntity(String entityName, Vector3f pos) {
        cmdSMN.processCommand(sender, new String[]{entityName, String.valueOf(pos.getX()), String.valueOf(pos.getY()), String.valueOf(pos.getZ())});
    }

    @Override
    public void slayEntity(UUID uuid) {
        List<Entity> entities = Minecraft.getMinecraft().theWorld.loadedEntityList;
        for (Entity e:
             entities) {
            if (e.getUniqueID() == uuid){
                e.setDead();
            }
        }
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
        return null;
    }

    @Override
    public World getEntityWorld() {
        return null;
    }
}