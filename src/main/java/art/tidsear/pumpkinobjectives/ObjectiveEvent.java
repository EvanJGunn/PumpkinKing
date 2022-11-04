package art.tidsear.pumpkinobjectives;

import art.tidsear.utility.Vector3f;

public class ObjectiveEvent {
    private Vector3f objective;
    private String playerName;
    private float expireTime;

    public ObjectiveEvent(Vector3f objective, String playerName, float expireTime) {
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

    public float getExpireTime() {
        return expireTime;
    }
}
