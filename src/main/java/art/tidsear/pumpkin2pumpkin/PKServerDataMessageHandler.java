package art.tidsear.pumpkin2pumpkin;

import art.tidsear.pumpkinking.PumpkinKingMod;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;

public class PKServerDataMessageHandler implements IMessageHandler<PKServerDataMessage, IMessage> {
    @Override
    public IMessage onMessage(PKServerDataMessage message, MessageContext ctx) {
        if (ctx.side == Side.CLIENT) {
            PumpkinKingMod.myData = message.localData;
        }
        return null;
    }
}
