package noname.blockbuster.network.server;

import net.minecraft.entity.player.EntityPlayerMP;
import noname.blockbuster.network.Dispatcher;
import noname.blockbuster.network.common.director.PacketDirectorCast;
import noname.blockbuster.network.common.director.PacketDirectorReset;
import noname.blockbuster.tileentity.TileEntityDirector;

public class ServerHandlerDirectorReset extends ServerMessageHandler<PacketDirectorReset>
{
    @Override
    public void run(EntityPlayerMP player, PacketDirectorReset message)
    {
        TileEntityDirector tile = (TileEntityDirector) player.worldObj.getTileEntity(message.pos);

        tile.reset();
        Dispatcher.getInstance().sendTo(new PacketDirectorCast(message.pos, tile.actors, tile.cameras), player);
    }
}