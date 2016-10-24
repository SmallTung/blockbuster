package mchorse.blockbuster.common.block;

import mchorse.blockbuster.common.item.ItemRegister;
import mchorse.blockbuster.common.tileentity.TileEntityDirector;
import mchorse.blockbuster.network.Dispatcher;
import mchorse.blockbuster.network.common.director.PacketDirectorCast;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

/**
 * Descendant of abstract director block
 *
 * This block is the one that responsible for machinimas creation.
 */
public class BlockDirector extends AbstractBlockDirector
{
    public BlockDirector()
    {
        super();
        this.setRegistryName("director");
        this.setUnlocalizedName("blockbuster.director");
    }

    @Override
    protected boolean handleItem(ItemStack item, World world, BlockPos pos, EntityPlayer player)
    {
        return this.handlePlaybackItem(item, world, pos, player) || this.handleRegisterItem(item, world, pos, player);
    }

    private boolean handleRegisterItem(ItemStack item, World world, BlockPos pos, EntityPlayer player)
    {
        if (!(item.getItem() instanceof ItemRegister))
        {
            return false;
        }

        if (world.isRemote)
        {
            return true;
        }

        ((ItemRegister) item.getItem()).registerStack(item, pos);
        player.addChatMessage(new TextComponentTranslation("blockbuster.director.attached_device"));

        return true;
    }

    @Override
    protected void displayCast(EntityPlayer player, World worldIn, BlockPos pos)
    {
        TileEntityDirector tile = (TileEntityDirector) worldIn.getTileEntity(pos);
        Dispatcher.sendTo(new PacketDirectorCast(tile.getPos(), tile.replays), (EntityPlayerMP) player);
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta)
    {
        return new TileEntityDirector();
    }
}