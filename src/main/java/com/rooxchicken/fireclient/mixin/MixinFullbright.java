package com.rooxchicken.fireclient.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.rooxchicken.fireclient.FireClient;
import com.rooxchicken.fireclient.modules.FullBright;

import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.SimpleOption;

@Mixin(GameOptions.class)
class MixinFullbright <T>
{
	private boolean wasEnabled = false;

	@Inject(method = "write()V", at = @At("HEAD"))
	public void writeHEAD(CallbackInfo info)
	{
		wasEnabled = ((FullBright)FireClient.Modules.get("FullBright")).Enabled;
		if(wasEnabled)
		{
			((FullBright)FireClient.Modules.get("FullBright")).Enabled = false;
			((FullBright)FireClient.Modules.get("FullBright")).CheckStatus();
		}
	}

	@Inject(method = "write()V", at = @At("TAIL"))
	public void writeTAIL(CallbackInfo info)
	{
		if(wasEnabled)
		{
			((FullBright)FireClient.Modules.get("FullBright")).Enabled = true;
			((FullBright)FireClient.Modules.get("FullBright")).CheckStatus();
		}
	}
}
