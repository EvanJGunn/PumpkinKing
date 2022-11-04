package art.tidsear.pumpkinobjectives;

import art.tidsear.utility.Vector3f;

public class ObjectiveEvent {
    private Vector3f objective;
    private String playerName;
    private long expireTime;

    public ObjectiveEvent(Vector3f objective, String playerName, long expireTime) {
        this.objective = objective;
        this.playerName = playerName;
        this.expireTime = expireTime;
    }

    public Vector3f getObjective() {
        return objective;
    }

    public String getPlayerName() {
        return playerName;
    }

    public long getExpireTime() {
        return expireTime;
    }
}
