package net.ilexiconn.rearrange.client;

import net.ilexiconn.rearrange.api.RearrangeAPI;
import net.ilexiconn.rearrange.client.component.HotbarComponent;
import net.ilexiconn.rearrange.server.ServerProxy;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

@SideOnly(Side.CLIENT)
public class ClientProxy extends ServerProxy {
    public static KeyBinding keyEditComponents = new KeyBinding("key.editComponents", Keyboard.KEY_G, "key.categories.misc");

    public void preInit() {
        ClientEventHandler eventHandler = new ClientEventHandler();
        MinecraftForge.EVENT_BUS.register(eventHandler);
        ClientRegistry.registerKeyBinding(keyEditComponents);

        RearrangeAPI.registerOverrideComponent(new HotbarComponent(), RenderGameOverlayEvent.ElementType.HOTBAR);
    }
}
