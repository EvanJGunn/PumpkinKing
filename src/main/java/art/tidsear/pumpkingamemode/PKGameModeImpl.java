package art.tidsear.pumpkingamemode;

import art.tidsear.pumpkininterface.InternalCommands;
import art.tidsear.pumpkininterface.InternalCommandsImpl;
import art.tidsear.pumpkinpoints.PointsSystem;
import org.lwjgl.Sys;
import art.tidsear.utility.Vector3f;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PKGameModeImpl implements PKGameMode{
    // Probably should have some sort of config or something
    private int pkPlayerDeathAward = 25;

    private PKState pkState;
    private InternalCommands icms;
    public PointsSystem ptsSys;
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

    // TODO Make all timings configurable
    // Countdown, amount is in seconds and is multiplied later to get milliseconds
    private final float countdownAmount = 10;
    private long targetCountDownTime;

    // Pumpkin Locked / Unlocked phase
    private final float pumpkinPlayTime = 1200; // seconds, 20 minutes
    private long targetPumpkinPlayTime;

    // Time based points awarding vars
    // TODO

    // TODO need to add a timer for locked and unlocked pumpking, in case the players never reach the boss battle

    // Dependency injection makes testing easier, not that I am going to test this, but if I did...
    public PKGameModeImpl(InternalCommands icms) {
        this.icms = icms;
        this.pkState = PKState.IDLE;

        randGen = new Random();
        ptsSys = new PointsSystem();

        // All players array is set by a command
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
            return;
        }

        // Move to countdown state
        icms.sendMessageAll("The game will begin in "+countdownAmount+" seconds");
        targetCountDownTime = System.currentTimeMillis()+(long)(countdownAmount*1000);
        pkState = PKState.COUNTDOWN;
    }

    @Override
    public void Reset() {
        pkState = PKState.IDLE;
        crew.clear();
        pks.clear();

        spawnInLobby();
        // disable pvp etc
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
                doLockedPumpkin();
                break;
            case UNLOCKED_PUMPKIN:
                break;
        }
    }

    @Override
    public PKState getState() {
        return this.pkState;
    }

    private void doCountdown() {
        long currentTime = System.currentTimeMillis();
        if (currentTime < targetCountDownTime){
            return;
        }
        icms.sendMessageAll("Let the hunt begin");

        spawnAllPlayers();

        // move to pumpkin state
        pkState = PKState.LOCKED_PUMPKIN;
        targetPumpkinPlayTime = System.currentTimeMillis()+(long)(pumpkinPlayTime*1000);
    }

    private void doLockedPumpkin() {
        long currentTime = System.currentTimeMillis();
        if (currentTime < targetPumpkinPlayTime){
            return;
        }

        // If we reach here, the players never unlocked the pumpkin
        doPKWinsGame();
    }

    private void doPKWinsGame() {
        icms.sendMessageAll("The Pumpkin King Has Won");
        Reset();
    }

    private void spawnInLobby() {
        for (int i = 0; i < allPlayers.size(); i++) {
            icms.teleportPlayer(allPlayers.get(i), (Vector3f)getRandListItem(lobbySpawns));
            // This catches the case where the player dies, and doesn't respawn until after the game ends
            icms.setPlayerSpawnLocation(allPlayers.get(i), (Vector3f)getRandListItem(lobbySpawns));
        }
    }

    private void doTimeBasedPointsAwards() {
        // TODO
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

    @Override
    public void ResetLobbySpawn() {
        lobbySpawns.clear();
    }

    @Override
    public void ResetPlayerSpawn() {
        playerSpawns.clear();
    }

    @Override
    public String GetPlayerRole(String playerName) {
        if (pkState == PKState.IDLE) {
           return "Lobby";
        }
        if(pks.contains(playerName)) {
            return "King";
        }
        return "Crew";
    }

    @Override
    public void ResetPKSpawn() {
        pkSpawns.clear();
    }

    @Override
    public void DoPlayerPKRespawn(String playerName) {
        if (pks.contains(playerName)) {
            icms.teleportPlayer(playerName, (Vector3f)getRandListItem(pkSpawns));
        } else if (crew.contains(playerName)) {
            icms.teleportPlayer(playerName, (Vector3f)getRandListItem(playerSpawns));
        }
    }

    @Override
    public void DoRandomTeleportToPlayerSpawns(String playerName) {
        icms.teleportPlayer(playerName, (Vector3f)getRandListItem(playerSpawns));
    }

    @Override
    public void OnPlayerDeath(String playerName) {
        boolean isPK = false;
        if (pks.contains(playerName)) {
            isPK = true;
        }
        switch (pkState) {
            case IDLE:
            case COUNTDOWN:
                // People shouldn't be dying here lol, but if they do...
                icms.setPlayerSpawnLocation(playerName,(Vector3f)getRandListItem(lobbySpawns));
                break;
            case LOCKED_PUMPKIN:
            case UNLOCKED_PUMPKIN:
                if (isPK) {
                    icms.setPlayerSpawnLocation(playerName,(Vector3f)getRandListItem(pkSpawns));
                } else {
                    icms.setPlayerSpawnLocation(playerName,(Vector3f)getRandListItem(playerSpawns));
                }
                break;
        }

        // TODO Award points based on whether the pk died or the players died
        if (!isPK) {
            for (int i = 0; i < pks.size(); i++) {
                ptsSys.AddPoints(pks.get(i),pkPlayerDeathAward);
            }
        }
        icms.sendMessageAll("Someone has died.");
    }

    @Override
    public void OnPlayerRespawn(String playerName) {
        // Give player items based on their role, etc

    }

    @Override
    public PointsSystem GetPtsSystem() {
        return this.ptsSys;
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
