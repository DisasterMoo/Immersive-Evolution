package disastermoo.immersiveevolution.client.render;

import org.lwjgl.opengl.GL11;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import blusunrize.immersiveengineering.api.IEProperties;
import blusunrize.immersiveengineering.client.ClientUtils;
import disastermoo.immersiveevolution.common.EvolutionContent;
import disastermoo.immersiveevolution.common.blocks.multiblocks.tileentities.TECrusherTiered;

@SideOnly(Side.CLIENT)
public class TESRCrusherTiered extends TileEntitySpecialRenderer<TECrusherTiered>
{
    @Override
    public void render(TECrusherTiered te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
    {
        if (!te.formed || te.isDummy() || !te.getWorld().isBlockLoaded(te.getPos(), false))
            return;

        final BlockRendererDispatcher blockRenderer = Minecraft.getMinecraft().getBlockRendererDispatcher();
        BlockPos blockPos = te.getPos();
        IBlockState state = getWorld().getBlockState(blockPos);
        if (state.getBlock() != EvolutionContent.MULTIBLOCKS)
            return;
        //noinspection deprecation
        state = state.getBlock().getActualState(state, getWorld(), blockPos);
        state = state.withProperty(IEProperties.DYNAMICRENDER, true);
        IBakedModel model = blockRenderer.getBlockModelShapes().getModelForState(state);

        boolean b = te.shouldRenderAsActive();
        float angle = te.animation_barrelRotation + (b ? 18 * partialTicks : 0);

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder worldRenderer = tessellator.getBuffer();

        ClientUtils.bindAtlas();
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);
        GlStateManager.translate(.5, 1.5, .5);


        RenderHelper.disableStandardItemLighting();
        GlStateManager.blendFunc(770, 771);
        GlStateManager.enableBlend();
        GlStateManager.disableCull();
        if (Minecraft.isAmbientOcclusionEnabled())
            GlStateManager.shadeModel(7425);
        else
            GlStateManager.shadeModel(7424);
        GlStateManager.translate(te.facing.getXOffset() * .5, 0, te.facing.getZOffset() * .5);
        GlStateManager.rotate(angle, -te.facing.getZOffset(), 0, te.facing.getXOffset());
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
        worldRenderer.setTranslation(-.5 - blockPos.getX(), -.5 - blockPos.getY(), -.5 - blockPos.getZ());
        worldRenderer.color(255, 255, 255, 255);
        blockRenderer.getBlockModelRenderer().renderModel(te.getWorld(), model, state, blockPos, worldRenderer, true);
        worldRenderer.setTranslation(0.0D, 0.0D, 0.0D);
        tessellator.draw();
        GlStateManager.rotate(-angle, -te.facing.getZOffset(), 0, te.facing.getXOffset());
        GlStateManager.translate(te.facing.getXOffset() * -1, 0, te.facing.getZOffset() * -1);
        GlStateManager.rotate(-angle, -te.facing.getZOffset(), 0, te.facing.getXOffset());
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
        worldRenderer.setTranslation(-.5 - blockPos.getX(), -.5 - blockPos.getY(), -.5 - blockPos.getZ());
        worldRenderer.color(255, 255, 255, 255);
        blockRenderer.getBlockModelRenderer().renderModel(te.getWorld(), model, state, blockPos, worldRenderer, true);
        worldRenderer.setTranslation(0.0D, 0.0D, 0.0D);
        tessellator.draw();
        GlStateManager.rotate(angle, -te.facing.getZOffset(), 0, te.facing.getXOffset());

        RenderHelper.enableStandardItemLighting();

        GlStateManager.popMatrix();
    }

}
