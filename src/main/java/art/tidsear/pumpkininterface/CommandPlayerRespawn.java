package art.tidsear.pumpkininterface;

import art.tidsear.pumpkinking.PumpkinKingMod;
import art.tidsear.utility.Vector3f;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;

public class CommandPlayerRespawn extends CommandBase {

    @Override
    public String getCommandName() {
        return "playerrespawn";
    }

    @Override
    public String getCommandUsage(ICommandSender s) {
        return "/playerrespawn <player>";
    }

    @Override
    public void processCommand(ICommandSender s, String[] args) {
        if(args.length != 1) {
            return;
        }
        PumpkinKingMod.pkGameMode.DoPlayerPKRespawn(args[0]);
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }
}
