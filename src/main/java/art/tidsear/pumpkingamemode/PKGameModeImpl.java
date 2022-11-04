package art.tidsear.pumpkingamemode;

import art.tidsear.mobarea.MobAreaManager;
import art.tidsear.pumpkininterface.InternalCommands;
import art.tidsear.pumpkinobjectives.ObjectiveManager;
import art.tidsear.pumpkinpoints.PointsSystem;
import art.tidsear.utility.Vector3f;
import net.minecraft.util.EnumChatFormatting;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class PKGameModeImpl implements PKGameMode{
    private PKState pkState;
    private PKConfig pkConfig;
    private InternalCommands icms;
    private PointsSystem ptsSys;
    private ObjectiveManager objManager;
    private MobAreaManager mobAreaManager;
    private Random randGen;

    // Spawn points
    private List<Vector3f> playerSpawns;
    private List<Vector3f> pkSpawns;
    private List<Vector3f> lobbySpawns;


    // Player lists
    private List<String> allPlayers;
    private List<String> pks;
    private List<String> crew;

    // TODO Make all timings configurable
    // Countdown, amount is in seconds and is multiplied later to get milliseconds
    private final float countdownAmount = 10;
    private long targetCountDownTime;

    // Pumpkin Locked / Unlocked phase
    private long targetPumpkinPlayTime;

    // Time based points awarding vars
    private long targetPumpkinAwardsTime;

    private Map<String, Integer> remainingCrewObjectives;

    // Dependency injection makes testing easier, not that I am going to test this, but if I did...
    public PKGameModeImpl(InternalCommands icms) {
        this.icms = icms;
        this.pkState = PKState.IDLE;

        randGen = new Random();
        ptsSys = new PointsSystem();
        pkConfig = new PKConfig();
        mobAreaManager = new MobAreaManager(icms);
        objManager = new ObjectiveManager();

        // All players array is set by a command
        // allPlayers = new ArrayList<>();
        pks = new ArrayList<String>();
        crew = new ArrayList<String>();
        remainingCrewObjectives = new HashMap<>();

        playerSpawns = new ArrayList<Vector3f>();
        pkSpawns = new ArrayList<Vector3f>();
        lobbySpawns = new ArrayList<Vector3f>();
    }

    // Give the players their roles, adventure mode, starting items.
    // Have PVP disabled.
    @Override
    public void StartUp() {
        icms.sendMessageAll("Starting PK Game Mode");

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

        // TODO do all resets here, lists, etc
        crew.clear();
        pks.clear();
        remainingCrewObjectives.clear();
        this.ptsSys.ResetPoints();
        this.objManager.resetAssignments();
        mobAreaManager.resetMobAreas();

        allPlayers = icms.getServerPlayers();

        // TODO no proper lower bound for testing purposes, implement testing mode later?
        if (allPlayers.size() > 5 || allPlayers.size() < 0) {
            icms.sendMessageAll("Number of players greater than 5, Start Up Failed.");
            return;
        }

        // TODO Make number of pk configurable, player count configurable, etc
        // Assign roles
        int pkIndex = randGen.nextInt(allPlayers.size());
        pks.add(allPlayers.get(pkIndex));

        for (int i = 0; i < allPlayers.size(); i++) {
            if (i != pkIndex) {
                crew.add(allPlayers.get(i));
            }
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
        mobAreaManager.resetMobAreas();

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
                doTimeBasedPointsAwards();
                mobAreaManager.doUpdate();
                break;
            case UNLOCKED_PUMPKIN:
                doTimeBasedPointsAwards();
                mobAreaManager.doUpdate();
                break;
        }
    }

    @Override
    public PKState GetState() {
        return this.pkState;
    }

    @Override
    public PKConfig GetConfig() {
        return pkConfig;
    }

    @Override
    public int GetTotalRemainingObjectives() {
        AtomicInteger i = new AtomicInteger(0);
        remainingCrewObjectives.forEach((player, count) -> {
            i.set(i.get()+count);
        });
        return i.get();
    }

    @Override
    public int GetPlayerRemainingObjectives(String player) {
        if (remainingCrewObjectives.containsKey(player)) {
            return remainingCrewObjectives.get(player);
        }
        return 0;
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
        targetPumpkinPlayTime = System.currentTimeMillis()+(long)(pkConfig.roundTimeSeconds*1000);
        targetPumpkinAwardsTime = System.currentTimeMillis()+(long)(pkConfig.timeBasedAwardSeconds*1000);

        // Give players starting items
        for (String player:
             allPlayers) {
            givePlayerRoleBasedItems(player);
        }

        // Assign Objectives
        assignAndSetupInitialObjectives();
    }

    private void doLockedPumpkin() {
        long currentTime = System.currentTimeMillis();

        // Check if players have remaining objectives
        AtomicBoolean remains = new AtomicBoolean(false);
        remainingCrewObjectives.forEach((player, amnt) -> {
            if (amnt > 0) {
                remains.set(true);
            }
        });
        if (!remains.get()) {
            initialUnlockPumpkin();
            return;
        }

        if (currentTime < targetPumpkinPlayTime){
            return;
        }

        // If we reach here, the players never unlocked the pumpkin
        doPKWinsGame();
    }

    private void initialUnlockPumpkin() {
        System.out.println("TODO PUMPKIN UNLOCKING");
        pkState = PKState.UNLOCKED_PUMPKIN;
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
        long currentTime = System.currentTimeMillis();
        if (currentTime < targetPumpkinAwardsTime){
            return;
        }
        for (int i = 0; i < crew.size(); i++) {
            ptsSys.AddPoints(crew.get(i), pkConfig.playerTimeBasedAward);
        }
        for (int i = 0; i < pks.size(); i++) {
            ptsSys.AddPoints(pks.get(i), pkConfig.pkTimeBasedAward);
        }
        icms.sendMessageAll(String.format("Time Based Awards! Players got %s points, PK got %s points",pkConfig.playerTimeBasedAward,pkConfig.pkTimeBasedAward));

        targetPumpkinAwardsTime = System.currentTimeMillis()+(long)(pkConfig.timeBasedAwardSeconds*1000);
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
            if (lobbySpawns.get(i).equals(pos)) return;
        }
        lobbySpawns.add(pos);
    }

    @Override
    public void AddPlayerSpawn(Vector3f pos) {
        for (int i = 0; i < playerSpawns.size(); i++) {
            if (playerSpawns.get(i).equals(pos)) return;
        }
        playerSpawns.add(pos);
    }

    @Override
    public void AddPKSpawn(Vector3f pos) {
        for (int i = 0; i < pkSpawns.size(); i++) {
            if (pkSpawns.get(i).equals(pos)) return;
        }
        pkSpawns.add(pos);
    }

    // TODO don't remove while iterating over list
    @Override
    public void RemoveLobbySpawn(Vector3f pos) {
        for (int i = 0; i < lobbySpawns.size(); i++) {
            if (lobbySpawns.get(i).equals(pos)) {
                lobbySpawns.remove(i);
                return;
            }
        }
    }

    // TODO don't remove while iterating over list
    @Override
    public void RemovePlayerSpawn(Vector3f pos) {
        for (int i = 0; i < playerSpawns.size(); i++) {
            if (playerSpawns.get(i).equals(pos)) {
                playerSpawns.remove(i);
                return;
            }
        }
    }

    // TODO don't remove while iterating over list
    @Override
    public void RemovePKSpawn(Vector3f pos) {
        for (int i = 0; i < pkSpawns.size(); i++) {
            if (pkSpawns.get(i).equals(pos)) {
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
                if (lobbySpawns.size() > 0) {
                    icms.setPlayerSpawnLocation(playerName, (Vector3f) getRandListItem(lobbySpawns));
                }
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
                ptsSys.AddPoints(pks.get(i),pkConfig.playerDeathAward);
            }
        }
        icms.sendMessageAll("Someone has died.");
    }

    @Override
    public void OnPlayerRespawn(String playerName) {
        // Give player items based on their role, etc
        givePlayerRoleBasedItems(playerName);
    }

    private void givePlayerRoleBasedItems(String playerName) {
        Boolean isPk = pks.contains(playerName);
        if (isPk) {
           for (String item: pkConfig.pkSpawnItems) {
               icms.givePlayerItem(playerName, item);
           }
           return;
        }

        for (String item:
             pkConfig.playerSpawnItems) {
            icms.givePlayerItem(playerName, item);
        }
    }

    @Override
    public void OnPlayerEntityKill(String playerName, UUID uuid) {
        int killAward = mobAreaManager.registerEntityDeath(uuid);
        if (killAward > 0) {
            ptsSys.AddPoints(playerName, killAward);
        }
    }

    @Override
    public void OnPlayerPlayerKill(String killer, String dead) {
        if (pks.contains(killer) && !pks.contains(dead)) {
            for (int i = 0; i < pks.size(); i++) {
                ptsSys.AddPoints(pks.get(i),pkConfig.playerDeathAward);
            }
        }
    }

    @Override
    public void OnPlayerlessEntityDeath(UUID uuid) {
        mobAreaManager.registerEntityDeath(uuid);
    }

    @Override
    public void OnPlayerJoinGame(String playerName) {
        // First spawn them in the lobby if there are lobby spawns
        if (lobbySpawns.size() > 0) {
            icms.teleportPlayer(playerName, (Vector3f)getRandListItem(lobbySpawns));
            icms.setPlayerSpawnLocation(playerName, (Vector3f)getRandListItem(lobbySpawns));
        }
        // Only teleports a player if a game is in progress,
        // AND the player was part of the game
        // this accounts for players disconnecting accidentally
        // - if a player is cheating, it is up to YOU (me, I am talking to myself and I) to ban them
        DoPlayerPKRespawn(playerName);
    }

    @Override
    public void OnPlayerCompletesObjective(Vector3f loc, String playerName, int award) {
        icms.sendMessageAll(EnumChatFormatting.GREEN + "An Objective Has Been Completed");
        ptsSys.AddPoints(playerName, award);

        int remaining = remainingCrewObjectives.get(playerName);
        if (remaining > 0) remaining -= 1;

        if (remaining > 0 ) {
            List<Vector3f> objectives = objManager.getAvailableObjectives();
            Vector3f remove = null;
            for(int i = 0; i < objectives.size(); i++) {
                if (loc.equals(objectives.get(i))) {
                    remove = objectives.get(i);
                }
            }
            // Attempt to remove the last objective so we don't give the player the one they just had
            // not sure if this works
            objectives.remove(remove);
            objManager.assignObjective(playerName, (Vector3f) getRandListItem(objectives));
        }

        remainingCrewObjectives.put(playerName, remaining);
    }

    private void assignAndSetupInitialObjectives() {
        List<Vector3f> avail = objManager.getAvailableObjectives();
        for ( int i = 0; i < crew.size(); i++) {
            remainingCrewObjectives.put(crew.get(i),pkConfig.perPlayerObjectives);
            Vector3f randObjective = (Vector3f) getRandListItem(avail);
            avail.remove(randObjective);
            objManager.assignObjective(crew.get(i), randObjective);
        }
    }

    @Override
    public PointsSystem GetPtsSystem() {
        return this.ptsSys;
    }

    @Override
    public MobAreaManager GetMobAreaManager() {
        return this.mobAreaManager;
    }

    @Override
    public ObjectiveManager GetObjectiveManager() {
        return this.objManager;
    }
}
