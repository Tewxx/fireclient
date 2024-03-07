package com.rooxchicken.fireclient.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.option.SimpleOption;

@Mixin(SimpleOption.class)
public class MixinSimpleOption <T>
{
	@Shadow
	T value;
	
	@Inject(method = "setValue(Ljava/lang/Object;)V", at = @At("HEAD"), cancellable = true)
	public void setValue(T value, CallbackInfo info)
	{
		this.value = value;
		info.cancel();
	}
}
