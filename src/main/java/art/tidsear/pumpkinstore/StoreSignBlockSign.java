package art.tidsear.pumpkinstore;

import art.tidsear.pumpkingamemode.PKState;
import art.tidsear.pumpkinking.PumpkinKingMod;
import art.tidsear.pumpkinking.PumpkinKingModBlocks;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.BlockSign;
import net.minecraft.command.CommandGive;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.Sys;

public class StoreSignBlockSign extends BlockSign {

    private ICommandSender sender;
    private CommandGive cmdGive;
    public StoreSignBlockSign(boolean onGround) {
        super(TileEntitySign.class, onGround);
        cmdGive = new CommandGive();
        sender = new StoreSender();
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
        System.out.println("CLICKED BLOCK");
        if (world.isRemote) {
            return true;
        }
        TileEntitySign tes = (TileEntitySign) world.getTileEntity(x,y,z);

        if (tes == null) {
            System.out.println("tes was null");
            return false;
        }

        int cost = parseBuySign(tes.signText[0]);
        if (cost == -1){
            System.out.println("cost was -1");
        }
        if (cost == -1 || cost < 0) return false;

        if (player.capabilities.isCreativeMode) {
            player.addChatMessage(new ChatComponentText("Cost was: "+cost));
            cmdGive.processCommand(this.sender,new String[]{player.getDisplayName(), tes.signText[1]+":"+tes.signText[2]+tes.signText[3]});
        } else if (PumpkinKingMod.pkGameMode.getState() != PKState.IDLE) {
            // logic for checking / subtracting points from a player
        }
        return true;
    }

    private int parseBuySign(String line) {
        String[] parts = line.split(" ");
        if (parts.length != 2) {
            System.out.println("hello");
            return -1;
        }
        if (!parts[0].equals("Buy")) {
            return -1;
        }

        if (parts[1].contains("pts")) {
            parts[1] = parts[1].replace("pts","");
        }
        if (parts[1].contains("PTS")) {
            parts[1] = parts[1].replace("PTS","");
        }
        if (parts[1].contains("pt")) {
            parts[1] = parts[1].replace("pt","");
        }
        if (parts[1].contains("PT")) {
            parts[1] = parts[1].replace("PT","");
        }
        try {
            return Integer.parseInt(parts[1]);
        } catch (NumberFormatException e) {
            System.out.println("exception?");
            return -1;
        }
    }
}

class StoreSender implements ICommandSender {

    @Override
    public String getCommandSenderName() {
        return "Store";
    }

    @Override
    public IChatComponent func_145748_c_() {
        return new ChatComponentText(this.getCommandSenderName());
    }

    @Override
    public void addChatMessage(IChatComponent p_145747_1_) {
        System.out.println(p_145747_1_.getUnformattedText());
    }

    @Override
    public boolean canCommandSenderUseCommand(int p_70003_1_, String p_70003_2_) {
        System.out.println("HI");
        return true;
    }

    @Override
    public ChunkCoordinates getPlayerCoordinates() {
        return null;
    }

    @Override
    public World getEntityWorld() {
        return null;
    }
}