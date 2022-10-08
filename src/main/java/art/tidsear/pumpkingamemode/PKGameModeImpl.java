package art.tidsear.pumpkingamemode;

import art.tidsear.pumpkininterface.InternalCommands;
import art.tidsear.pumpkininterface.InternalCommandsImpl;
import org.lwjgl.Sys;
import org.lwjgl.util.vector.Vector3f;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PKGameModeImpl implements PKGameMode{
    private PKState pkState;
    private InternalCommands icms;
    private Random randGen;

    // Spawn points
    private List<Vector3f> playerSpawns;
    private List<Vector3f> pkSpawns;
    private List<Vector3f> lobbySpawns;
    private List<Vector3f> bossRoomSpawns;
    private List<Vector3f> bossRoomPkSpawns;


    // Player lists
    private List<String> allPlayers;
    private List<String> pks;
    private List<String> crew;

    // Dependency injection makes testing easier, not that I am going to test this, but if I did...
    public PKGameModeImpl(InternalCommands icms) {
        this.icms = icms;
        this.pkState = PKState.IDLE;

        randGen = new Random();

        // All players is set by a command
        // allPlayers = new ArrayList<>();
        pks = new ArrayList<String>();
        crew = new ArrayList<String>();

        playerSpawns = new ArrayList<Vector3f>();
        pkSpawns = new ArrayList<Vector3f>();
        lobbySpawns = new ArrayList<Vector3f>();
    }

    // Give the players their roles, adventure mode, starting items.
    // Have PVP disabled.
    @Override
    public void StartUp() {
        icms.sendMessageAll("Starting PK Game Mode");
        allPlayers = icms.getServerPlayers();
        icms.sendMessageAll(String.format("Players found: %s", allPlayers.toString()));

        // TODO no proper lower bound for testing purposes, implement testing mode later?
        if (allPlayers.size() > 5 || allPlayers.size() < 0) {
            icms.sendMessageAll("Number of players greater than 5, Start Up Failed.");
            return;
        }

        // TODO Make number of pk configurable, player count configurable, etc
        int pkIndex = randGen.nextInt(allPlayers.size());
        pks.add(allPlayers.get(pkIndex));
        icms.sendMessageAll(String.format("REMOVE ME, pk is %s", allPlayers.get(pkIndex)));

        for (int i = 0; i < allPlayers.size(); i++) {
            if (i != pkIndex) {
                crew.add(allPlayers.get(i));
            }
        }

        // TODO Should exit early, moved down here for dev purposes
        // IMPORTANT: Spawns should be set via commands/ command blocks in game
        if (playerSpawns.size() == 0) {
            icms.sendMessageAll("No Player Spawns Set, Start Up Failed");
            return;
        }
        if (pkSpawns.size() == 0) {
            icms.sendMessageAll("No Pumpkin King Spawns Set, Start Up Failed");
            return;
        }
        if (lobbySpawns.size() == 0) {
            icms.sendMessageAll("No Lobby Spawns Set, Start Up Failed");
        }

        pkState = PKState.COUNTDOWN;
    }

    @Override
    public void Update() {
        switch (pkState) {
            case IDLE:
                // Do nothing
                break;
            case COUNTDOWN:
                break;
            case LOCKED_PUMPKIN:
                break;
            case UNLOCKED_PUMPKIN:
                break;
            case BOSS_BATTLE:
                break;
        }
    }
}
