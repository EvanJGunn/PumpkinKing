package art.tidsear.pumpkininterface;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import org.lwjgl.util.vector.Vector3f;
import scala.collection.parallel.ParIterableLike;

import java.util.ArrayList;
import java.util.List;

public class InternalCommandsImpl implements InternalCommands{
    public InternalCommandsImpl() {

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
        playerEntity.setPosition(newPos.getX(), newPos.getY(), newPos.getZ());
    }

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
