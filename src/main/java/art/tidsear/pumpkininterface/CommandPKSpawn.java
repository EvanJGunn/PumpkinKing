package art.tidsear.pumpkininterface;

import art.tidsear.pumpkinking.PumpkinKingMod;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import art.tidsear.utility.Vector3f;

// This command is basically duplicated 2 more times for other spawn types,
// should I make it generic somehow? Maybe. But this is really a one off mod, so, going to pass.
public class CommandPKSpawn extends CommandBase {

    @Override
    public String getCommandName() {
        return "pkspawn";
    }

    @Override
    public String getCommandUsage(ICommandSender s) {
        return "/pkspawn <add|remove|reset> <x> <y> <z>";
    }

    @Override
    public void processCommand(ICommandSender s, String[] args) {
        try {
            float x,y,z;
            switch (args[0]) {
                case "add":
                    x = Float.parseFloat(args[1]);
                    y = Float.parseFloat(args[2]);
                    z = Float.parseFloat(args[3]);
                    PumpkinKingMod.pkGameMode.AddPKSpawn(new Vector3f(x, y, z));
                    return;
                case "remove":
                    x = Float.parseFloat(args[1]);
                    y = Float.parseFloat(args[2]);
                    z = Float.parseFloat(args[3]);
                    PumpkinKingMod.pkGameMode.RemovePKSpawn(new Vector3f(x, y, z));
                    return;
                case "reset":
                    PumpkinKingMod.pkGameMode.ResetPKSpawn();
                    return;
            }
            s.addChatMessage(new ChatComponentText("Please use either add, remove, or reset"));
        } catch (NumberFormatException e) {
            s.addChatMessage(new ChatComponentText("Error processing input: "+e.getMessage()));
        }
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }
}
