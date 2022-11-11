package art.tidsear.pumpkindoor;

import art.tidsear.pumpkininterface.InternalCommands;
import art.tidsear.utility.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class PumpkinShop {
    private int blockType = 1;
    private List<Vector3f> doorBlocks;

    private InternalCommands icms;

    public PumpkinShop(InternalCommands icms) {
        doorBlocks = new ArrayList<>();
        this.icms = icms;
    }

    public void setBlockType(int type) {
        this.blockType = type;
    }

    public void clearAllBlockPos() {
        doorBlocks.clear();
    }

    public void addBlockPos(Vector3f pos) {
        doorBlocks.add(pos);
    }

    public void unlockDoor() {
        for (int i = 0; i < doorBlocks.size(); i++) {
            icms.setBlock(doorBlocks.get(i), 0);
        }
    }

    public void resetDoor() {
        for (int i = 0; i < doorBlocks.size(); i++) {
            icms.setBlock(doorBlocks.get(i), blockType);
        }
    }
}
