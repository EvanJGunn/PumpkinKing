package art.tidsear.pumpkingamemode;

import art.tidsear.pumpkinpoints.PointsSystem;
import art.tidsear.utility.Vector3f;

// There should be no interaction between the PKGameMode and
// minecraft directly. Any calls to modify the minecraft state
// should be made using passed in objects or other interfaces (commands?).
// Doing everything with commands might be useful for debugging
// Let commands be used to configure the PKGameMode before it starts,
// ie where players can spawn randomly, where the pumpkin king can spawn, etc
// Meant for 4 crew players, 1 pumpkin king
public interface PKGameMode {
    // Do setup, reset everything, etc
    public void StartUp();

    public void Reset();

    public void Update();

    public PKState getState();

    public PKConfig getConfig();

    // TODO We should never add duplicates for spawns
    public void AddLobbySpawn(Vector3f pos);
    public void AddPlayerSpawn(Vector3f pos);
    public void AddPKSpawn(Vector3f pos);
    public void RemoveLobbySpawn(Vector3f pos);
    public void RemovePlayerSpawn(Vector3f pos);
    public void RemovePKSpawn(Vector3f pos);
    public void ResetPKSpawn();
    public void ResetLobbySpawn();
    public void ResetPlayerSpawn();

    public String GetPlayerRole(String playerName);

    // Respawn a player or pk depending on their role
    public void DoPlayerPKRespawn(String playerName);

    public void DoRandomTeleportToPlayerSpawns(String playerName);

    public void OnPlayerDeath(String playerName);
    public void OnPlayerRespawn(String playerName);

    // Sure this isn't too great, but hacky code in my free time an on time game makes
    public PointsSystem GetPtsSystem();

    // TODO use set redstone block from icms, and have commands to change what positions the blocks are set to for specific events
}

