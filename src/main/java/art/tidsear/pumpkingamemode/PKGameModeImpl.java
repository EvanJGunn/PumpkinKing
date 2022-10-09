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

    // Countdown, amount is in seconds and is multiplied later to get milliseconds
    private final float countdownAmount = 10;
    private long targetCountDownTime;

    // TODO need to add a timer for locked and unlocked pumpking, in case the players never reach the boss battle

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

        // TODO do all resets here, lists, etc
        crew.clear();
        pks.clear();

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
//        if (playerSpawns.size() == 0) {
//            icms.sendMessageAll("No Player Spawns Set, Start Up Failed");
//            return;
//        }
//        if (pkSpawns.size() == 0) {
//            icms.sendMessageAll("No Pumpkin King Spawns Set, Start Up Failed");
//            return;
//        }
//        if (lobbySpawns.size() == 0) {
//            icms.sendMessageAll("No Lobby Spawns Set, Start Up Failed");
//        }

        // Move to countdown state
        icms.sendMessageAll("The game will begin in "+countdownAmount+" seconds");
        targetCountDownTime = System.currentTimeMillis()+(long)(countdownAmount*1000);
        pkState = PKState.COUNTDOWN;
    }

    @Override
    public void Update() {
        switch (pkState) {
            case IDLE:
                // Do nothing
                break;
            case COUNTDOWN:
                doCountdown();
                break;
            case LOCKED_PUMPKIN:
                break;
            case UNLOCKED_PUMPKIN:
                break;
            case BOSS_BATTLE:
                break;
        }
    }

    private void doCountdown() {
        long currentTime = System.currentTimeMillis();
        if (currentTime < targetCountDownTime){
            return;
        }
        icms.sendMessageAll("Let the hunt begin");

        spawnAllPlayers();
        pkState = PKState.LOCKED_PUMPKIN;
    }

    // TODO this should be callable via a command block
    public void SpawnInLobby() {
        for (int i = 0; i < allPlayers.size(); i++) {
            icms.teleportPlayer(allPlayers.get(i), (Vector3f)getRandListItem(lobbySpawns));
        }
    }

    private void spawnAllPlayers() {
        for (int i = 0; i < allPlayers.size(); i++) {
            icms.teleportPlayer(allPlayers.get(i), (Vector3f)getRandListItem(playerSpawns));
        }
    }

    private Object getRandListItem(List list) {
        int randIndex = randGen.nextInt(list.size());
        return list.get(randIndex);
    }

    @Override
    public void AddLobbySpawn(Vector3f pos) {
        for (int i = 0; i < lobbySpawns.size(); i++) {
            if (v3feql(lobbySpawns.get(i),pos)) return;
        }
        lobbySpawns.add(pos);
    }

    @Override
    public void AddPlayerSpawn(Vector3f pos) {
        for (int i = 0; i < playerSpawns.size(); i++) {
            if (v3feql(playerSpawns.get(i),pos)) return;
        }
        playerSpawns.add(pos);
    }

    @Override
    public void AddPKSpawn(Vector3f pos) {
        for (int i = 0; i < pkSpawns.size(); i++) {
            if (v3feql(pkSpawns.get(i),pos)) return;
        }
        pkSpawns.add(pos);
    }

    @Override
    public void RemoveLobbySpawn(Vector3f pos) {
        for (int i = 0; i < lobbySpawns.size(); i++) {
            if (v3feql(lobbySpawns.get(i),pos)) {
                lobbySpawns.remove(i);
                return;
            }
        }
    }

    @Override
    public void RemovePlayerSpawn(Vector3f pos) {
        for (int i = 0; i < playerSpawns.size(); i++) {
            if (v3feql(playerSpawns.get(i),pos)) {
                playerSpawns.remove(i);
                return;
            }
        }
    }

    @Override
    public void RemovePKSpawn(Vector3f pos) {
        for (int i = 0; i < pkSpawns.size(); i++) {
            if (v3feql(pkSpawns.get(i),pos)) {
                pkSpawns.remove(i);
                return;
            }
        }
    }

    // Never trust .equals, also TODO put in a util
    private boolean v3feql(Vector3f v1, Vector3f v2) {
        if(v1.getX() == v2.getX()) {
            if(v1.getZ() == v2.getZ()) {
                return v1.getY() == v2.getY();
            }
        }
        return false;
    }
}
