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

import com.rooxchicken.fireclient.FireClient;
//import com.rooxchicken.fireclient.EntityFeatures.CatEarsFeature;
import com.rooxchicken.fireclient.client.FireClientside;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.Dilation;
import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelPartData;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.EntityRendererFactory.Context;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModelPartNames;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

@Mixin(PlayerEntityRenderer.class)
public abstract class MixinPlayerEntityRenderer extends LivingEntityRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>>
{   
    public MixinPlayerEntityRenderer(Context ctx, PlayerEntityModel<AbstractClientPlayerEntity> model,
			float shadowRadius) {
		super(ctx, model, shadowRadius);
	}
    
    // @Inject(method = "<init>(Lnet/minecraft/client/render/entity/EntityRendererFactory$Context;Z)V", at = @At("TAIL"))
    // public void PlayerEntityRenderer(EntityRendererFactory.Context ctx, boolean slim, CallbackInfo info)
    // {
    // 	//FireClient.LOGGER.info("Hello! - mixin");
    // 	//this.addFeature(new CatEarsFeature(this));
    // }
//    
//    @Inject(method = "renderEars()V", at = @At("HEAD"), cancellable = true)
//    public void renderEars(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, CallbackInfo info)
//    {
//    	//this.ear.copyTransform(this.head);
//        this.ear.pivotX = 0.0f;
//        this.ear.pivotY = 0.0f;
//        this.ear.render(matrices, vertices, light, overlay);
//    	info.cancel();
//    }
    
    // private HashMap<PlayerEntity, Float> playerHealthStored = new HashMap<PlayerEntity, Float>();
    // private HashMap<PlayerEntity, Float> lastPlayerHealth = new HashMap<PlayerEntity, Float>();

	// @ModifyArgs(
    //         // This specifies method
    //         method = "renderLabelIfPresent(Lnet/minecraft/client/network/AbstractClientPlayerEntity;Lnet/minecraft/text/Text;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V",
    //         at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/LivingEntityRenderer;renderLabelIfPresent(Lnet/minecraft/entity/Entity;Lnet/minecraft/text/Text;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", ordinal = 1)
    // )
    // public void nametagReplace(Args args)
	// {
    //     PlayerEntity entity = args.get(0);
    //     Text text = args.get(1);
        
    //     MinecraftClient client = MinecraftClient.getInstance();
        
    //     if(FireClient.Setting_HealthDifferenceDisplay && (entity == client.player.getAttacker() || entity == client.player.getAttacking()))
    //     {
    //     	if(!lastPlayerHealth.containsKey(client.player))
    //         {
    //         	lastPlayerHealth.put(client.player, client.player.getHealth());
    //         }
            
    //         if(!playerHealthStored.containsKey(entity))
    //         {
    //         	playerHealthStored.put(entity, 0f);
    //         	lastPlayerHealth.put(entity, entity.getHealth());
    //         }
            
    //         float newPlayerHealth = lastPlayerHealth.get(client.player) - client.player.getHealth();
    //         if(newPlayerHealth < 0)
    //         	newPlayerHealth = 0;
        	
    //     	lastPlayerHealth.put(client.player, client.player.getHealth());
            
    //     	float newAttackerHealth = lastPlayerHealth.get(entity) - entity.getHealth();
    //     	if(newAttackerHealth < 0)
    //     		newAttackerHealth = 0;
        	
    //     	lastPlayerHealth.put(entity, entity.getHealth());
        	
    //     	addHashmapValue(playerHealthStored, entity, newPlayerHealth - newAttackerHealth);
        	
    //     	float v = playerHealthStored.get(entity) * -1;
    //     	MutableText healthDifferenceText = Text.literal(text.getString());
    //     	if(v < 0)
    //     		healthDifferenceText.append(Text.literal(String.format(" | %.2f", v)).copy().styled(s -> s.withColor(Formatting.RED)));
    //     	if(v > 0)
    //     		healthDifferenceText.append(Text.literal(String.format(" | %.2f", v)).copy().styled(s -> s.withColor(Formatting.GREEN)));
        	
    //     	text = healthDifferenceText;
            
    //         if(!client.player.isAlive())
    //         	playerHealthStored.remove(client.player);
            
    //         if(!entity.isAlive())
    //         	playerHealthStored.remove(entity);
    //     }

    //     args.set(1, text);
    // }
	
	// private void addHashmapValue(HashMap<PlayerEntity, Float> map, PlayerEntity key, float value)
	// {
	// 	map.put(key, map.get(key) + value);
	// }
}