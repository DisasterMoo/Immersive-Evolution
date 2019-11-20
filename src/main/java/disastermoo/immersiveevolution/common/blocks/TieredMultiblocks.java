package disastermoo.immersiveevolution.common.blocks;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.Properties;

import blusunrize.immersiveengineering.api.IEProperties;
import blusunrize.immersiveengineering.common.blocks.BlockIEMultiblock;
import blusunrize.immersiveengineering.common.blocks.ItemBlockIEBase;
import blusunrize.immersiveengineering.common.blocks.TileEntityMultiblockPart;
import blusunrize.immersiveengineering.common.blocks.metal.BlockTypes_MetalMultiblock;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityMultiblockMetal;
import disastermoo.immersiveevolution.common.blocks.multiblocks.tileentities.TECrusherTiered;
import mcp.MethodsReturnNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class TieredMultiblocks extends BlockIEMultiblock<BlockTypes_MetalMultiblock>
{

    public TieredMultiblocks()
    {
        super("tiered_multiblocks", Material.IRON, PropertyEnum.create("type", BlockTypes_MetalMultiblock.class), ItemBlockIEBase.class, IEProperties.DYNAMICRENDER, IEProperties.BOOLEANS[0], Properties.AnimationProperty, IEProperties.OBJ_TEXTURE_REMAP);
        this.setHardness(3.0F);
        this.setResistance(15.0F);
        this.setMetaBlockLayer(BlockTypes_MetalMultiblock.TANK.getMeta(), BlockRenderLayer.CUTOUT);
        this.setMetaBlockLayer(BlockTypes_MetalMultiblock.DIESEL_GENERATOR.getMeta(), BlockRenderLayer.CUTOUT);
        this.setMetaBlockLayer(BlockTypes_MetalMultiblock.BOTTLING_MACHINE.getMeta(), BlockRenderLayer.SOLID, BlockRenderLayer.TRANSLUCENT);
        this.setAllNotNormalBlock();
        this.lightOpacity = 0;
    }

    @Override
    public boolean useCustomStateMapper()
    {
        return true;
    }

    @SuppressWarnings("NullableProblems")
    @Override
    @Nullable
    public String getCustomStateMapping(int meta, boolean itemBlock)
    {
        return BlockTypes_MetalMultiblock.values()[meta].needsCustomState() ? BlockTypes_MetalMultiblock.values()[meta].getCustomState() : null;
    }

    @Override
    public EnumPushReaction getPushReaction(IBlockState state)
    {
        return EnumPushReaction.BLOCK;
    }

    @Override
    public boolean allowHammerHarvest(IBlockState state)
    {
        return true;
    }

    @Override
    public TileEntity createBasicTE(World world, BlockTypes_MetalMultiblock type)
    {
        switch (type)
        {
            case CRUSHER:
                return new TECrusherTiered();
            default:
                return null;
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isSideSolid(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side)
    {
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof TileEntityMultiblockPart)
        {
            TileEntityMultiblockPart tile = (TileEntityMultiblockPart) te;
            if (tile instanceof TileEntityMultiblockMetal && ((TileEntityMultiblockMetal) tile).isRedstonePos())
            {
                return true;
            }

            if (te instanceof TECrusherTiered)
            {
                return tile.pos % 5 == 0 || tile.pos == 2 || tile.pos == 9 || tile.pos == 19 && side.getOpposite() == tile.facing;
            }
        }
        return super.isSideSolid(state, world, pos, side);
    }
}
