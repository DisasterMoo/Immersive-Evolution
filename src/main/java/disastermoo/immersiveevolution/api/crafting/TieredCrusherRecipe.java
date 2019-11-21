package disastermoo.immersiveevolution.api.crafting;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nullable;

import com.google.common.collect.Lists;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraftforge.oredict.OreDictionary;

import blusunrize.immersiveengineering.api.ApiUtils;
import blusunrize.immersiveengineering.api.IEApi;
import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import blusunrize.immersiveengineering.api.crafting.MultiblockRecipe;
import blusunrize.immersiveengineering.common.util.ListUtils;
import blusunrize.immersiveengineering.common.util.Utils;
import disastermoo.immersiveevolution.EvolutionConfig;
import disastermoo.immersiveevolution.common.blocks.multiblocks.TieredCrusher;

@SuppressWarnings("WeakerAccess")
public class TieredCrusherRecipe extends MultiblockRecipe
{
    // How many ticks are needed to crush ores of the same tier
    public static ArrayList<TieredCrusherRecipe> RECIPE_LIST = new ArrayList<>();

    public static TieredCrusherRecipe addRecipe(ItemStack output, Object input, int requiredTier)
    {
        TieredCrusherRecipe r = new TieredCrusherRecipe(output, input, requiredTier);
        if (r.input != null && !r.output.isEmpty())
            RECIPE_LIST.add(r);
        return r;
    }

    public static TieredCrusherRecipe findRecipe(ItemStack input, int machineTier)
    {
        for (TieredCrusherRecipe recipe : RECIPE_LIST)
        {
            if (recipe.input.matchesItemStack(input) && recipe.requiredTier <= machineTier)
            {
                TieredCrusherRecipe recipeClone = new TieredCrusherRecipe(recipe.output, recipe.input, recipe.requiredTier);
                recipeClone.currentTier = machineTier;
                return recipeClone;
            }
        }

        return null;
    }

    public static List<TieredCrusherRecipe> removeRecipesForOutput(ItemStack stack)
    {
        List<TieredCrusherRecipe> list = new ArrayList<>();
        Iterator<TieredCrusherRecipe> it = RECIPE_LIST.iterator();
        while (it.hasNext())
        {
            TieredCrusherRecipe ir = it.next();
            if (OreDictionary.itemMatches(ir.output, stack, true))
            {
                list.add(ir);
                it.remove();
            }
        }
        return list;
    }

    public static List<TieredCrusherRecipe> removeRecipesForInput(ItemStack stack)
    {
        List<TieredCrusherRecipe> list = new ArrayList<>();
        Iterator<TieredCrusherRecipe> it = RECIPE_LIST.iterator();
        while (it.hasNext())
        {
            TieredCrusherRecipe ir = it.next();
            if (ir.input.matchesItemStackIgnoringSize(stack))
            {
                list.add(ir);
                it.remove();
            }
        }
        return list;
    }

    @Nullable
    public static TieredCrusherRecipe loadFromNBT(NBTTagCompound nbt)
    {
        IngredientStack input = IngredientStack.readFromNBT(nbt.getCompoundTag("input"));
        int currentTier = nbt.getInteger("currentTier");
        for (TieredCrusherRecipe recipe : RECIPE_LIST)
        {
            if (recipe.input.equals(input) && recipe.requiredTier <= currentTier)
            {
                TieredCrusherRecipe recipeClone = new TieredCrusherRecipe(recipe.output, recipe.input, recipe.requiredTier);
                recipeClone.currentTier = currentTier;
                return recipeClone;
            }
        }
        return null;
    }

    private final String oreInputString;
    private final IngredientStack input;
    private final ItemStack output;
    private ItemStack[] secondaryOutput;
    private float[] secondaryChance;
    private int requiredTier;
    private int currentTier;

    public TieredCrusherRecipe(ItemStack output, Object input, int requiredTier)
    {
        this.output = output;
        this.input = ApiUtils.createIngredientStack(input);
        this.oreInputString = input instanceof String ? (String) input : null;

        this.inputList = Lists.newArrayList(this.input);
        this.outputList = ListUtils.fromItem(this.output);

        this.requiredTier = requiredTier;
        this.currentTier = requiredTier;
    }

    @Override
    public NonNullList<ItemStack> getActualItemOutputs(TileEntity tile)
    {
        NonNullList<ItemStack> list = NonNullList.create();
        list.add(output);
        if (secondaryOutput != null && secondaryChance != null)
            for (int i = 0; i < secondaryOutput.length; i++)
                if (Utils.RAND.nextFloat() < secondaryChance[i])
                    list.add(secondaryOutput[i]);
        return list;
    }

    @Override
    public int getMultipleProcessTicks()
    {
        return 4;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt)
    {
        nbt.setTag("input", input.writeToNBT(new NBTTagCompound()));
        nbt.setInteger("currentTier", currentTier);
        return nbt;
    }

    public TieredCrusherRecipe addToSecondaryOutput(Object... outputs)
    {
        if (outputs.length % 2 != 0)
            return this;
        ArrayList<ItemStack> newSecondaryOutput = new ArrayList<>();
        ArrayList<Float> newSecondaryChance = new ArrayList<>();
        if (secondaryOutput != null)
            for (int i = 0; i < secondaryOutput.length; i++)
            {
                newSecondaryOutput.add(secondaryOutput[i]);
                newSecondaryChance.add(secondaryChance[i]);
            }
        for (int i = 0; i < (outputs.length / 2); i++)
            if (outputs[i * 2] != null)
            {
                Object o = ApiUtils.convertToValidRecipeInput(outputs[i * 2]);
                //noinspection unchecked
                ItemStack ss = o instanceof ItemStack ? (ItemStack) o : o instanceof List ? IEApi.getPreferredStackbyMod((List<ItemStack>) o) : ItemStack.EMPTY;
                if (!ss.isEmpty())
                {
                    newSecondaryOutput.add(ss);
                    newSecondaryChance.add((Float) outputs[i * 2 + 1]);
                }
            }
        secondaryOutput = newSecondaryOutput.toArray(new ItemStack[0]);
        secondaryChance = new float[newSecondaryChance.size()];
        int i = 0;
        for (Float f : newSecondaryChance)
            secondaryChance[i++] = f;

        this.outputList = ListUtils.fromItems(this.secondaryOutput);
        if (this.outputList.isEmpty())
            this.outputList.add(this.output);
        else
            this.outputList.add(0, this.output);

        return this;
    }

    @Override
    public int getTotalProcessTime()
    {
        double tierModifier = Math.pow(0.5f, (currentTier - requiredTier));
        return (int) Math.max(1, EvolutionConfig.CRUSHER.ticks * tierModifier);
    }

    @Override
    public int getTotalProcessEnergy()
    {
        return getTotalProcessTime() * TieredCrusher.getEnergyUsage(currentTier);
    }
}
