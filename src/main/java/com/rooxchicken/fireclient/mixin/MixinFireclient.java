package com.rooxchicken.fireclient.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.mojang.authlib.GameProfile;
import com.rooxchicken.fireclient.FireClient;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.AbstractParentElement;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.HeldItemFeatureRenderer;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShieldItem;
import net.minecraft.util.Arm;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@Mixin(HeldItemRenderer.class)
class MixinHideShieldsInRiptide
{
    @Inject(method = "renderItem(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformationMode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At("HEAD"), cancellable = true)
    public void renderItem(LivingEntity entity, ItemStack stack, ModelTransformationMode renderMode, boolean leftHanded, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo info)
    {
        if(FireClient.Setting_HideShieldsInRiptide)
        {
        MinecraftClient client = MinecraftClient.getInstance();
        if(client.player.isUsingRiptide())
            if(stack.getItem().getClass() == ShieldItem.class)
            {
                info.cancel();
            }
        }

        
    }
}

@Mixin(AbstractClientPlayerEntity.class)
abstract class MixinCustomCapes extends PlayerEntity
{
    GameProfile profile;

    public MixinCustomCapes(World world, BlockPos pos, float yaw, GameProfile gameProfile) {
        super(world, pos, yaw, gameProfile);
        profile = gameProfile;
        //TODO Auto-generated constructor stub
    }

    // @Inject(method = "getCapeTexture()Lnet/minecraft/util/Identifier;", at = @At("HEAD"), cancellable = true)
    // public Identifier getCapeTexture(CallbackInfoReturnable info)
    // {
    //     if(!FireClient.Setting_CustomCape)
    //         return null;

    //     String username = this.getName().getString();

    //     if(FireClient.FIRECLIENT_CUSTOMCAPES.containsKey(username))
    //         info.setReturnValue(Identifier.of(("fireclient:textures/capes/" + FireClient.FIRECLIENT_CUSTOMCAPES.get(username) + "_cape.png")));
    //     else
    //         info.setReturnValue(null);
    //     info.cancel();

    //     return null;
    // }

    // @Inject(method = "getElytraTexture()Lnet/minecraft/util/Identifier;", at = @At("HEAD"), cancellable = true)
    // public Identifier getElytraTexture(CallbackInfoReturnable info)
    // {
    //     if(!FireClient.Setting_CustomCape)
    //         return null;

    //         String username = this.getName().getString();

    //     if(FireClient.FIRECLIENT_CUSTOMCAPES.containsKey(username))
    //         info.setReturnValue(Identifier.of(("fireclient:textures/capes/" + FireClient.FIRECLIENT_CUSTOMCAPES.get(username) + "_elytra.png")));
    //     else
    //         info.setReturnValue(null);
    //     info.cancel();

    //     return null;
    // }
    
}

@Mixin(SimpleOption.class)
class MixinRemoveOptionLimits <T>
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