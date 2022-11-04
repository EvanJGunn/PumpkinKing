package art.tidsear.pumpkinking;

import art.tidsear.pumpkinobjectives.ObjectiveBlockSign;
import art.tidsear.pumpkinstore.StoreSignBlockSign;
import art.tidsear.pumpkinteleporter.RandomTeleportBlockSign;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;

public class PumpkinKingModBlocks {
    public static final Block.SoundType soundTypeWood = new Block.SoundType("wood", 1.0F, 1.0F);
    public static void RegisterBlocks() {
        // I'm not sure if there is some sort of conflict when registering for the same block name, this seemed to be the way minecraft does it for signs though?
        GameRegistry.registerBlock((new StoreSignBlockSign(true)).setHardness(1.0F).setStepSound(soundTypeWood).setBlockName("store_sign"),"store_standing_sign");
        GameRegistry.registerBlock((new StoreSignBlockSign(false)).setHardness(1.0F).setStepSound(soundTypeWood).setBlockName("store_sign"),"store_wall_sign");

        GameRegistry.registerBlock((new RandomTeleportBlockSign(true)).setHardness(1.0F).setStepSound(soundTypeWood).setBlockName("teleport_sign"),"randtele_standing_sign");
        GameRegistry.registerBlock((new RandomTeleportBlockSign(false)).setHardness(1.0F).setStepSound(soundTypeWood).setBlockName("teleport_sign"),"randtele_wall_sign");

        GameRegistry.registerBlock((new ObjectiveBlockSign(true)).setHardness(1.0F).setStepSound(soundTypeWood).setBlockName("objective_sign"),"objective_standing_sign");
        GameRegistry.registerBlock((new ObjectiveBlockSign(false)).setHardness(1.0F).setStepSound(soundTypeWood).setBlockName("objective_sign"),"objective_wall_sign");
    }

    public static Block getBlock(String name){
        return (Block)Block.blockRegistry.getObject(name);
    }
}
