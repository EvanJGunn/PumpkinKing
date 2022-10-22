package art.tidsear.pumpkininterface;

import art.tidsear.pumpkinking.PumpkinKingMod;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;

public class CommandResetPKG extends CommandBase {

    @Override
    public String getCommandName() {
        return "pkgreset";
    }

    @Override
    public String getCommandUsage(ICommandSender s) {
        return "/pkgreset";
    }

    @Override
    public void processCommand(ICommandSender s, String[] args) {
        PumpkinKingMod.pkGameMode.Reset();
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }
}