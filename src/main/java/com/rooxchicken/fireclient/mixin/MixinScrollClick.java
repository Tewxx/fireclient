package com.rooxchicken.fireclient.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.rooxchicken.fireclient.FireClient;
import com.rooxchicken.fireclient.modules.ScrollClick;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import net.minecraft.screen.slot.SlotActionType;

@Mixin(MinecraftClient.class)
abstract class MixinScrollItemUse
{/*
    @Invoker("doItemUse") protected abstract void invokeDoItemUse();

    @Inject(at = @At("HEAD"), method = "tick")
	private void tick(CallbackInfo info)
    {
        if(!FireClient.Modules.get("ScrollClick").Enabled)
            return;
            
        if(MinecraftClient.getInstance().player != null)
            for(int i = 0; i < ((ScrollClick)FireClient.Modules.get("ScrollClick")).clicks; i++)
            {
                invokeDoItemUse();
                ((ScrollClick)FireClient.Modules.get("ScrollClick")).clicks--;
            }
    }
	*/
}

@Mixin(Mouse.class)
class MixinScrollAdder
{
	/*
    @Inject(method = "onMouseScroll(JDD)V", at = @At("HEAD"), cancellable = true)
    private void onMouseScroll(long window, double horizontal, double vertical, CallbackInfo info)
    {
        if(!FireClient.Modules.get("ScrollClick").Enabled)
            return;
        
        if(MinecraftClient.getInstance().player != null)
            ((ScrollClick)FireClient.Modules.get("ScrollClick")).clicks++;

        info.cancel();
    }
	*/
}
