package art.tidsear.pumpkinpoints;

import java.util.HashMap;
import java.util.Map;

// Cant be bothered to account for overflow
// if anyone manages it, good for them
public class PointsSystem {
    private Map<String, Integer> playerPoints;
    public PointsSystem() {
        playerPoints = new HashMap<>();
    }

    public void AddPoints(String player, int points) {
        if (playerPoints.containsKey(player)) {
            playerPoints.put(player,playerPoints.get(player)+points);
        } else {
            playerPoints.put(player, points);
        }
    }

    public void ResetPoints() {
        playerPoints = new HashMap<>();
    }

    public int GetPoints(String player) {
        if (playerPoints.containsKey(player)) {
            return playerPoints.get(player);
        }
        return 0;
    }

    // Returns true if you can withdraw that amount of points
    public boolean WithdrawPoints(String player, int points) {
        if (points < 0) {
            return false;
        }
        if (playerPoints.containsKey(player)) {
            int curPts = playerPoints.get(player);
            if (curPts >= points) {
                playerPoints.put(player, curPts-points);
            }
        }
        return points == 0;
    }
}
