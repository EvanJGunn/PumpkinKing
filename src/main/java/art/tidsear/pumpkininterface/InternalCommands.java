package art.tidsear.pumpkininterface;

import org.lwjgl.util.vector.Vector3f;

import java.util.List;

// Commands that can only be used internally
public interface InternalCommands {
    public List<String> getServerPlayers();
    public void teleportPlayer(String playerName, Vector3f newPos);
}
