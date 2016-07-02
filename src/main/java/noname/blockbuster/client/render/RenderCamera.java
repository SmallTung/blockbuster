package noname.blockbuster.client.render;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import noname.blockbuster.Blockbuster;
import noname.blockbuster.client.model.ModelCamera;
import noname.blockbuster.entity.EntityCamera;

/**
 * Camera renderer
 *
 * Renders camera and more
 */
@SideOnly(Side.CLIENT)
public class RenderCamera extends RenderLiving
{
    private static final ResourceLocation resource = new ResourceLocation(Blockbuster.MODID, "textures/entity/camera.png");

    /**
     * This is stupid. Why I should define this constructor if it's already
     * defined in RenderLiving?
     */
    public RenderCamera(RenderManager rendermanagerIn, ModelBase modelbaseIn, float shadowsizeIn)
    {
        super(rendermanagerIn, modelbaseIn, shadowsizeIn);
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity entity)
    {
        return resource;
    }

    @Override
    protected boolean canRenderName(EntityLiving entity)
    {
        return super.canRenderName(entity) && ((EntityCamera) entity).renderName;
    }

    /**
     * Render the camera only if it's not recording, basically hide cameras
     * when the director block is playbacks its actors
     */
    @Override
    public void doRender(EntityLiving entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
        EntityCamera camera = (EntityCamera) entity;

        if (!camera.isRecording)
        {
            super.doRender(entity, x, y, z, entityYaw, partialTicks);
        }
    }

    /**
     * Renderer factory
     */
    public static class CameraFactory implements IRenderFactory
    {
        @Override
        public Render createRenderFor(RenderManager manager)
        {
            return new RenderCamera(manager, new ModelCamera(), 0.4F);
        }
    }
}
