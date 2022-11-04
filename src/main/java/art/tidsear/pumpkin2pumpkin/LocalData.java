package art.tidsear.pumpkin2pumpkin;

import java.io.Serializable;

public class LocalData implements Serializable {
    public int playerPoints;

    public int objectivesCount;
    public int objectivesTotalCount;
    public String playerRole;

    public String objective;

    public LocalData(int playerPoints, int objectivesCount, int objectivesTotalCount, String playerRole, String objective) {
        this.playerPoints = playerPoints;
        this.objectivesCount = objectivesCount;
        this.objectivesTotalCount = objectivesTotalCount;
        this.playerRole = playerRole;
        this.objective = objective;
    }

    public LocalData() {
        this.playerPoints = 0;
        this.playerRole = "";
        this.objective = "";
        this.objectivesCount = 0;
        this.objectivesTotalCount = 0;
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

    public int getObjectivesCount() {
        return objectivesCount;
    }

    public void setObjectivesCount(int objectivesCount) {
        this.objectivesCount = objectivesCount;
    }

    public int getObjectivesTotalCount() {
        return objectivesTotalCount;
    }

    public void setObjectivesTotalCount(int objectivesTotalCount) {
        this.objectivesTotalCount = objectivesTotalCount;
    }
}
