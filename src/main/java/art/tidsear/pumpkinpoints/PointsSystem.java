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
        if (playerPoints.containsKey(player.toLowerCase())) {
            playerPoints.put(player.toLowerCase(),playerPoints.get(player.toLowerCase())+points);
        } else {
            playerPoints.put(player.toLowerCase(), points);
        }
    }

    public void ResetPoints() {
        playerPoints = new HashMap<>();
    }

    public int GetPoints(String player) {
        if (playerPoints.containsKey(player.toLowerCase())) {
            return playerPoints.get(player.toLowerCase());
        }
        return 0;
    }

    public void SetPoints(String player, int points) {
        playerPoints.put(player.toLowerCase(), points);
    }

    // Returns true if you can withdraw that amount of points
    public boolean WithdrawPoints(String player, int points) {
        if (points < 0) {
            return false;
        }
        if (playerPoints.containsKey(player.toLowerCase())) {
            int curPts = playerPoints.get(player.toLowerCase());
            if (curPts >= points) {
                playerPoints.put(player.toLowerCase(), curPts-points);
                return true;
            }
        }
        return points == 0;
    }
}
