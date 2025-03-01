package com.lying.wheelchairs.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.lying.wheelchairs.item.ItemCrutch;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.BipedEntityModel.ArmPose;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;

@Mixin(BipedEntityModel.class)
public class BipedEntityModelMixin extends EntityModelMixin
{
	@Shadow
	public ModelPart rightLeg;
	
	@Shadow
	public ModelPart leftLeg;
	
	@Shadow
	public ArmPose rightArmPose;
	
	@Shadow
	public ArmPose leftArmPose;
	
	@Shadow
	public ModelPart getArm(Arm armIn) { return null; }
	
	@Inject(method = "setAngles(Lnet/minecraft/entity/LivingEntity;FFFFF)V", at = @At("TAIL"), cancellable = true)
	public void whc$setAngles(LivingEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float headYaw, float headPitch, final CallbackInfo ci)
	{
		if(entity.getType() != EntityType.PLAYER || !(entity.isInPose(EntityPose.STANDING) || entity.isInPose(EntityPose.CROUCHING)) || (rightArmPose.isTwoHanded() || leftArmPose.isTwoHanded()))
			return;
		
		ModelPart mainLeg = getLeg(entity.getMainArm());
		ModelPart offLeg = getLeg(entity.getMainArm().getOpposite());
		boolean isPair = isHoldingPair(entity);
		boolean rightHanded = entity.getMainArm() == Arm.RIGHT;
		boolean isCrouching = entity.isInPose(EntityPose.CROUCHING);
		
		double amount = isCrouching ? 25D : entity.hasVehicle() ? 15D : 10D;
		float roll = (float)Math.toRadians(amount);
		if(entity.hasVehicle())
		{
			if(isCrutch(entity.getMainHandStack()))
				getArm(entity.getMainArm()).roll += roll * (rightHanded ? 1F : -1F);
			
			if(isCrutch(entity.getOffHandStack()))
				getArm(entity.getMainArm().getOpposite()).roll += roll * (rightHanded ? -1F : 1F);
			
			return;
		}
		
		if(handSwingProgress == 0F || entity.getMainArm() != Arm.RIGHT)
			correctCrutchPose(entity.getMainArm() == Arm.RIGHT ? entity.getMainHandStack() : entity.getOffHandStack(), getArm(Arm.RIGHT), mainLeg, offLeg, rightHanded, isPair);
		if(handSwingProgress == 0F || entity.getMainArm() != Arm.LEFT)
			correctCrutchPose(entity.getMainArm() == Arm.LEFT ? entity.getMainHandStack() : entity.getOffHandStack(), getArm(Arm.LEFT), mainLeg, offLeg, !rightHanded, isPair);
		
		if(isPair)
		{
			getArm(Arm.RIGHT).roll = roll;
			getArm(Arm.LEFT).roll = -roll;
			
			offLeg.pitch = (Math.abs(offLeg.pitch) * 0.3F) + (float)Math.toRadians(40D);
		}
		else if(isCrouching)
		{
			if(isCrutch(entity.getMainHandStack()))
				getArm(entity.getMainArm()).roll = roll * (rightHanded ? 1F : -1F);
			
			if(isCrutch(entity.getOffHandStack()))
				getArm(entity.getMainArm().getOpposite()).roll = roll * (rightHanded ? -1F : 1F);
		}
	}
	
	private ModelPart getLeg(Arm arm) { return arm == Arm.RIGHT ? rightLeg : leftLeg; }
	
	private static boolean isCrutch(ItemStack stack) { return !stack.isEmpty() && stack.getItem() instanceof ItemCrutch; }
	
	private static boolean isHoldingPair(LivingEntity entity) { return isCrutch(entity.getMainHandStack()) && isCrutch(entity.getOffHandStack()); }
	
	private static void correctCrutchPose(ItemStack crutch, ModelPart arm, ModelPart legMain, ModelPart legOff, boolean isMain, boolean isPair)
	{
		if(!isCrutch(crutch))
			return;
		
		if(isPair)
			arm.pitch = legOff.pitch;
		else
			arm.pitch = isMain ? legMain.pitch : legOff.pitch;
		
		arm.pitch *= 0.5F;
		arm.yaw = 0F;
		arm.roll = 0F;
	}
}
