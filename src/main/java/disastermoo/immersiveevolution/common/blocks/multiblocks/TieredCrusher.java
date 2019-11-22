package disastermoo.immersiveevolution.common.blocks.multiblocks;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.block.Block;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.Properties;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import blusunrize.immersiveengineering.api.IEProperties;
import blusunrize.immersiveengineering.api.Lib;
import blusunrize.immersiveengineering.api.MultiblockHandler;
import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.common.IEContent;
import blusunrize.immersiveengineering.common.blocks.BlockIEMultiblock;
import blusunrize.immersiveengineering.common.blocks.ItemBlockIEBase;
import blusunrize.immersiveengineering.common.blocks.TileEntityMultiblockPart;
import blusunrize.immersiveengineering.common.blocks.metal.BlockTypes_MetalDecoration0;
import blusunrize.immersiveengineering.common.blocks.metal.BlockTypes_MetalDecoration1;
import blusunrize.immersiveengineering.common.blocks.metal.BlockTypes_MetalMultiblock;
import blusunrize.immersiveengineering.common.util.Utils;
import disastermoo.immersiveevolution.EvolutionConfig;
import disastermoo.immersiveevolution.common.EvolutionContent;
import disastermoo.immersiveevolution.common.blocks.multiblocks.tileentities.TETieredCrusher;
import mcp.MethodsReturnNonnullByDefault;

@SuppressWarnings({"ConstantConditions", "WeakerAccess"})
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public abstract class TieredCrusher implements MultiblockHandler.IMultiblock
{
    protected static ItemStack[][][] DEFAULT_STRUCTURE = new ItemStack[3][3][5];

    static
    {
        for (int h = 0; h < 3; h++)
            for (int l = 0; l < 3; l++)
                for (int w = 0; w < 5; w++)
                {
                    if ((w == 0 && h == 2) || (w == 4 && h == 2) || (w == 4 && l == 2 && h > 0))
                    {
                        DEFAULT_STRUCTURE[h][l][w] = ItemStack.EMPTY;
                    }
                    else if (w == 0)
                    {
                        DEFAULT_STRUCTURE[h][l][w] = new ItemStack(IEContent.blockMetalDecoration0, 1, BlockTypes_MetalDecoration0.LIGHT_ENGINEERING.getMeta());
                    }
                    else if (w == 4)
                    {
                        if (h < 1)
                            DEFAULT_STRUCTURE[h][l][w] = new ItemStack(IEContent.blockMetalDecoration1, 1, BlockTypes_MetalDecoration1.STEEL_SCAFFOLDING_0.getMeta());
                        else if (h < 2 && l == 0)
                            DEFAULT_STRUCTURE[h][l][w] = new ItemStack(IEContent.blockMetalDecoration0, 1, BlockTypes_MetalDecoration0.RS_ENGINEERING.getMeta());
                        else if (h < 2 && l == 1)
                            DEFAULT_STRUCTURE[h][l][w] = new ItemStack(IEContent.blockMetalDecoration0, 1, BlockTypes_MetalDecoration0.LIGHT_ENGINEERING.getMeta());
                    }
                    else if (h == 0)
                    {
                        if (w == 2 && (l == 0 || l == 1))
                            DEFAULT_STRUCTURE[h][l][w] = new ItemStack(IEContent.blockMetalDecoration0, 1, BlockTypes_MetalDecoration0.LIGHT_ENGINEERING.getMeta());
                        else
                            DEFAULT_STRUCTURE[h][l][w] = new ItemStack(IEContent.blockMetalDecoration1, 1, BlockTypes_MetalDecoration1.STEEL_SCAFFOLDING_0.getMeta());
                    }
                    else if (h == 1)
                    {
                        if (l == 1 && w == 2)
                            DEFAULT_STRUCTURE[h][l][w] = new ItemStack(IEContent.blockMetalDecoration0, 1, BlockTypes_MetalDecoration0.LIGHT_ENGINEERING.getMeta());
                        else
                            DEFAULT_STRUCTURE[h][l][w] = new ItemStack(IEContent.blockMetalDecoration1, 1, BlockTypes_MetalDecoration1.STEEL_FENCE.getMeta());
                    }
                    else
                    {
                        DEFAULT_STRUCTURE[h][l][w] = new ItemStack(Blocks.PACKED_ICE);
                    }
                }
    }


    public static TieredCrusher getInstance(EnumTier tier)
    {
        switch (tier)
        {
            case MARK_I:
            default:
                return CrusherMk1.INSTANCE;
            case MARK_II:
                return CrusherMk2.INSTANCE;
        }
    }

    public static int getEnergyCapacity(EnumTier tier)
    {
        return getEnergyUsage(tier) * EvolutionConfig.CRUSHER.capacityMultiplier;
    }

    public static int getEnergyUsage(EnumTier tier)
    {
        switch (tier)
        {
            case MARK_I:
            default:
                return EvolutionConfig.CRUSHER.energyMk1;
            case MARK_II:
                return EvolutionConfig.CRUSHER.energyMk2;
            case MARK_III:
                return EvolutionConfig.CRUSHER.energyMk3;
            case MARK_IV:
                return EvolutionConfig.CRUSHER.energyMk4;
            case MARK_V:
                return EvolutionConfig.CRUSHER.energyMk5;
        }
    }

    @Override
    public boolean isBlockTrigger(IBlockState state)
    {
        ItemStack[][][] struct = getStructureManual();
        ItemStack stack = struct[1][1][1];
        Block block = ((ItemBlock) stack.getItem()).getBlock();
        return state.getBlock() == block && state.getBlock().getMetaFromState(state) == stack.getMetadata();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean overwriteBlockRender(ItemStack stack, int iterator)
    {
        return false;
    }

    @Override
    public float getManualScale()
    {
        return 12;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean canRenderFormedStructure()
    {
        return true;
    }

    @Override
    public boolean createStructure(World world, BlockPos pos, EnumFacing side, EntityPlayer player)
    {
        if (side.getAxis() == EnumFacing.Axis.Y)
            return false;

        BlockPos startPos = pos;
        side = side.getOpposite();
        ItemStack[][][] struct = getStructureManual();
        ItemStack engineer = struct[0][0][0];
        ItemStack scaffold = struct[0][0][4];
        Block engineerBlock = ((ItemBlock) engineer.getItem()).getBlock();
        Block scaffoldBlock = ((ItemBlock) scaffold.getItem()).getBlock();
        if (Utils.isBlockAt(world, startPos.add(0, -1, 0), scaffoldBlock, scaffold.getMetadata())
                && Utils.isBlockAt(world, startPos.offset(side, 2).add(0, -1, 0), engineerBlock, engineer.getMetadata()))
        {
            startPos = startPos.offset(side, 2);
            side = side.getOpposite();
        }
        boolean mirrored = false;
        boolean b = structureCheck(world, startPos, side, mirrored);
        if (!b)
        {
            mirrored = true;
            b = structureCheck(world, startPos, side, mirrored);
        }
        if (!b)
            return false;
        ItemStack hammer = player.getHeldItemMainhand().getItem().getToolClasses(player.getHeldItemMainhand()).contains(Lib.TOOL_HAMMER) ? player.getHeldItemMainhand() : player.getHeldItemOffhand();
        if (MultiblockHandler.fireMultiblockFormationEventPost(player, this, pos, hammer).isCanceled())
            return false;
        IBlockState state = EvolutionContent.CRUSHER.getStateFromMeta(getTier().getMeta());
        state = state.withProperty(IEProperties.FACING_HORIZONTAL, side);
        for (int l = 0; l < 3; l++)
        {
            for (int w = -2; w <= 2; w++)
            {
                for (int h = -1; h <= 1; h++)
                {
                    if ((w == -2 && h == 1) || (w == 2 && h == 1) || (w == 2 && l == 2 && h > -1))
                        continue;
                    int ww = mirrored ? -w : w;
                    BlockPos pos2 = startPos.offset(side, l).offset(side.rotateY(), ww).add(0, h, 0);

                    world.setBlockState(pos2, state);
                    TileEntity curr = world.getTileEntity(pos2);
                    if (curr instanceof TETieredCrusher)
                    {
                        TETieredCrusher tile = (TETieredCrusher) curr;
                        tile.formed = true;
                        tile.pos = (h + 1) * 15 + l * 5 + (w + 2);
                        tile.offset = new int[] {(side == EnumFacing.WEST ? -l + 1 : side == EnumFacing.EAST ? l - 1 : side == EnumFacing.NORTH ? ww : -ww), h, (side == EnumFacing.NORTH ? -l + 1 : side == EnumFacing.SOUTH ? l - 1 : side == EnumFacing.EAST ? ww : -ww)};
                        tile.mirrored = mirrored;
                        tile.markDirty();
                        world.addBlockEvent(pos2, EvolutionContent.CRUSHER, 255, 0);
                    }
                }
            }
        }
        return b;
    }

    protected boolean structureCheck(World world, BlockPos startPos, EnumFacing dir, boolean mirror)
    {
        ItemStack[][][] structure = getStructureManual();
        for (int l = 0; l < 3; l++)
        {
            for (int w = -2; w <= 2; w++)
            {
                for (int h = -1; h <= 1; h++)
                {
                    if (structure[h + 1][l][w + 2].isEmpty()) continue;
                    ItemStack stack = structure[h + 1][l][w + 2];
                    if (!(stack.getItem() instanceof ItemBlock)) continue;
                    Block block = ((ItemBlock) stack.getItem()).getBlock();
                    int ww = mirror ? -w : w;
                    BlockPos pos = startPos.offset(dir, l).offset(dir.rotateY(), ww).add(0, h, 0);
                    if (!Utils.isBlockAt(world, pos, block, stack.getMetadata()))
                        return false;
                }
            }
        }
        return true;
    }

    protected abstract EnumTier getTier();

    public static class CrusherMk1 extends TieredCrusher
    {
        private static final IngredientStack[] MATERIALS;
        private static final ItemStack[][][] STRUCTURE;

        static CrusherMk1 INSTANCE = new CrusherMk1();
        static ItemStack renderStack = ItemStack.EMPTY;

        static
        {
            MATERIALS = new IngredientStack[] {
                    new IngredientStack(new ItemStack(IEContent.blockMetalDecoration1, 10, BlockTypes_MetalDecoration1.STEEL_SCAFFOLDING_0.getMeta())),
                    new IngredientStack(new ItemStack(IEContent.blockMetalDecoration0, 1, BlockTypes_MetalDecoration0.RS_ENGINEERING.getMeta())),
                    new IngredientStack(new ItemStack(IEContent.blockMetalDecoration0, 10, BlockTypes_MetalDecoration0.LIGHT_ENGINEERING.getMeta())),
                    new IngredientStack(new ItemStack(IEContent.blockMetalDecoration1, 8, BlockTypes_MetalDecoration1.STEEL_FENCE.getMeta())),
                    new IngredientStack(new ItemStack(Blocks.PACKED_ICE, 9))};

            STRUCTURE = new ItemStack[3][3][5];
            for (int h = 0; h < 3; h++)
            {
                for (int l = 0; l < 3; l++)
                {
                    //noinspection ManualArrayCopy
                    for (int w = 0; w < 5; w++)
                    {
                        STRUCTURE[h][l][w] = DEFAULT_STRUCTURE[h][l][w];
                    }
                }
            }
        }

        @Override
        public String getUniqueName()
        {
            return "immersiveevolution:CrusherMk1";
        }

        @Override
        public ItemStack[][][] getStructureManual()
        {
            return STRUCTURE;
        }

        @Override
        public IngredientStack[] getTotalMaterials()
        {
            return MATERIALS;
        }

        @Override
        @SideOnly(Side.CLIENT)
        public void renderFormedStructure()
        {
            if (renderStack.isEmpty())
                renderStack = new ItemStack(IEContent.blockMetalMultiblock, 1, BlockTypes_MetalMultiblock.CRUSHER.getMeta());
            GlStateManager.translate(1.5, 1.5, 2.5);
            GlStateManager.rotate(-45, 0, 1, 0);
            GlStateManager.rotate(-20, 1, 0, 0);
            GlStateManager.scale(5.5, 5.5, 5.5);

            GlStateManager.disableCull();
            ClientUtils.mc().getRenderItem().renderItem(renderStack, ItemCameraTransforms.TransformType.GUI);
            GlStateManager.enableCull();
        }

        @Override
        protected EnumTier getTier()
        {
            return EnumTier.MARK_I;
        }
    }

    public static class CrusherMk2 extends TieredCrusher
    {
        private static final IngredientStack[] MATERIALS;
        private static final ItemStack[][][] STRUCTURE;

        static CrusherMk2 INSTANCE = new CrusherMk2();
        static ItemStack renderStack = ItemStack.EMPTY;

        static
        {
            MATERIALS = new IngredientStack[] {
                    new IngredientStack(new ItemStack(IEContent.blockMetalDecoration1, 10, BlockTypes_MetalDecoration1.ALUMINUM_SCAFFOLDING_0.getMeta())),
                    new IngredientStack(new ItemStack(IEContent.blockMetalDecoration0, 1, BlockTypes_MetalDecoration0.RS_ENGINEERING.getMeta())),
                    new IngredientStack(new ItemStack(IEContent.blockMetalDecoration0, 10, BlockTypes_MetalDecoration0.LIGHT_ENGINEERING.getMeta())),
                    new IngredientStack(new ItemStack(IEContent.blockMetalDecoration1, 8, BlockTypes_MetalDecoration1.ALUMINUM_FENCE.getMeta())),
                    new IngredientStack(new ItemStack(Blocks.PACKED_ICE, 9))};

            STRUCTURE = new ItemStack[3][3][5];
            for (int h = 0; h < 3; h++)
            {
                for (int l = 0; l < 3; l++)
                {
                    for (int w = 0; w < 5; w++)
                    {
                        STRUCTURE[h][l][w] = DEFAULT_STRUCTURE[h][l][w];
                        if (STRUCTURE[h][l][w].isItemEqual(new ItemStack(IEContent.blockMetalDecoration1, 1, BlockTypes_MetalDecoration1.STEEL_SCAFFOLDING_0.getMeta())))
                        {
                            STRUCTURE[h][l][w] = new ItemStack(IEContent.blockMetalDecoration1, 1, BlockTypes_MetalDecoration1.ALUMINUM_SCAFFOLDING_0.getMeta());
                        }
                        else if (STRUCTURE[h][l][w].isItemEqual(new ItemStack(IEContent.blockMetalDecoration1, 1, BlockTypes_MetalDecoration1.STEEL_FENCE.getMeta())))
                        {
                            STRUCTURE[h][l][w] = new ItemStack(IEContent.blockMetalDecoration1, 1, BlockTypes_MetalDecoration1.ALUMINUM_FENCE.getMeta());
                        }
                    }
                }
            }
        }

        @Override
        public String getUniqueName()
        {
            return "immersiveevolution:CrusherMk2";
        }

        @Override
        public ItemStack[][][] getStructureManual()
        {
            return STRUCTURE;
        }

        @Override
        public IngredientStack[] getTotalMaterials()
        {
            return MATERIALS;
        }

        @Override
        @SideOnly(Side.CLIENT)
        public void renderFormedStructure()
        {
            if (renderStack.isEmpty())
                renderStack = new ItemStack(IEContent.blockMetalMultiblock, 1, BlockTypes_MetalMultiblock.CRUSHER.getMeta());
            GlStateManager.translate(1.5, 1.5, 2.5);
            GlStateManager.rotate(-45, 0, 1, 0);
            GlStateManager.rotate(-20, 1, 0, 0);
            GlStateManager.scale(5.5, 5.5, 5.5);

            GlStateManager.disableCull();
            ClientUtils.mc().getRenderItem().renderItem(renderStack, ItemCameraTransforms.TransformType.GUI);
            GlStateManager.enableCull();
        }

        @Override
        protected EnumTier getTier()
        {
            return EnumTier.MARK_II;
        }
    }

    public static class CrusherBlock extends BlockIEMultiblock<EnumTier>
    {

        public CrusherBlock()
        {
            super("tiered_multiblock_crusher", Material.IRON, PropertyEnum.create("type", EnumTier.class), ItemBlockIEBase.class, IEProperties.DYNAMICRENDER, IEProperties.BOOLEANS[0], Properties.AnimationProperty, IEProperties.OBJ_TEXTURE_REMAP);
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
            return "mark" + (meta + 1);
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
        public TileEntity createBasicTE(World world, EnumTier type)
        {
            return new TETieredCrusher(type);
        }

        @SuppressWarnings("deprecation")
        @Override
        public boolean isSideSolid(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side)
        {
            TileEntity te = world.getTileEntity(pos);
            if (te instanceof TileEntityMultiblockPart)
            {
                TileEntityMultiblockPart tile = (TileEntityMultiblockPart) te;
                return tile.pos % 5 == 0 || tile.pos == 2 || tile.pos == 9 || tile.pos == 19 && side.getOpposite() == tile.facing;
            }
            return super.isSideSolid(state, world, pos, side);
        }
    }
}
