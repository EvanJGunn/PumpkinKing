package art.tidsear.pumpkinking;

import art.tidsear.pumpkinstore.StoreSignBlockSign;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;

public class PumpkinKingModBlocks {
    public static final Block.SoundType soundTypeWood = new Block.SoundType("wood", 1.0F, 1.0F);
    public static void RegisterBlocks() {
        GameRegistry.registerBlock((new StoreSignBlockSign(true)).setHardness(1.0F).setStepSound(soundTypeWood).setBlockName("store_sign"),"store_standing_sign");
        GameRegistry.registerBlock((new StoreSignBlockSign(false)).setHardness(1.0F).setStepSound(soundTypeWood).setBlockName("store_sign"),"store_wall_sign");
    }

    public static Block getBlock(String name){
        return (Block)Block.blockRegistry.getObject(name);
    }
}
