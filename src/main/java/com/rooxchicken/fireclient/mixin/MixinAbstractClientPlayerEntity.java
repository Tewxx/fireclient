package com.rooxchicken.fireclient.mixin;

import java.util.HashMap;

import org.apache.logging.log4j.core.util.KeyValuePair;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import com.mojang.authlib.GameProfile;
import com.mojang.util.UUIDTypeAdapter;
import com.rooxchicken.fireclient.FireClient;
//import com.rooxchicken.fireclient.EntityFeatures.CatEarsFeature;
import com.rooxchicken.fireclient.client.FireClientside;

import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.Dilation;
import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelPartData;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.render.entity.EntityRendererFactory.Context;
import net.minecraft.client.render.entity.feature.CapeFeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.PlayerModelPart;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModelPartNames;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.world.World;


@Mixin(AbstractClientPlayerEntity.class)
public abstract class MixinAbstractClientPlayerEntity extends PlayerEntity
{
    GameProfile profile;

    public MixinAbstractClientPlayerEntity(World world, BlockPos pos, float yaw, GameProfile gameProfile) {
        super(world, pos, yaw, gameProfile);
        profile = gameProfile;
        //TODO Auto-generated constructor stub
    }

    @Inject(method = "getCapeTexture()Lnet/minecraft/util/Identifier;", at = @At("HEAD"), cancellable = true)
    public Identifier getCapeTexture(CallbackInfoReturnable info)
    {
        if(!FireClient.Setting_CustomCape)
            return null;

        String username = this.getName().getString();

        if(FireClient.FIRECLIENT_CUSTOMCAPES.containsKey(username))
            info.setReturnValue(new Identifier(("fireclient:textures/capes/" + FireClient.FIRECLIENT_CUSTOMCAPES.get(username) + "_cape.png")));
        else
            info.setReturnValue(null);
        info.cancel();

        return null;
    }

    @Inject(method = "getElytraTexture()Lnet/minecraft/util/Identifier;", at = @At("HEAD"), cancellable = true)
    public Identifier getElytraTexture(CallbackInfoReturnable info)
    {
        if(!FireClient.Setting_CustomCape)
            return null;

            String username = this.getName().getString();

        if(FireClient.FIRECLIENT_CUSTOMCAPES.containsKey(username))
            info.setReturnValue(new Identifier(("fireclient:textures/capes/" + FireClient.FIRECLIENT_CUSTOMCAPES.get(username) + "_elytra.png")));
        else
            info.setReturnValue(null);
        info.cancel();

        return null;
    }
    
}