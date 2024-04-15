package com.rooxchicken.fireclient.modules;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.codec.net.PercentCodec;
import org.apache.http.conn.routing.RouteInfo.LayerType;
import org.joml.Matrix4f;
import org.lwjgl.glfw.GLFW;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.ibm.icu.util.CodePointTrie.Small;
import com.rooxchicken.fireclient.FireClient;
import com.rooxchicken.fireclient.client.FireClientside;
import com.rooxchicken.fireclient.screen.FireClientMainScreen;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gl.VertexBuffer.Usage;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

class InventorySlot
{
	public int slot = -1;
	public ItemStack itemStack = null;

	public InventorySlot(int _slot, ItemStack _itemStack)
	{
		slot = _slot;
		itemStack = _itemStack;
	}
}

public class BowSwap extends ModuleBase implements HudRenderCallback
{
	private TextRenderer textRenderer;

	private ButtonWidget enabledButton;

	private ArrayList<InventorySlot> arrowList;
	private int a_amount = -1;
	private int a_index = 1;
	
	private ArrayList<InventorySlot> bowList;
	private int b_amount = -1;
	private int b_index = 1;

	private HashMap<Integer, Integer> links = new HashMap<Integer, Integer>();
	private ItemStack currentArrow = ItemStack.EMPTY;

	public KeyBinding ArrowKey;
	public KeyBinding BowKey;
	public KeyBinding BothKey;
	
	@Override
	public void Initialize()
	{
		Name = "BowSwap";
		Description = "Swaps a bow & arrow combination with the press of a button";
		Enabled = true;
		KeyName = "key.hazelgatekept_bowswap";
		Scale = 1;
		SmallestSize = 0.5;
		
		HasLines = true;

		PositionX = 0;
		PositionY = -100;

		x1Mod = -1;
		x2Mod = 17;
		y1Mod = -3;
		y2Mod = 17;

		arrowList = new ArrayList<InventorySlot>();
		links = new HashMap<Integer, Integer>();

		FireClient.LOGGER.info("Module: " + Name + " loaded successfully.");
	}

	@Override
	public void ClientInitialization()
	{
		HudRenderCallback.EVENT.register(this);
	}

	@Override
	public void PostInitialization()
	{
		Visible = FireClient.FIRECLIENT_WHITELISTED;
		if(!Visible)
			Enabled = false;
	}
	@Override
	public void RegisterKeyBinds(String category)
	{
		ArrowKey = KeyBindingHelper.registerKeyBinding(
				new KeyBinding("key.hazelgatekept_arrowswap", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_N, category));
		BowKey = KeyBindingHelper.registerKeyBinding(
			new KeyBinding("key.hazelgatekept_bowswap", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_M, category));

		BothKey = KeyBindingHelper.registerKeyBinding(
			new KeyBinding("key.hazelgatekept_bothswap", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_B, category));
		
	}

	@Override
	public void CheckKey()
	{
		if(!Enabled)
			return;
		
		if(ArrowKey.wasPressed())
			swapArrows();

		if(BowKey.wasPressed())
			swapBows();

		if(BothKey.wasPressed())
		{
			swapArrows();
			swapBows();
		}
	}

	private void swapArrows()
	{
		MinecraftClient client = MinecraftClient.getInstance();
		arrowList = new ArrayList<InventorySlot>();

		int _amount = 0;

		for(int i = 0; i < 41; i++)
		{
			if(client.player.getInventory().getStack(i).getTranslationKey().contains("arrow"))
			{
				arrowList.add(new InventorySlot(i, client.player.getInventory().getStack(i)));
				_amount++;
			}
		}

		if(arrowList.size() < 2)
			return;

		if(a_amount != _amount)
		{
			a_index = 1;
			a_amount = _amount;
		}

		swapSlots(arrowList.get(0).slot, arrowList.get(a_index).slot);

		a_index++;
		if(a_index > a_amount-1)
			a_index = 1;
	}

	private void swapBows()
	{
		MinecraftClient client = MinecraftClient.getInstance();
		bowList = new ArrayList<InventorySlot>();

		int _amount = 0;

		for(int i = 0; i < 41; i++)
		{
			if(client.player.getInventory().getStack(i).getTranslationKey().contains("bow"))
			{
				bowList.add(new InventorySlot(i, client.player.getInventory().getStack(i)));
				_amount++;
			}
		}

		if(bowList.size() < 2)
			return;

		if(b_amount != _amount)
		{
			b_index = 1;
			b_amount = _amount;
		}

		swapSlots(bowList.get(0).slot, bowList.get(b_index).slot);

		b_index++;
		if(b_index > b_amount-1)
			b_index = 1;
	}

	public void swapSlots(int sourceSlot, int destSlot)
	{
		MinecraftClient client = MinecraftClient.getInstance();
		int syncId = client.player.playerScreenHandler.syncId;
		if(destSlot < 9)
			destSlot += 36;

		//dest -> source
		//client.player.interact(client.player, client.player.getActiveHand());
		
		client.interactionManager.clickSlot(syncId, destSlot, sourceSlot, SlotActionType.SWAP, client.player);
	}

	@Override
	public void Update()
	{
		MinecraftClient client = MinecraftClient.getInstance();

		if(client.player != null)
		{
			RefreshInventory();
		}
	}

	public void RefreshInventory()
	{
		MinecraftClient client = MinecraftClient.getInstance();
		for(int i = 0; i < 36; i++)
		{
			if(client.player.getInventory().getStack(i).getTranslationKey().contains("arrow"))
			{
				currentArrow = client.player.getInventory().getStack(i).copy();
				return;
			}
		}

		currentArrow = ItemStack.EMPTY;
	}
	
	@Override
	public void Tick()
	{
		
	}

	@Override
	public void RenderConfiguration(FireClientMainScreen screen, DrawContext context, TextRenderer textRenderer, int mouseX, int mouseY)
	{
		context.drawCenteredTextWithShadow(textRenderer, Text.literal("BowSwap Configuration"), screen.width / 2, screen.height/2 - 35, 0xffffff);
	}

	@Override
	public void BeforeLines(DrawContext context)
	{
		onHudRender(context, 0);
	}
	
	@Override
	public void onHudRender(DrawContext drawContext, float tickDelta)
	{
		if(!Enabled || currentArrow == ItemStack.EMPTY)
			return;
		
		MinecraftClient client = MinecraftClient.getInstance();
		textRenderer = client.textRenderer;

		MatrixStack matrixStack = drawContext.getMatrices();
		matrixStack.push();
		matrixStack.scale((float)screenScale, (float)screenScale, (float)screenScale);
		matrixStack.translate(screenX/screenScale, screenY/screenScale, 0);
		drawContext.drawItem(currentArrow, 0, 0);
		drawContext.drawText(textRenderer, "" + currentArrow.getCount(), 5, 8, 0xFFFFFFFF, true);
		matrixStack.pop();
	}
	
	@Override
	public void UpdateScreen(boolean mouseDown, int mouseX, int mouseY)
	{
		
	}
	

	@Override
	public void OpenSettingsMenu(FireClientMainScreen screen, ButtonWidget button)
	{
		enabledButton = ButtonWidget.builder(Text.of("Enabled: " + Enabled), _button ->
        {
			Enabled = !Enabled;
			enabledButton.setMessage(Text.of("Enabled: " + Enabled));
			enabledButton.setTooltip(Tooltip.of(Text.of("Sets Enabled to:  " + !Enabled)));
        })
		.dimensions(screen.width / 2 - 50, screen.height / 2 - 10, 100, 20)
        .tooltip(Tooltip.of(Text.of("Sets Enabled to: " + !Enabled)))
        .build();

		screen.AddDrawableChild(enabledButton);
	}
	
	@Override
	public void CloseSettingsMenu(FireClientMainScreen screen)
	{
		
	}
	
	@Override
	public void LoadSettings(JsonObject file)
	{
		Enabled = file.get("Enabled").getAsBoolean();
	}

	@Override
	public void SaveSettings(JsonObject file)
	{
		HashMap<String, Object> moduleSettings = new HashMap<String, Object>();
		moduleSettings.put("Enabled", Enabled);

		file.addProperty(Name, new Gson().toJson(moduleSettings));
	}

}
