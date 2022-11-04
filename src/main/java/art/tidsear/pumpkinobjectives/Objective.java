package art.tidsear.pumpkinobjectives;

public class Objective {
    private int pointsAward;
    private String description;

    private float expirationSeconds;

    public Objective(int pointsAward, String description, float expirationSeconds) {
        this.description = description;
        this.pointsAward = pointsAward;
        this.expirationSeconds = expirationSeconds;
    }

    public int getPointsAward() {
        return pointsAward;
    }

    public String getDescription() {
        return description;
    }

    public float getExpirationSeconds() {
        return expirationSeconds;
    }
}
