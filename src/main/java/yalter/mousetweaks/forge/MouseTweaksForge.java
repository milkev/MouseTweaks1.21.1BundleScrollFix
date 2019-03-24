package yalter.mousetweaks.forge;

import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.GuiScreenEvent.MouseClickedEvent;
import net.minecraftforge.client.event.GuiScreenEvent.MouseReleasedEvent;
import net.minecraftforge.client.event.GuiScreenEvent.MouseDragEvent;
import net.minecraftforge.client.event.GuiScreenEvent.MouseScrollEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;
import yalter.mousetweaks.Constants;
import yalter.mousetweaks.Logger;
import yalter.mousetweaks.Main;
import yalter.mousetweaks.MouseButton;

@Mod(Constants.MOD_ID)
public class MouseTweaksForge {
	public MouseTweaksForge() {
		Main.initialize();
		MinecraftForge.EVENT_BUS.register(this);
	}

//	@SubscribeEvent
//	public void onRenderTick(TickEvent.RenderTickEvent event) {
//		if (Main.onTickMethod == OnTickMethod.FORGE && event.phase == TickEvent.Phase.START)
//			Main.onUpdateInGame();
//	}

	@SubscribeEvent
	public void onGuiOpen(GuiOpenEvent event) {
	    // Send when a gui is opened or closed (with null in getGui() in the latter case).
		Logger.DebugLog("onGuiOpen gui = " + event.getGui());
		Main.onGuiOpen(event.getGui());
	}

	@SubscribeEvent
	public void onGuiMouseClickedPre(MouseClickedEvent.Pre event) {
		Logger.DebugLog("onGuiMouseClickedPre button = " + event.getButton());

		MouseButton button = eventButtonToMouseButton(event.getButton());
		if (button != null) {
			if (Main.onMouseClicked(event.getMouseX(), event.getMouseY(), button))
				event.setCanceled(true);
		}
	}

	@SubscribeEvent
	public void onGuiMouseReleasedPre(MouseReleasedEvent.Pre event) {
		Logger.DebugLog("onGuiMouseReleasedPre button = " + event.getButton());

		MouseButton button = eventButtonToMouseButton(event.getButton());
		if (button != null) {
			if (Main.onMouseReleased(event.getMouseX(), event.getMouseY(), button))
				event.setCanceled(true);
		}
	}

	@SubscribeEvent
	public void onGuiMouseScrollPost(MouseScrollEvent.Post event) {
	    // Sent when nothing handled the scroll itself. For example, the creative inventory handles scroll anywhere on
		// screen, so this event is suppressed. Quick scrolls at limited FPS result in multiple scroll events rather
		// than one with a bigger delta.
		Logger.DebugLog("onGuiMouseScrollPost delta = " + event.getScrollDelta());

		if (Main.onMouseScrolled(event.getMouseX(), event.getMouseY(), event.getScrollDelta()))
			event.setCanceled(true);
	}

	@SubscribeEvent
	public void onGuiMouseDragPre(MouseDragEvent.Pre event) {
	    // Sent when a mouse is dragged while a mouse button is down (so between Clicked and Released events). The
		// rate of reporting is high even when the FPS is limited through the options.
		Logger.DebugLog("onGuiMouseDragPre dx = " + event.getDragX() + ", dy = " + event.getDragY());
	}

	private static MouseButton eventButtonToMouseButton(int eventButton) {
		switch (eventButton) {
			case GLFW.GLFW_MOUSE_BUTTON_LEFT:
				return MouseButton.LEFT;
			case GLFW.GLFW_MOUSE_BUTTON_RIGHT:
				return MouseButton.RIGHT;
            default:
                return null;
		}
	}
}
