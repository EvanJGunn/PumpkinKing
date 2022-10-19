package art.tidsear.utility;

public class Vector3f {
    private float x;
    private float y;
    private float z;

    public Vector3f(Vector3f original) {
        this.x = original.x;
        this.y = original.y;
        this.z = original.z;
    }

    public Vector3f(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getZ() {
        return z;
    }

    public void setZ(float z) {
        this.z = z;
    }
}
