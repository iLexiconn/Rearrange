package net.ilexiconn.rearrange.client;

import net.ilexiconn.rearrange.api.RearrangeAPI;
import net.ilexiconn.rearrange.api.component.IComponent;
import net.ilexiconn.rearrange.api.component.IComponentConfig;
import net.ilexiconn.rearrange.client.gui.GuiEditComponents;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientEventHandler {
    public Minecraft mc = Minecraft.getMinecraft();

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        for (IComponent component : RearrangeAPI.getComponentList()) {
            component.update(RearrangeAPI.getConfigForComponent(component));
        }
    }

    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent.Pre event) {
        IComponent component = RearrangeAPI.getComponentForType(event.type);
        if (component != null) {
            event.setCanceled(true);
            if (mc.currentScreen instanceof GuiEditComponents) {
                return;
            }
            IComponentConfig config = RearrangeAPI.getConfigForComponent(component);
            boolean enabled = config.get("enabled");
            if (!enabled) {
                return;
            }
            int xPos = config.get("xPos");
            int yPos = config.get("yPos");
            component.render(xPos, yPos, config);
        }
    }

    @SubscribeEvent
    public void onKeyPress(InputEvent.KeyInputEvent event) {
        if (ClientProxy.keyEditComponents.isPressed()) {
            mc.displayGuiScreen(new GuiEditComponents());
        }
    }
}
