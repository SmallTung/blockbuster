package mchorse.blockbuster.network.server.recording;

import mchorse.blockbuster.common.CommonProxy;
import mchorse.blockbuster.network.Dispatcher;
import mchorse.blockbuster.network.common.recording.PacketActions;
import mchorse.blockbuster.network.common.recording.PacketRequestActions;
import mchorse.blockbuster.network.server.ServerMessageHandler;
import mchorse.blockbuster.recording.data.Record;
import net.minecraft.entity.player.EntityPlayerMP;

public class ServerHandlerRequestActions extends ServerMessageHandler<PacketRequestActions>
{
    @Override
    public void run(EntityPlayerMP player, PacketRequestActions message)
    {
        Record record = CommonProxy.manager.records.get(message.filename);

        if (record != null)
        {
            Dispatcher.sendTo(new PacketActions(message.filename, record.actions), player);
        }
    }
}