package art.tidsear.pumpkingamemode;

// There should be no interaction between the PKGameMode and
// minecraft directly. Any calls to modify the minecraft state
// should be made using passed in objects or other interfaces (commands?).
// Doing everything with commands might be useful for debugging
// Let commands be used to configure the PKGameMode before it starts,
// ie where players can spawn randomly, where the pumpkin king can spawn, etc
// Meant for 4 crew players, 1 pumpkin king
public interface PKGameMode {
    public void Init();
    // Reset the gamemode
    public void Reset();

    public void Update();
    //Init
    //DoCountDown
    //Reset
    //Update
}

