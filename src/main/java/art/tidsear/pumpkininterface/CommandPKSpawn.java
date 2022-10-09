package art.tidsear.pumpkininterface;

import art.tidsear.pumpkinking.PumpkinKingMod;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import org.lwjgl.util.vector.Vector3f;

// This command is basically duplicated 2 more times for other spawn types,
// should I make it generic somehow? Maybe. But this is really a one off mod, so, going to pass.
public class CommandPKSpawn extends CommandBase {

    @Override
    public String getCommandName() {
        return "pkspawn";
    }

    @Override
    public String getCommandUsage(ICommandSender s) {
        return "/pkspawn <add|remove> <x> <y> <z>";
    }

    @Override
    public void processCommand(ICommandSender s, String[] args) {
        try {
            float x = Float.parseFloat(args[1]);
            float y = Float.parseFloat(args[2]);
            float z = Float.parseFloat(args[3]);

            switch (args[0]) {
                case "add":
                    PumpkinKingMod.pkGameMode.AddPKSpawn(new Vector3f(x, y, z));
                    return;
                case "remove":
                    PumpkinKingMod.pkGameMode.RemovePKSpawn(new Vector3f(x, y, z));
                    return;
            }
            s.addChatMessage(new ChatComponentText("Please use either add or remove"));
        } catch (NumberFormatException e) {
            s.addChatMessage(new ChatComponentText("Error processing input: "+e.getMessage()));
        }
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }
}
