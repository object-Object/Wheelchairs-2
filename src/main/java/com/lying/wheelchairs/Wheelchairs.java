package com.lying.wheelchairs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lying.wheelchairs.init.WHCBlocks;
import com.lying.wheelchairs.init.WHCEnchantments;
import com.lying.wheelchairs.init.WHCEntityTypes;
import com.lying.wheelchairs.init.WHCItems;
import com.lying.wheelchairs.init.WHCScreenHandlerTypes;
import com.lying.wheelchairs.init.WHCSoundEvents;
import com.lying.wheelchairs.init.WHCSpecialRecipes;
import com.lying.wheelchairs.init.WHCUpgrades;
import com.lying.wheelchairs.network.FlyingMountRocketReceiver;
import com.lying.wheelchairs.network.OpenInventoryScreenReceiver;
import com.lying.wheelchairs.network.StartFlyingReceiver;
import com.lying.wheelchairs.network.WHCPacketHandler;
import com.lying.wheelchairs.reference.Reference;
import com.lying.wheelchairs.utility.ServerBus;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

public class Wheelchairs implements ModInitializer
{
    public static final Logger LOGGER = LoggerFactory.getLogger(Reference.ModInfo.MOD_ID);
    
	public void onInitialize()
	{
		ServerBus.registerEventCallbacks();
		WHCItems.init();
		WHCUpgrades.init();
		WHCEnchantments.init();
		WHCBlocks.init();
		WHCEntityTypes.init();
		WHCSpecialRecipes.init();
		WHCScreenHandlerTypes.init();
		WHCSoundEvents.init();
		
		ServerPlayNetworking.registerGlobalReceiver(WHCPacketHandler.OPEN_INVENTORY_ID, new OpenInventoryScreenReceiver());
		ServerPlayNetworking.registerGlobalReceiver(WHCPacketHandler.FLYING_START_ID, new StartFlyingReceiver());
		ServerPlayNetworking.registerGlobalReceiver(WHCPacketHandler.FLYING_ROCKET_ID, new FlyingMountRocketReceiver());
	}
}