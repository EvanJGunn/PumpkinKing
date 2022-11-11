package art.tidsear.pumpkininterface;

import art.tidsear.pumpkinking.PumpkinKingMod;
import art.tidsear.utility.Vector3f;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;

public class CommandPKShop extends CommandBase {

    @Override
    public String getCommandName() {
        return "pkshop";
    }

    @Override
    public String getCommandUsage(ICommandSender s) {
        return "/pkshop";
    }

    @Override
    public void processCommand(ICommandSender s, String[] args) {
        try {
            switch (args[0]) {
                case "add":
                    PumpkinKingMod.pkGameMode.GetShop().addBlockPos(new Vector3f(Integer.parseInt(args[1]),Integer.parseInt(args[2]),Integer.parseInt(args[3])));
                    return;
                case "clearAll":
                    PumpkinKingMod.pkGameMode.GetShop().clearAllBlockPos();
                    return;
                case "reset":
                    PumpkinKingMod.pkGameMode.GetShop().resetDoor();
                    return;
                case "unlock":
                    PumpkinKingMod.pkGameMode.GetShop().unlockDoor();
                    return;
                case "setBlock":
                    PumpkinKingMod.pkGameMode.GetShop().setBlockType(Integer.parseInt(args[1]));
                    return;
            }
        } catch (NumberFormatException e) {
            s.addChatMessage(new ChatComponentText("Error processing input: "+e.getMessage()));
        }
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }
}
