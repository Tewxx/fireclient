package com.rooxchicken.fireclient.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.rooxchicken.fireclient.FireClient;
import com.rooxchicken.fireclient.modules.ModuleBase;
import com.rooxchicken.fireclient.modules.ToggleablePieChart;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.Window;

@Mixin(Window.class)
class MixinWindowModification {

	//private int oldWindowWidth = 854;
	//private int oldWindowHeight = 480;
	@Inject(method = "getFramebufferWidth()I", at = @At("HEAD"), cancellable = true)
	private void getFramebufferWidth(CallbackInfoReturnable<Integer> info) {
		if(((ToggleablePieChart)FireClient.Modules.get("ToggleablePieChart")).beingRendered)
		{
			((ToggleablePieChart)FireClient.Modules.get("ToggleablePieChart")).windowIndex++;
			if(((ToggleablePieChart)FireClient.Modules.get("ToggleablePieChart")).windowIndex > 2)
				info.setReturnValue(((ToggleablePieChart)FireClient.Modules.get("ToggleablePieChart")).PieChartPositionX);
			else
				info.setReturnValue((int)((ToggleablePieChart)FireClient.Modules.get("ToggleablePieChart")).ScaleX);
		}
    }
	
	@Inject(method = "getFramebufferHeight()I", at = @At("HEAD"), cancellable = true)
	private void getFramebufferHeight(CallbackInfoReturnable<Integer> info) {
		if(((ToggleablePieChart)FireClient.Modules.get("ToggleablePieChart")).beingRendered)
		{
			((ToggleablePieChart)FireClient.Modules.get("ToggleablePieChart")).windowIndex++;
			if(((ToggleablePieChart)FireClient.Modules.get("ToggleablePieChart")).windowIndex > 2)
				info.setReturnValue(((ToggleablePieChart)FireClient.Modules.get("ToggleablePieChart")).PieChartPositionY);
			else
				info.setReturnValue((int)((ToggleablePieChart)FireClient.Modules.get("ToggleablePieChart")).ScaleY);
		}
    }
	// @Inject(method = "onWindowSizeChanged(JII)V", at = @At("HEAD"), cancellable = true)
	// private void onWindowSizeChanged(long window, int width, int height, CallbackInfo info)
	// {
	// 	double multiplierX = (width / (oldWindowWidth + 0.0));
	// 	double multiplierY = (height / (oldWindowHeight + 0.0));

	// 	FireClient.LOGGER.info("" + multiplierX);
	// 	for(ModuleBase module : FireClient.Modules.values())
	// 	{
	// 		module.ResolutionUpdated(multiplierX-1, multiplierY-1);
	// 	}

	// 	oldWindowWidth = width;
	// 	oldWindowHeight = height;

	// 	// if(((ToggleablePieChart)FireClient.Modules.get("ToggleablePieChart")).oldSizeX == -1)
	// 	// {
	// 	// 	((ToggleablePieChart)FireClient.Modules.get("ToggleablePieChart")).oldSizeX = width;
	// 	// 	((ToggleablePieChart)FireClient.Modules.get("ToggleablePieChart")).oldSizeY = height;
	// 	// }
		
	// 	// ((ToggleablePieChart)FireClient.Modules.get("ToggleablePieChart")).PositionX *= (width + 0.0) / ((ToggleablePieChart)FireClient.Modules.get("ToggleablePieChart")).oldSizeX;
	// 	// ((ToggleablePieChart)FireClient.Modules.get("ToggleablePieChart")).PositionY *= (height + 0.0) / ((ToggleablePieChart)FireClient.Modules.get("ToggleablePieChart")).oldSizeY;
		
	// 	// ((ToggleablePieChart)FireClient.Modules.get("ToggleablePieChart")).oldSizeX = width;
	// 	// ((ToggleablePieChart)FireClient.Modules.get("ToggleablePieChart")).oldSizeY = height;
	// }
}

@Mixin(MinecraftClient.class)
class MixinDebugPieChart
{
	@Inject(method = "drawProfilerResults(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/util/profiler/ProfileResult;)V", at = @At("HEAD"))
	private void drawProfilerResultsHead(CallbackInfo info) {
		((ToggleablePieChart)FireClient.Modules.get("ToggleablePieChart")).UpdateValues();
		((ToggleablePieChart)FireClient.Modules.get("ToggleablePieChart")).beingRendered = true;
		((ToggleablePieChart)FireClient.Modules.get("ToggleablePieChart")).windowIndex = 0;
    }

	@Inject(method = "drawProfilerResults(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/util/profiler/ProfileResult;)V", at = @At("TAIL"))
	private void drawProfilerResultsTail(CallbackInfo info) {
		((ToggleablePieChart)FireClient.Modules.get("ToggleablePieChart")).beingRendered = false;
    }
	
	@Inject(method = "stop()V", at = @At("HEAD"))
	private void stop(CallbackInfo info)
	{
		FireClient.LOGGER.info("Disabling the PieChart");
		((ToggleablePieChart)FireClient.Modules.get("ToggleablePieChart")).Enabled = false;
	}

	@Inject(method = "shouldMonitorTickDuration()Z", at = @At("HEAD"), cancellable = true)
	private void shouldMonitorTickDuration(CallbackInfoReturnable<Boolean> info) {
		MinecraftClient client = MinecraftClient.getInstance();

		if(client != null)
			info.setReturnValue(((ToggleablePieChart)FireClient.Modules.get("ToggleablePieChart")).Enabled && !client.options.hudHidden);
		else
			info.setReturnValue(false);
    }
}
