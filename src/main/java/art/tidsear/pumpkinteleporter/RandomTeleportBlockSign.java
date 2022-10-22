package art.tidsear.pumpkinteleporter;

import art.tidsear.pumpkinking.PumpkinKingMod;
import net.minecraft.block.BlockSign;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public class RandomTeleportBlockSign extends BlockSign {

    public RandomTeleportBlockSign(boolean onGround) {
        super(TileEntitySign.class, onGround);
    }

    @Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
        try {
            TileEntitySign te = TileEntitySign.class.newInstance();
            return te;
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    public boolean onBlockActivated(@NotNull World world, int x, int y, int z, EntityPlayer player, int handIn, float hitX, float hitY, float hitZ) {
        if (world.isRemote) {
            return true;
        }
        PumpkinKingMod.pkGameMode.DoRandomTeleportToPlayerSpawns(player.getDisplayName());
        return true;
    }
}
