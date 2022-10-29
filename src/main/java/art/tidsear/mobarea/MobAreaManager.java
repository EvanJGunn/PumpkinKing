package art.tidsear.mobarea;

import art.tidsear.pumpkininterface.InternalCommands;
import art.tidsear.utility.Vector3f;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class MobAreaManager {
    private Map<String,MobArea> mobAreas;

    private InternalCommands icms;
    public MobAreaManager(InternalCommands icms) {
        mobAreas = new HashMap<>();
        this.icms = icms;
    }

    public void resetMobAreas() {
        mobAreas.forEach((s, mobArea) -> {
            mobArea.Reset();
        });
    }

    public void clearMobAreas() {
        mobAreas.clear();
    }

    public void deleteMobArea(String ma) {
        mobAreas.remove(ma);
    }

    public void registerMobArea(String name, String mobtype, int killAward, int maxPop, int replenishRate) {
        if (!mobAreas.containsKey(name)) {
            mobAreas.put(name, new MobArea(icms, mobtype, killAward, maxPop, replenishRate));
        }
    }

    public void addPos(String name, Vector3f pos) {
        mobAreas.get(name).AddSpawnPos(pos);
    }

    public void doUpdate() {
        mobAreas.forEach((s, mobArea) -> {
            mobArea.Update();
        });
    }

    public String[] printAreas() {
        List<String> prints = new ArrayList<>();
        mobAreas.forEach((s,mobArea) -> {
            // idk if this works, I don't love closure lookin' things
            // javascript flashbacks
            prints.add("Name: "+s+ " "+ mobArea.print());
        });
        return prints.toArray(new String[0]);
    }

    public int registerEntityDeath(UUID uuid) {
        AtomicInteger returnAward = new AtomicInteger(-1);
        mobAreas.forEach((s, mobArea) -> {
            // Idk why I need an atomic integer :/
            int award = mobArea.checkKilledMob(uuid);
            if (award > 0) {
                returnAward.set(award);
            }
        });
        return returnAward.get();
    }
}
