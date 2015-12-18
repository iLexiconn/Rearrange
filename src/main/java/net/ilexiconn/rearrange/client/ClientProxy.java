package net.ilexiconn.rearrange.client;

import net.ilexiconn.llibrary.common.crash.SimpleCrashReport;
import net.ilexiconn.rearrange.Rearrange;
import net.ilexiconn.rearrange.api.RearrangeAPI;
import net.ilexiconn.rearrange.api.component.IComponent;
import net.ilexiconn.rearrange.api.component.IComponentConfig;
import net.ilexiconn.rearrange.client.component.*;
import net.ilexiconn.rearrange.server.ServerProxy;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import java.io.IOException;

import static net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;

@SideOnly(Side.CLIENT)
public class ClientProxy extends ServerProxy {
    public static KeyBinding keyEditComponents = new KeyBinding("key.editComponents", Keyboard.KEY_G, "key.categories.misc");

    public void preInit() {
        ClientEventHandler eventHandler = new ClientEventHandler();
        MinecraftForge.EVENT_BUS.register(eventHandler);
        ClientRegistry.registerKeyBinding(keyEditComponents);

        RearrangeAPI.registerOverrideComponent(new HotbarComponent(), ElementType.HOTBAR);
        RearrangeAPI.registerOverrideComponent(new HealthComponent(), ElementType.HEALTH);
        RearrangeAPI.registerOverrideComponent(new FoodComponent(), ElementType.FOOD);
        RearrangeAPI.registerOverrideComponent(new ExperienceComponent(), ElementType.EXPERIENCE);
        RearrangeAPI.registerOverrideComponent(new ArmorComponent(), ElementType.ARMOR);
    }

    public void postInit() {
        for (IComponent component : RearrangeAPI.getComponentList()) {
            IComponentConfig config = RearrangeAPI.getConfigForComponent(component);
            if (config != null) {
                try {
                    config.load();
                } catch (IOException e) {
                    Rearrange.logger.error(SimpleCrashReport.makeCrashReport(e, "Unable to load config for component " + component.getComponentID()));
                }
            }
        }
    }
}
