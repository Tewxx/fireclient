package com.rooxchicken.fireclient.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.rooxchicken.fireclient.FireClient;
import com.rooxchicken.fireclient.modules.Nametag;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.CommandOutput;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Nameable;
import net.minecraft.world.entity.EntityLike;

@Mixin(LivingEntityRenderer.class)
class MixinRenderNametags<T extends LivingEntity, M extends EntityModel<T>>
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

// @Mixin(Entity.class)
// abstract class MixinModifyNametag
// implements Nameable,
// EntityLike,
// CommandOutput
// {
//     @Inject(method = "getName()Lnet/minecraft/text/Text;", at = @At("HEAD"), cancellable = true)
//     public Text getName(CallbackInfoReturnable<Text> info)
//     {
//         //FireClient.LOGGER.info(((Nametag)FireClient.Modules.get("Nametag")).input);
//         if(!((Nametag)FireClient.Modules.get("Nametag")).Enabled)
//             return null;
        
//         if(!((Nametag)FireClient.Modules.get("Nametag")).RenderNametags || !((Nametag)FireClient.Modules.get("Nametag")).RenderOwnNametag)
//             return null;

//         if(((Nametag)FireClient.Modules.get("Nametag")).input != "" && ((Entity)(Object)this) == MinecraftClient.getInstance().player)
//         {
//             Text text = Text.of(((Nametag)FireClient.Modules.get("Nametag")).input);
//             FireClient.LOGGER.info("HI");
//             info.setReturnValue(text);
//             info.cancel();
//             return text;
//         }

//         return null;
//     }

    // @Inject(method = "renderLabelIfPresent(Lnet/minecraft/entity/Entity;Lnet/minecraft/text/Text;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At("HEAD"))
    // protected void renderLabelIfPresent(T entity, Text text, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo info)
    // {
    //     text = Text.of("HELLO!");
    // }
//}

// @Mixin(EntityRenderer.class)
// abstract class MixinModifyNametag<T extends Entity>
// {
//     @Invoker("hasLabel") protected abstract boolean hasLabel(T entity);
//     //@Invoker("renderLabelIfPresent") protected abstract void renderLabelIfPresent(T entity, Text text, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light);

//     @Inject(method = "render(Lnet/minecraft/entity/Entity;FFLnet/minecraft/text/Text;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At("HEAD"), cancellable = true)
//     public void render(T entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo info)
//     {
//         if(entity != MinecraftClient.getInstance().player || !((Nametag)FireClient.Modules.get("Nametag")).Enabled || !((Nametag)FireClient.Modules.get("Nametag")).RenderNametags || !((Nametag)FireClient.Modules.get("Nametag")).RenderOwnNametag || ((Nametag)FireClient.Modules.get("Nametag")).input == "")
//             return;
        
//         if (!hasLabel(entity)) {
//             return;
//         }

//         MutableText text = Text.of(((Nametag)FireClient.Modules.get("Nametag")).input).copy();
//         text.fillStyle(((Entity)entity).getDisplayName().getStyle());

//         renderLabelIfPresent(entity, text, matrices, vertexConsumers, light);

//         info.cancel();
//     }
// }