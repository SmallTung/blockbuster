package mchorse.blockbuster.commands.record;

import java.util.List;

import mchorse.blockbuster.commands.CommandRecord;
import mchorse.blockbuster.recording.Utils;
import mchorse.blockbuster.recording.actions.Action;
import mchorse.blockbuster.recording.data.Frame;
import mchorse.blockbuster.recording.data.Record;
import mchorse.blockbuster.utils.L10n;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

/**
 * Command /record origin
 *
 * This command is responsible for changing the origin of the player 
 * recording.
 */
public class SubCommandRecordOrigin extends SubCommandRecordBase
{
    @Override
    public int getRequiredArgs()
    {
        return 1;
    }

    @Override
    public String getCommandName()
    {
        return "origin";
    }

    @Override
    public String getCommandUsage(ICommandSender sender)
    {
        return "blockbuster.commands.record.origin";
    }

    @Override
    public void executeCommand(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        String filename = args[0];

        EntityPlayer player = getCommandSenderAsPlayer(sender);
        Record record = CommandRecord.getRecord(filename);

        double x = player.posX;
        double y = player.posY;
        double z = player.posZ;

        double rotation = args.length >= 2 ? CommandBase.parseDouble(args[1]) : 0;

        double firstX = 0;
        double firstY = 0;
        double firstZ = 0;
        int i = 0;

        for (Frame frame : record.frames)
        {
            if (i == 0)
            {
                firstX = frame.x;
                firstY = frame.y;
                firstZ = frame.z;

                if (args.length >= 5)
                {
                    x = CommandBase.parseDouble(firstX, args[2], false);
                    y = CommandBase.parseDouble(firstY, args[3], false);
                    z = CommandBase.parseDouble(firstZ, args[4], false);
                }
            }

            double frameX = frame.x - firstX;
            double frameY = frame.y - firstY;
            double frameZ = frame.z - firstZ;

            if (rotation != 0)
            {
                float cos = (float) Math.cos(rotation / 180 * Math.PI);
                float sin = (float) Math.sin(rotation / 180 * Math.PI);

                double xx = frameX * cos - frameZ * sin;
                double zz = frameX * sin + frameZ * cos;

                frameX = xx;
                frameZ = zz;

                frame.yaw += rotation;
                frame.yawHead += rotation;
            }

            frame.x = x + frameX;
            frame.y = y + frameY;
            frame.z = z + frameZ;

            i++;
        }

        for (List<Action> actions : record.actions)
        {
            if (actions == null || actions.isEmpty())
            {
                continue;
            }

            for (Action action : actions)
            {
                action.changeOrigin(rotation, x, y, z, firstX, firstY, firstZ);
            }
        }

        record.dirty = true;

        Utils.unloadRecord(record);
        L10n.success(sender, "record.changed_origin", args[0], firstX, firstY, firstZ, x, y, z);
    }
}