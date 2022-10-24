package art.tidsear.pumpkininterface;

import art.tidsear.pumpkinking.PumpkinKingMod;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;

public class CommandPKGConfig extends CommandBase {

    @Override
    public String getCommandName() {
        return "pkgconfig";
    }

    @Override
    public String getCommandUsage(ICommandSender s) {
        return "/pkgconfig <reset|set|add|print> <configKey> <value?>";
    }

    @Override
    public void processCommand(ICommandSender s, String[] args) {
        System.out.println("Command Start PK Gamemode");

        if(args.length < 1) return;

        switch (args[0]) {
            case "reset":
                PumpkinKingMod.pkGameMode.getConfig().resetArrayList(args[1]);
                break;
            case "set":
                PumpkinKingMod.pkGameMode.getConfig().setValue(args[1],args[2]);
                break;
            case "add":
                PumpkinKingMod.pkGameMode.getConfig().addValue(args[1],args[2]);
                break;
            case "print":
                String[] prints = PumpkinKingMod.pkGameMode.getConfig().toStrings();
                for (int i = 0; i < prints.length; i++) {
                    s.addChatMessage(new ChatComponentText(prints[i]));
                }
                break;
        }
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }
}
