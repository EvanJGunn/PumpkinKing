package art.tidsear.mobarea;

import art.tidsear.pumpkininterface.InternalCommands;
import art.tidsear.utility.Vector3f;
import sun.awt.Mutex;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class MobArea {
    private List<Vector3f> locations;
    private String mobType;
    private InternalCommands icms;
    private List<UUID> mobs;
    private int killAward;
    private int maxPopulation;
    private float replenishTime;
    private long nextSpawnTime;

    private Random randGen;

    public MobArea(InternalCommands icms, String mobType, int killAward, int maxPopulation, float replenishTime) {
        this.mobType = mobType;
        locations = new ArrayList<>();
        mobs = new ArrayList<>();

        this.killAward = killAward;
        this.maxPopulation = maxPopulation;
        this.replenishTime = replenishTime;
        randGen = new Random();

        this.icms = icms;

        // Set it up so that on first update it will be ready to spawn instantly
        nextSpawnTime = System.currentTimeMillis();
    }

    public void Update() {
        if (mobs.size() < maxPopulation) {
            if (System.currentTimeMillis() > nextSpawnTime) {
                if (locations.size() > 0) {
                    UUID uuid = icms.spawnEntity(mobType, (Vector3f) getRandListItem(locations));
                    if (uuid != null) {
                        mobs.add(uuid);
                    }
                    nextSpawnTime = System.currentTimeMillis() + (long) (1000 * replenishTime);
                }
            }
        } else {
            // A good optimization would be to do this every few seconds instead of every Update
            clearUnloaded();

            nextSpawnTime = System.currentTimeMillis() + (long) (1000 * replenishTime);
        }
    }

    public void AddSpawnPos(Vector3f pos) {
        locations.add(pos);
    }

    public void Reset() {
        for (UUID uuid:
             mobs) {
            icms.slayEntity(uuid);
        }
        mobs.clear();
    }

    // Check as in get the kill award, if this method is called, we know the mob is dead
    public int checkKilledMob(UUID uuid) {
        if (mobs.contains(uuid)) {
            mobs.remove(uuid);
            return killAward;
        }
        return -1;
    }

    // This is not coded for large minecraft servers, etc, etc
    private void clearUnloaded() {
        List<UUID> removes = new ArrayList<>();
        for (int i = 0; i < mobs.size(); i++) {
            if (!icms.isEntityLiving(mobs.get(i))) {
                removes.add(mobs.get(i));
            }
        }
        mobs.removeAll(removes);
    }

    public String print() {
        return String.format("Mob: %s, KillAward: %s, MaxPopulation: %s, ReplenishTimeSeconds: %s, MobList: %s", mobType, killAward, maxPopulation, replenishTime, mobs.toString());
    }


    // Yes this is duplicated code, what are you going to fix it? o.O
    private Object getRandListItem(List list) {
        int randIndex = randGen.nextInt(list.size());
        return list.get(randIndex);
    }
}
