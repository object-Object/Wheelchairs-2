package com.lying.wheelchairs.data.recipe;

import java.util.LinkedHashMap;
import java.util.Map;

import com.lying.wheelchairs.data.WHCItemTags;

import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementCriterion;
import net.minecraft.advancement.AdvancementRequirements;
import net.minecraft.advancement.AdvancementRewards;
import net.minecraft.advancement.criterion.RecipeUnlockedCriterion;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.util.Identifier;

public class RecipeWheelchairJsonBuilder
{
	private final RecipeCategory category;
	private final ItemStack result;
	private final Ingredient backing, cushion, wheelL, wheelR;
	private final Map<String, AdvancementCriterion<?>> criteria = new LinkedHashMap<>();
	
	public RecipeWheelchairJsonBuilder(ItemStack result, Ingredient backing, RecipeCategory category)
	{
		this(result, backing, Ingredient.fromTag(ItemTags.WOOL), Ingredient.fromTag(WHCItemTags.WHEEL), Ingredient.fromTag(WHCItemTags.WHEEL), category);
	}
	
	public RecipeWheelchairJsonBuilder(ItemStack result, Ingredient backing, Ingredient cushion, Ingredient wheelL, Ingredient wheelR, RecipeCategory category)
	{
		this.category = category;
		this.backing = backing;
		this.cushion = cushion;
		this.wheelL = wheelL;
		this.wheelR = wheelR;
		this.result = result;
	}
	
	public RecipeWheelchairJsonBuilder criterion(String name, AdvancementCriterion<?> criterion)
	{
		this.criteria.put(name, criterion);
		return this;
	}
	
	public void offerTo(RecipeExporter exporter, Identifier recipeId)
	{
		if(this.criteria.isEmpty())
			throw new IllegalStateException("No way of obtaining recipe " + recipeId);
		Advancement.Builder builder = exporter.getAdvancementBuilder().criterion("has_the_recipe", RecipeUnlockedCriterion.create(recipeId)).rewards(AdvancementRewards.Builder.recipe(recipeId)).criteriaMerger(AdvancementRequirements.CriterionMerger.OR);
		this.criteria.forEach(builder::criterion);
		RecipeWheelchair recipe = new RecipeWheelchair(this.result, this.backing, this.cushion, this.wheelL, this.wheelR);
		exporter.accept(recipeId, recipe, builder.build(recipeId.withPrefixedPath("recipes/" + this.category.getName() + "/")));
	}
}
