package com.rooxchicken.fireclient.mixin;

import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.rooxchicken.fireclient.FireClient;
import com.rooxchicken.fireclient.modules.RenderWorld;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;


@Mixin(WorldRenderer.class)
class MixinRenderWorld
{
	@Inject(method = "renderLayer(Lnet/minecraft/client/render/RenderLayer;DDDLorg/joml/Matrix4f;Lorg/joml/Matrix4f;)V", at = @At("HEAD"), cancellable = true)
	private void renderLayer(RenderLayer renderLayer, double x, double y, double z, Matrix4f matrix4f, Matrix4f positionMatrix, CallbackInfo info)
	{
		if(!((RenderWorld)FireClient.Modules.get("RenderWorld")).renderWorld)
			info.cancel();
	}
	
//	@Inject(method = "renderEntity(Lnet/minecraft/entity/Entity;DDDFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;)V", at = @At("HEAD"))
//	private void renderEntity(Entity entity, double cameraX, double cameraY, double cameraZ, RenderTickCounter tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, CallbackInfo info)
//	{
//		if(FireClientside.villageruuids.containsKey(entity.getUuidAsString()))
//		{
//			BlockPos blockPos = FireClientside.villageruuids.get(entity.getUuidAsString());
//			
////			RenderSystem.enableDepthTest();
////	        RenderSystem.depthFunc(515);
////	        RenderSystem.enableBlend();
////	        RenderSystem.defaultBlendFunc();
////	        RenderSystem.depthMask(false);
////	
////	        Tessellator tessellator = Tessellator.getInstance();
////	        BufferBuilder bufferBuilder = tessellator.getBuffer();
////	
////	        double posX = cameraX - blockPos.getX();
////	        double posY = cameraY - blockPos.getY();
////	        double posZ = cameraZ - blockPos.getZ();
////	        
////	        //FireClient.LOGGER.info(posX + " " + posY + " " + posZ);
////	        
////	        RenderSystem.setShader(GameRenderer::getPositionColorProgram);
////	        bufferBuilder.begin(DrawMode.DEBUG_LINES, VertexFormats.POSITION_COLOR);
////	        bufferBuilder.vertex(posX, posY, posZ).color(255,0,0,255).next();
////	        bufferBuilder.vertex(posX, posY - 3f, posZ).color(255,0,0,255).next();
////	        tessellator.draw();
////	
////	        RenderSystem.enableCull();
////	        RenderSystem.depthMask(true);
////	        RenderSystem.disableBlend();
////	        RenderSystem.defaultBlendFunc();
//		}
		
		
	//}
}
