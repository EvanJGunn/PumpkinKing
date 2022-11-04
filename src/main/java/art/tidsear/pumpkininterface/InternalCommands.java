package art.tidsear.pumpkininterface;

import art.tidsear.utility.Vector3f;

import java.util.List;
import java.util.UUID;

// Commands that can only be used internally
public interface InternalCommands {
    List<String> getServerPlayers();
    void sendMessageAll(String message);
    void teleportPlayer(String playerName, Vector3f newPos);
    void givePlayerItem(String playerName, String itemName);
    void setPlayerSpawnLocation(String playerName, Vector3f spawnLoc);

    UUID spawnEntity(String entityName, Vector3f pos);
    void slayEntity(UUID uuid);
    boolean isEntityLiving(UUID uuid);
}