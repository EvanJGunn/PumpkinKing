package art.tidsear.pumpkin2pumpkin;

import java.io.Serializable;

public class LocalData implements Serializable {
    public int playerPoints;
    public String playerRole;
    // TODO Objectives, etc
    public LocalData(int playerPoints, String playerRole) {
        this.playerPoints = playerPoints;
        this.playerRole = playerRole;
    }

    public LocalData() {
        this.playerPoints = 0;
        this.playerRole = "";
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
}
