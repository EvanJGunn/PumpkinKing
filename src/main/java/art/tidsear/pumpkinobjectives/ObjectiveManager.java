package art.tidsear.pumpkinobjectives;

import art.tidsear.pumpkinking.PumpkinKingMod;
import art.tidsear.utility.Vector3f;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

// This class is basically screaming out for unit tests, but I don't have time
public class ObjectiveManager {
    private Map<Vector3f,Objective> objectives;

    // No guarantee that pairs will have matching objectives, must be checked
    private BiMap<Vector3f,Vector3f> pairs;
    private List<ObjectiveEvent> objectiveEvents;

    private Map<String,Vector3f> playerToObjective;

    public ObjectiveManager() {
        objectives = new HashMap<>();
        pairs = HashBiMap.create();
        objectiveEvents = new ArrayList<>();
        playerToObjective = new HashMap<>();
    }

    public void resetAssignments() {
        objectiveEvents.clear();
        playerToObjective.clear();
    }

    // Safe to modify the return value
    public List<Vector3f> getAvailableObjectives() {
        Vector3f[] allObjs = objectives.keySet().toArray(new Vector3f[0]);
        List<Vector3f> availObjs = new ArrayList<>();

        // Add unassigned objectives to the list of available objectives
        for (int i = 0; i < allObjs.length; i++) {
            Vector3f pairing = getPairing(allObjs[i]);
            // Skip if the pair is assigned to a player
            if (pairing != null) {
                if (playerToObjective.containsValue(pairing)){
                    continue;
                }
            }
            // Add if objective is unassigned
            if (!playerToObjective.containsValue(allObjs[i])) {
                availObjs.add(allObjs[i]);
            }
        }

        // Remove one half of unassigned pairs that remain in the available objective list
        List<Vector3f> removablePairing = new ArrayList<>();
        for (int i = 0; i < availObjs.size(); i++) {
            if (removablePairing.contains(availObjs.get(i))) continue;
            Vector3f pairing = getPairing(availObjs.get(i));
            if (pairing != null) {
                removablePairing.add(pairing);
            }
        }
        availObjs.removeAll(removablePairing);
        return availObjs;
    }

    public Objective getObjective(String player) {
        Vector3f objective = playerToObjective.get(player);
        return objectives.get(objective);
    }

    // up to caller to check if the objective is available
    public void assignObjective(String player, Vector3f objective) {
        if (!playerToObjective.containsKey(player)) {
            playerToObjective.put(player, objective);
        }
    }

    public void activateObjective(String player, Vector3f objective) {
        Objective obj = objectives.get(objective);

        if (obj != null) {
            // Probably a better way
            if (pairsContainsKey(objective)) {
                long expireTime = System.currentTimeMillis() + (long)(obj.getExpirationSeconds() * 1000);
                objectiveEvents.add(new ObjectiveEvent(objective, player, expireTime));
                doPairObjectivesCheck();
            } else {
                // We cant do this check earlier in case of a player helping on a paired objective
                if (playerToObjective.get(player).equals(objective)) {
                    playerToObjective.remove(player);
                    // Binding this to the pkGameMode is a little sus, but I am too lazy to make this class completely
                    // independent. Up to pkGameMode to not reassign the same objective immediately after the player completes it.
                    PumpkinKingMod.pkGameMode.OnPlayerCompletesObjective(objective, player, obj.getPointsAward());
                }
            }
        }
    }

    // This is the big one
    private void doPairObjectivesCheck() {
        // This definitely might not be efficient, but I am trying to finish the game
        List<ObjectiveEvent> oeRemovals = new ArrayList<>();

        long currentTime = System.currentTimeMillis();
        for (int i = 0; i < objectiveEvents.size(); i++) {
            // Skip/trash if expired
            if (objectiveEvents.get(i).getExpireTime() <= currentTime) {
                oeRemovals.add(objectiveEvents.get(i));
                continue;
            }

            boolean cont = false;
            // Skip if the pair matched already in this loop
            for (int j = 0; j < oeRemovals.size(); j++) {
                if (objectiveEvents.get(i) == oeRemovals.get(j)) {
                    cont = true;
                    break;
                }
            }
            if (cont) continue;

            // Get the pairing
            Vector3f pairing = getPairing(objectiveEvents.get(i).getObjective());

            // Loop again to see if there is a brother event
            for (int j = 0; j < objectiveEvents.size(); j++) {
                // Skip the current event, we know they are the same, because we do not modify the
                // list until outside all of the loops
                if (j == i) continue;
                if (objectiveEvents.get(j).getObjective().equals(pairing)) {
                    // add both to trash
                    oeRemovals.add(objectiveEvents.get(i));
                    oeRemovals.add(objectiveEvents.get(j));

                    // check that if either of the players had the paired objective,
                    // should be impossible for both to have it
                    String playerA = objectiveEvents.get(i).getPlayerName();
                    String playerB = objectiveEvents.get(j).getPlayerName();
                    if (playerToObjective.get(playerA).equals(objectiveEvents.get(i).getObjective()) || playerToObjective.get(playerA).equals(objectiveEvents.get(j).getObjective())) {
                        playerToObjective.remove(playerA);
                        try {
                            PumpkinKingMod.pkGameMode.OnPlayerCompletesObjective(objectiveEvents.get(i).getObjective(), playerA, objectives.get(objectiveEvents.get(i).getObjective()).getPointsAward());
                        } catch (Exception e) {
                            // TODO put this catch in here for solo testing purposes
                        }
                    }
                    else if (playerToObjective.get(playerB).equals(objectiveEvents.get(i).getObjective()) || playerToObjective.get(playerB).equals(objectiveEvents.get(j).getObjective())) {
                        playerToObjective.remove(playerB);
                        try {
                            PumpkinKingMod.pkGameMode.OnPlayerCompletesObjective(objectiveEvents.get(i).getObjective(), playerB, objectives.get(objectiveEvents.get(i).getObjective()).getPointsAward());
                        } catch (Exception e) {
                            // TODO put this catch in here for solo testing purposes
                        }
                    }
                }
            }
        }

        objectiveEvents.removeAll(oeRemovals);
    }

    public String[] printObjs() {
        List<String> prints = new ArrayList<>();
        objectives.forEach((loc,objective) -> prints.add("Objective: "+loc.print()+ " Desc: "+ objective.getDescription()));
        return prints.toArray(new String[0]);
    }

    public String[] printPairs() {
        List<String> prints = new ArrayList<>();
        pairs.forEach((loc1,loc2) -> prints.add("Pair: "+loc1.print()+ " and "+ loc2.print()));
        return prints.toArray(new String[0]);
    }

    public String[] printAssigned() {
        List<String> prints = new ArrayList<>();
        playerToObjective.forEach((player,loc) -> prints.add("Assigned: "+player+ " and "+ loc.print()));
        return prints.toArray(new String[0]);
    }

    public String[] printEvents() {
        List<String> prints = new ArrayList<>();
        objectiveEvents.forEach((event) -> prints.add("Player: " + event.getPlayerName() +", Pos: "+event.getObjective().print() + ", Expiration: " + event.getExpireTime()));
        return prints.toArray(new String[0]);
    }

    public String[] printAvailObjs() {
        List<Vector3f> objs = getAvailableObjectives();
        List<String> prints = new ArrayList<>();
        for (int i = 0; i < objs.size(); i++) {
            prints.add(objs.get(i).print());
        }
        return prints.toArray(new String[0]);
    }

    // Could probably decouple from pairs, put in util or something, idk don't care enough to
    private Vector3f getPairing(Vector3f key) {
        Vector3f pairing = pairs.get(key);
        if (pairing == null) {
            pairing = pairs.inverse().get(key);
        }
        return pairing;
    }

    private boolean pairsContainsKey(Vector3f key) {
        boolean check = pairs.containsKey(key);
        if (check == false) {
            check = pairs.inverse().containsKey(key);
        }
        return check;
    }

    // DANGER ZONE
    // FUNCTIONS BELOW THIS LINE SHOULD NOT BE CALLED WHILE A GAME IS RUNNING
    public void deleteObjective(Vector3f loc) {
        if (objectives.containsKey(loc)) {
            objectives.remove(loc);
        }
    }

    public void clearAll() {
        objectives.clear();
        pairs.clear();
        objectiveEvents.clear();
        playerToObjective.clear();
    }

    public void addObjective(Vector3f loc, int pointsAward, String desc, long expirationSeconds) {
        if (!objectives.containsKey(loc)) {
            objectives.put(loc, new Objective(pointsAward, desc, expirationSeconds));
        }
    }

    public boolean addPair(Vector3f loc1, Vector3f loc2) {
        if (loc1.equals(loc2)) {
            return false;
        }
        if (pairs.containsKey(loc1) || pairs.containsKey(loc2)) {
            return false;
        }
        pairs.put(loc1,loc2);
        return true;
    }
}
