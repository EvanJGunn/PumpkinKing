package art.tidsear.pumpkininterface;

import art.tidsear.pumpkinking.PumpkinKingMod;
import art.tidsear.utility.Vector3f;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;

public class CommandPlayerPoints extends CommandBase {

    @Override
    public String getCommandName() {
        return "playerpoints";
    }

    @Override
    public String getCommandUsage(ICommandSender s) {
        return "/playerpoints <add|subtract|set> <player> <amount>";
    }

    @Override
    public void processCommand(ICommandSender s, String[] args) {
        try {
            String player = args[1];
            int amount = Integer.parseInt(args[2]);
            switch (args[0]) {
                case "add":
                    PumpkinKingMod.pkGameMode.GetPtsSystem().AddPoints(player,amount);
                    return;
                case "subtract":
                    PumpkinKingMod.pkGameMode.GetPtsSystem().SetPoints(player,PumpkinKingMod.pkGameMode.GetPtsSystem().GetPoints(player) - amount);
                    return;
                case "set":
                    PumpkinKingMod.pkGameMode.GetPtsSystem().SetPoints(player, amount);
                    return;
            }
            s.addChatMessage(new ChatComponentText("Please use either add, subtract, or set"));
        } catch (NumberFormatException e) {
            s.addChatMessage(new ChatComponentText("Error processing input: " + e.getMessage()));
        }
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }
}
