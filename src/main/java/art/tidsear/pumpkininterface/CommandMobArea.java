package art.tidsear.pumpkininterface;

import art.tidsear.pumpkinking.PumpkinKingMod;
import art.tidsear.utility.Vector3f;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;

public class CommandMobArea extends CommandBase {

    @Override
    public String getCommandName() {
        return "mobarea";
    }

    @Override
    public String getCommandUsage(ICommandSender s) {
        return "/mobarea <add | remove | removeall | addpos | print> <name> <vector3f | mobtype> <killaward> <maxpop> <replenish-rate>";
    }

    @Override
    public void processCommand(ICommandSender s, String[] args) {
        switch(args[0]) {
            case "add":
                PumpkinKingMod.pkGameMode.GetMobAreaManager().registerMobArea(args[1], args[2], Integer.parseInt(args[3]),Integer.parseInt(args[4]),Integer.parseInt(args[5]));
                break;
            case "remove":
                PumpkinKingMod.pkGameMode.GetMobAreaManager().deleteMobArea(args[1]);
                break;
            case "removeall":
                PumpkinKingMod.pkGameMode.GetMobAreaManager().clearMobAreas();
                break;
            case "addpos":
                PumpkinKingMod.pkGameMode.GetMobAreaManager().addPos(args[1], new Vector3f(Integer.parseInt(args[2]),Integer.parseInt(args[3]),Integer.parseInt(args[4])));
                break;
            case "print":
                String[] msgs = PumpkinKingMod.pkGameMode.GetMobAreaManager().printAreas();
                for(int i = 0; i < msgs.length; i++) {
                    s.addChatMessage(new ChatComponentText(msgs[i]));
                }
                break;
        }
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }
}
