package com.lying.wheelchairs.network;

import com.lying.wheelchairs.reference.Reference;

import net.minecraft.util.Identifier;

public class WHCPacketHandler
{
	public static final Identifier OPEN_INVENTORY_ID	= make("open_inventory_screen");
	
	private static Identifier make(String nameIn) { return new Identifier(Reference.ModInfo.MOD_ID, nameIn); }

}