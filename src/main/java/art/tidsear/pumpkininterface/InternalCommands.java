package art.tidsear.pumpkininterface;

import art.tidsear.utility.Vector3f;

import java.util.List;

// Commands that can only be used internally
public interface InternalCommands {
    public List<String> getServerPlayers();

    public void sendMessageAll(String message);
    public void teleportPlayer(String playerName, Vector3f newPos);

    public void givePlayerItem(String playerName, String itemName);

    public void setPlayerSpawnLocation(String playerName, Vector3f spawnLoc);

}