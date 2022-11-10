package art.tidsear.pumpkininterface;

import art.tidsear.pumpkinking.PumpkinKingMod;
import art.tidsear.utility.Vector3f;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;

public class CommandDestroyCore extends CommandBase {

    @Override
    public String getCommandName() {
        return "destroycore";
    }

    @Override
    public String getCommandUsage(ICommandSender s) {
        return "/destroycore";
    }

    @Override
    public void processCommand(ICommandSender s, String[] args) {
        PumpkinKingMod.pkGameMode.DestroyCore();
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }
}