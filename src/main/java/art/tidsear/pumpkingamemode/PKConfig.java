package art.tidsear.pumpkingamemode;

import java.util.ArrayList;
import java.util.Arrays;

// We don't have a pkDeathAward because A. it would be a test, B. we want to have players focus on objectives
// C. we want the pk to be scary, and we don't want the players to snowball exclusively off of killing the pk
public class PKConfig {
    public float roundTimeSeconds = 1200; // default 20 minutes
    public int playerDeathAward = 25;

    public float timeBasedAwardSeconds = 60; // default 1 min

    public int pkTimeBasedAward = 100;
    public int playerTimeBasedAward = 25; // You generally want player awards to be 1/4 of PK awards (if you have 4 guys)
    public ArrayList<String> playerSpawnItems = new ArrayList<String>();
    public ArrayList<String> pkSpawnItems = new ArrayList<String>();
    public ArrayList<String> randomItems = new ArrayList<String>();

    public String[] toStrings() {
        String[] out = new String[]{
                "roundTimeSeconds = " + roundTimeSeconds,
                "playerDeathAward = " + playerDeathAward,
                "timeBasedAwardSeconds = " + timeBasedAwardSeconds,
                "pkTimeBasedAward = " + pkTimeBasedAward,
                "playerTimeBasedAward = " + playerTimeBasedAward,
                "playerSpawnItems = " + Arrays.toString(playerSpawnItems.toArray()),
                "pkSpawnItems = " + Arrays.toString(pkSpawnItems.toArray()),
                "randomItems = " + Arrays.toString(randomItems.toArray())
        };
        return out;
    }

    // This is a little hacky, but I want to be able to edit the config from in game
    public void setValue(String configKey, String value) {
        try {
            switch (configKey) {
                case "roundTimeSeconds":
                    this.roundTimeSeconds = Float.parseFloat(value);
                    break;
                case "playerDeathAward":
                    this.playerDeathAward = Integer.parseInt(value);
                    break;
                case "timeBasedAwardSeconds":
                    this.timeBasedAwardSeconds = Float.parseFloat(value);
                    break;
                case "pkTimeBasedAward":
                    this.pkTimeBasedAward = Integer.parseInt(value);
                    break;
                case "playerTimeBasedAward":
                    this.playerTimeBasedAward = Integer.parseInt(value);
                    break;
            }
        } catch (Exception e) {

        }
    }

    public void addValue(String configKey, String value) {
        try {
            switch (configKey) {
                case "playerSpawnItems":
                    this.playerSpawnItems.add(value);
                    break;
                case "pkSpawnItems":
                    this.pkSpawnItems.add(value);
                    break;
                case "randomItems":
                    this.randomItems.add(value);
                    break;
            }
        } catch (Exception e) {

        }
    }

    public void resetArrayList(String configKey) {
        try {
            switch (configKey) {
                case "playerSpawnItems":
                    this.playerSpawnItems.clear();
                    break;
                case "pkSpawnItems":
                    this.pkSpawnItems.clear();
                    break;
                case "randomItems":
                    this.randomItems.clear();
                    break;
            }
        } catch (Exception e) {

        }
    }
}
