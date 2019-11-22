package disastermoo.immersiveevolution.common;

import net.minecraft.item.ItemStack;

import blusunrize.immersiveengineering.api.crafting.CrusherRecipe;
import disastermoo.immersiveevolution.api.crafting.TieredCrusherRecipe;
import disastermoo.immersiveevolution.common.blocks.multiblocks.EnumTier;

@SuppressWarnings("WeakerAccess")
public class EvolutionRecipes
{

    public static void init()
    {
        initCrusherRecipes();
    }

    private static TieredCrusherRecipe addCrusherRecipe(ItemStack output, Object input, EnumTier tier, Object... secondary)
    {
        TieredCrusherRecipe r = TieredCrusherRecipe.addRecipe(output, input, tier);
        if (secondary != null && secondary.length > 0)
            r.addToSecondaryOutput(secondary);
        return r;
    }


    private static void initCrusherRecipes()
    {
        for (CrusherRecipe recipe : CrusherRecipe.recipeList)
        {
            TieredCrusherRecipe r = TieredCrusherRecipe.addRecipe(recipe.output, recipe.input, EnumTier.MARK_I);
            if (recipe.secondaryOutput != null)
            {
                for (int i = 0; i < recipe.secondaryOutput.length; i++)
                {
                    ItemStack stack = recipe.secondaryOutput[i];
                    float chance = recipe.secondaryChance[i];
                    r = r.addToSecondaryOutput(stack, chance);
                }
            }
        }
    }
}
