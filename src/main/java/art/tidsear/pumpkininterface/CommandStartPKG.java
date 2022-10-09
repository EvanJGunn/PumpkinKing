package art.tidsear.pumpkininterface;

import art.tidsear.pumpkinking.PumpkinKingMod;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.IChatComponent;

import java.util.Iterator;
import java.util.List;

public class CommandStartPKG extends CommandBase {

    @Override
    public String getCommandName() {
        return "pkgstart";
    }

    @Override
    public String getCommandUsage(ICommandSender s) {
        return "/pkgstart";
    }

    @Override
    public void processCommand(ICommandSender s, String[] args) {
        System.out.println("Command Start PK Gamemode");

        PumpkinKingMod.pkGameMode.StartUp();
        s.addChatMessage(new ChatComponentText("Ready or not..."));
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }
}
