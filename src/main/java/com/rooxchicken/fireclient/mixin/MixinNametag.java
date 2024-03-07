package com.rooxchicken.fireclient.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.rooxchicken.fireclient.FireClient;
import com.rooxchicken.fireclient.modules.Nametag;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;

@Mixin(LivingEntityRenderer.class)
class MixinLivingEntityRenderer<T extends LivingEntity, M extends EntityModel<T>>
{
    @Inject(method = "hasLabel(Lnet/minecraft/entity/LivingEntity;)Z", at = @At("HEAD"), cancellable = true)
    public boolean hasLabel(T livingEntity, CallbackInfoReturnable info)
    {
        if(!((Nametag)FireClient.Modules.get("Nametag")).Enabled)
            return false;
        
        if(!((Nametag)FireClient.Modules.get("Nametag")).RenderNametags)
        {
            info.setReturnValue(false);
            info.cancel();
            return false;
        }
        
        if(((Nametag)FireClient.Modules.get("Nametag")).RenderOwnNametag && livingEntity == MinecraftClient.getInstance().player)
        {
            info.setReturnValue(true);
            info.cancel();
            return true;
        }
        
        return ((Entity)livingEntity).shouldRenderName() && ((Entity)livingEntity).hasCustomName();
    }
}
