package disastermoo.immersiveevolution.common;

import net.minecraft.item.ItemStack;

import blusunrize.immersiveengineering.api.crafting.CrusherRecipe;
import disastermoo.immersiveevolution.api.crafting.TieredCrusherRecipe;

@SuppressWarnings("WeakerAccess")
public class EvolutionRecipes
{

    public static void init()
    {
        initCrusherRecipes();
    }

    private static TieredCrusherRecipe addCrusherRecipe(ItemStack output, Object input, int tier, Object... secondary)
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
            addCrusherRecipe(recipe.output, recipe.input, 1, (Object) recipe.secondaryOutput);
        }
    }
}
