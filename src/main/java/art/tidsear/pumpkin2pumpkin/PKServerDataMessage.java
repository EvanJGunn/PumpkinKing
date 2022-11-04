package art.tidsear.pumpkin2pumpkin;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.PacketBuffer;

import java.io.*;
import java.util.Arrays;

public class PKServerDataMessage implements IMessage {

    public LocalData localData;

    public PKServerDataMessage() {
    }

    public PKServerDataMessage(LocalData localData) {
        this.localData = localData;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        // There is a magic byte of some sort on the front of the array
        // I don't know how to or care enough to find out why, let's just nuke it
        // future me says this worked out ok, so yeah
        if (buf.array().length == 0) {
            return;
        }
        byte[] fixedArray = new byte[buf.array().length-1];
        for (int i = 0; i < buf.array().length; i++) {
            if (i == 0) {
                continue;
            }
            fixedArray[i-1] = buf.array()[i];
        }

        try {
            ByteArrayInputStream byteInStr = new ByteArrayInputStream(fixedArray);
            ObjectInputStream objInStr = new ObjectInputStream(byteInStr);

            this.localData = (LocalData) objInStr.readObject();
            byteInStr.close();
            objInStr.close();
        } catch (IOException e) {
            this.localData = new LocalData();
            System.out.println("IOException Converting Buf");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("ISSUE CONVERTING CLASS NOT FOUND");
        } catch (Exception e) {
            // etc
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        try {
            ByteArrayOutputStream byteOutStr = new ByteArrayOutputStream();
            ObjectOutputStream objOutStr = new ObjectOutputStream(byteOutStr);
            objOutStr.writeObject(this.localData);
            objOutStr.flush();

            buf.writeBytes(byteOutStr.toByteArray());
            byteOutStr.close();
            objOutStr.close();
        } catch (IOException e) {
            System.out.println("ISSUE CONVERTING PKServer DATA to bytes "+e.getMessage());
        }
    }
}

