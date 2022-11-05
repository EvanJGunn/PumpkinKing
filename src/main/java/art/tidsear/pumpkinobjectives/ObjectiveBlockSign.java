package art.tidsear.pumpkinobjectives;

import art.tidsear.pumpkinking.PumpkinKingMod;
import art.tidsear.utility.Vector3f;
import net.minecraft.block.BlockButton;
import net.minecraft.block.BlockSign;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.Sys;

import java.util.Random;

public class ObjectiveBlockSign extends BlockSign {
    // Definitely should be using ticks somehow, but its and old version of minecraft and I don't care :p
    private final float activeTime = 0.5f;
    public ObjectiveBlockSign(boolean onGround) {
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

        String metadata = PumpkinKingMod.metadata.get(new Vector3f(x,y,z));
        long expireTime = 0;
        if (metadata != null) {
            expireTime = Long.parseLong(metadata);
        }

        if ((expireTime + (long)(1000f * activeTime)) < System.currentTimeMillis()) {

            // Do gamemode logic
            player.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "Activated Objective!"));
            PumpkinKingMod.pkGameMode.GetObjectiveManager().activateObjective(player.getDisplayName(), new Vector3f(x, y, z));

            world.playSoundEffect((double) x + 0.5D, (double) y + 0.5D, (double) z + 0.5D, "random.click", 0.3F, 0.6F);
            // Set the expiration time, prevents spamming and also sets duration of redstone signal
            PumpkinKingMod.metadata.put(new Vector3f(x, y, z), Long.toString(System.currentTimeMillis() + (long) (1000f * activeTime)));
            this.doNeighborNotifications(world, x, y, z);
            // 50 milliseconds per tick, 250 milliseconds/50 = 5 ticks for example, add 5 ticks to be safe
            world.scheduleBlockUpdate(x, y, z, this, 12);
        }
        return true;
    }

    @Override
    public int isProvidingWeakPower(IBlockAccess blockAccess, int x, int y, int z, int dir) {
        String metadata = PumpkinKingMod.metadata.get(new Vector3f(x,y,z));
        if (metadata != null) {
            long expireTime = Long.parseLong(metadata);
            if (System.currentTimeMillis() < expireTime) {
                return 15;
            }
        }
        return 0;
    }

    @Override
    public int isProvidingStrongPower(IBlockAccess blockAccess, int x, int y, int z, int dir) {
        String metadata = PumpkinKingMod.metadata.get(new Vector3f(x,y,z));
        if (metadata != null) {
            long expireTime = Long.parseLong(metadata);
            if (System.currentTimeMillis() < expireTime) {
                return 15;
            }
        }
        return 0;
    }

    @Override
    public void updateTick(World world, int x, int y, int z, Random random) {
        doNeighborNotifications(world, x, y, z);
    }

    private void doNeighborNotifications(World world, int x, int y, int z) {
        world.notifyBlocksOfNeighborChange(x, y, z, this);

        world.notifyBlocksOfNeighborChange(x - 1, y, z, this);

        world.notifyBlocksOfNeighborChange(x + 1, y, z, this);

        world.notifyBlocksOfNeighborChange(x, y, z - 1, this);

        world.notifyBlocksOfNeighborChange(x, y, z + 1, this);

        world.notifyBlocksOfNeighborChange(x, y - 1, z, this);
    }
}
