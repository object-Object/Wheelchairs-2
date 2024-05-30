package com.lying.client.init;

import java.util.function.Supplier;

import com.lying.client.renderer.entity.model.WheelchairElytraModel;
import com.lying.reference.Reference;

import dev.architectury.registry.client.level.entity.EntityModelLayerRegistry;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;

public class WHCModelParts
{
	public static final EntityModelLayer UPGRADE_ELYTRA	= ofName("wheelchair_elytra", "main");
	
	private static EntityModelLayer ofName(String main, String part)
	{
		return new EntityModelLayer(new Identifier(Reference.ModInfo.MOD_ID, main), part);
	}
	
	public static void init()
	{
		register(WHCModelParts.UPGRADE_ELYTRA, WheelchairElytraModel::createBodyLayer);
	}
	
	private static void register(EntityModelLayer layer, Supplier<TexturedModelData> func)
	{
		EntityModelLayerRegistry.register(layer, func);
	}
}
