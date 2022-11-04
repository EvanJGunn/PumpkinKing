package art.tidsear.pumpkin2pumpkin;

import java.io.Serializable;

public class LocalData implements Serializable {
    public int playerPoints;
    public String playerRole;
    public String objective;
    public LocalData(int playerPoints, String playerRole, String objective) {
        this.playerPoints = playerPoints;
        this.playerRole = playerRole;
        this.objective = objective;
    }

    public LocalData() {
        this.playerPoints = 0;
        this.playerRole = "";
        this.objective = "";
    }

    public int getPlayerPoints() {
        return playerPoints;
    }

    public void setPlayerPoints(int playerPoints) {
        this.playerPoints = playerPoints;
    }

    public String getPlayerRole() {
        return playerRole;
    }

    public void setPlayerRole(String playerRole) {
        this.playerRole = playerRole;
    }

    public String getObjective() {
        return objective;
    }

    public void setObjective(String objective) {
        this.objective = objective;
    }
}
