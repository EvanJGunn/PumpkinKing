package art.tidsear.pumpkinking;

import art.tidsear.pumpkinstore.StoreSignItem;
import art.tidsear.pumpkinteleporter.RandomTeleportSignItem;
import cpw.mods.fml.common.registry.GameRegistry;
//import net.minecraft.item.ItemSign;

public class PumpkinKingModItems {
    public static void RegisterItems() {
        GameRegistry.registerItem(new StoreSignItem().setUnlocalizedName("Store_Sign").setTextureName("sign"),"store_sign");

        GameRegistry.registerItem(new RandomTeleportSignItem().setUnlocalizedName("Teleport_Sign").setTextureName("sign"),"teleport_sign");
    }
}
