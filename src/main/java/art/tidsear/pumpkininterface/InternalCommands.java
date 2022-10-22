package art.tidsear.pumpkininterface;

import art.tidsear.utility.Vector3f;

import java.util.List;

// Commands that can only be used internally
public interface InternalCommands {
    List<String> getServerPlayers();

    void sendMessageAll(String message);
    void teleportPlayer(String playerName, Vector3f newPos);

    void givePlayerItem(String playerName, String itemName);

    void setPlayerSpawnLocation(String playerName, Vector3f spawnLoc);

}