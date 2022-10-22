package art.tidsear.pumpkinteleporter;

import art.tidsear.pumpkinking.PumpkinKingModBlocks;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class RandomTeleportSignItem extends Item {

    public RandomTeleportSignItem() {
        this.maxStackSize = 16;
        this.setCreativeTab(CreativeTabs.tabDecorations);
    }

    /**
     * Callback for item usage. If the item does something special on right clicking, he will have one of those. Return
     * True if something happen and false if it don't. This is for ITEMS, not BLOCKS
     */
    public boolean onItemUse(ItemStack itemStack, EntityPlayer entityPlayer, World world, int x, int y, int z, int facing, float hitX, float hitY, float hitZ) {
        if (facing == 0) {
            return false;
        } else if (!world.getBlock(x, y, z).getMaterial().isSolid()) {
            return false;
        } else {
            if (facing == 1) {
                ++y;
            }

            if (facing == 2) {
                --z;
            }

            if (facing == 3) {
                ++z;
            }

            if (facing == 4) {
                --x;
            }

            if (facing == 5) {
                ++x;
            }

            if (!entityPlayer.canPlayerEdit(x, y, z, facing, itemStack)) {
                return false;
            } else if (!PumpkinKingModBlocks.getBlock("randtele_standing_sign").canPlaceBlockAt(world, x, y, z)) {
                return false;
            } else if (world.isRemote) {
                return true;
            } else {
                if (facing == 1) {
                    int i1 = MathHelper.floor_double((double)((entityPlayer.rotationYaw + 180.0F) * 16.0F / 360.0F) + 0.5D) & 15;
                    world.setBlock(x, y, z, PumpkinKingModBlocks.getBlock("pumpkinking:randtele_standing_sign"), i1, 3);
                    Block block = PumpkinKingModBlocks.getBlock("pumpkinking:randtele_standing_sign");
                } else {
                    world.setBlock(x, y, z, PumpkinKingModBlocks.getBlock("pumpkinking:randtele_wall_sign"), facing, 3);
                }

                --itemStack.stackSize;
               TileEntitySign tileEntitySign = (TileEntitySign)world.getTileEntity(x, y, z);

                if (tileEntitySign != null) {
                    entityPlayer.func_146100_a(tileEntitySign);
                }

                return true;
            }
        }
    }
}
