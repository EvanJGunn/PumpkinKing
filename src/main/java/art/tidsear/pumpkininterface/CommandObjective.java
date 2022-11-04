package art.tidsear.pumpkininterface;

import art.tidsear.pumpkinking.PumpkinKingMod;
import art.tidsear.utility.Vector3f;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;

public class CommandObjective extends CommandBase {

    @Override
    public String getCommandName() {
        return "objective";
    }

    @Override
    public String getCommandUsage(ICommandSender s) {
        return "/objective <add | remove | clearall | pair | printobjs | printpairs | assign> <Vector3f | Player> <PointsAward | Vector3f> <Description> <Expiration>";
    }

    @Override
    public void processCommand(ICommandSender s, String[] args) {
        switch(args[0]) {
            case "add":
                String joined = joinArgs(args, 5, args.length-2);
                PumpkinKingMod.pkGameMode.GetObjectiveManager().addObjective(new Vector3f(Integer.parseInt(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3])),Integer.parseInt(args[4]), joined, Long.parseLong(args[args.length-1]));
                break;
            case "remove":
                PumpkinKingMod.pkGameMode.GetObjectiveManager().deleteObjective(new Vector3f(Integer.parseInt(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3])));
                break;
            case "clearall":
                PumpkinKingMod.pkGameMode.GetObjectiveManager().clearAll();
                break;
            case "pair":
                PumpkinKingMod.pkGameMode.GetObjectiveManager().addPair(new Vector3f(Integer.parseInt(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3])), new Vector3f(Integer.parseInt(args[4]), Integer.parseInt(args[5]), Integer.parseInt(args[6])));
                break;
            case "printobjs":
                String[] msgs = PumpkinKingMod.pkGameMode.GetObjectiveManager().printObjs();
                for(int i = 0; i < msgs.length; i++) {
                    s.addChatMessage(new ChatComponentText(msgs[i]));
                }
                break;
            case "printpairs":
                String[] msgs1 = PumpkinKingMod.pkGameMode.GetObjectiveManager().printPairs();
                for(int i = 0; i < msgs1.length; i++) {
                    s.addChatMessage(new ChatComponentText(msgs1[i]));
                }
                break;
            case "printevents":
                String[] msgs2 = PumpkinKingMod.pkGameMode.GetObjectiveManager().printEvents();
                for(int i = 0; i < msgs2.length; i++) {
                    s.addChatMessage(new ChatComponentText(msgs2[i]));
                }
                break;
            case "printassigned":
                String[] msgs3 = PumpkinKingMod.pkGameMode.GetObjectiveManager().printAssigned();
                for(int i = 0; i < msgs3.length; i++) {
                    s.addChatMessage(new ChatComponentText(msgs3[i]));
                }
                break;
            case "printavail":
                String[] msgs4 = PumpkinKingMod.pkGameMode.GetObjectiveManager().printAvailObjs();
                for(int i = 0; i < msgs4.length; i++) {
                    s.addChatMessage(new ChatComponentText(msgs4[i]));
                }
                break;
            case "assign":
                PumpkinKingMod.pkGameMode.GetObjectiveManager().assignObjective(args[1],new Vector3f(Integer.parseInt(args[2]), Integer.parseInt(args[3]), Integer.parseInt(args[4])));
                break;
            case "complete":
                PumpkinKingMod.pkGameMode.GetObjectiveManager().activateObjective(args[1],new Vector3f(Integer.parseInt(args[2]), Integer.parseInt(args[3]), Integer.parseInt(args[4])));
                break;
        }
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    private String joinArgs(String[] args, int startIndex, int endIndex) {
        String joined = "";
        for(int i = startIndex; i <= endIndex; i++) {
            joined += args[i] + " ";
        }
        return joined;
    }
}
