package net.ilexiconn.rearrange.api;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.ilexiconn.rearrange.api.component.IComponent;
import net.ilexiconn.rearrange.api.component.IComponentConfig;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Map;

/**
 * The main API class for Rearrange.
 *
 * @author iLexiconn
 * @since 0.1.0
 */
@SideOnly(Side.CLIENT)
public class RearrangeAPI {
    public static final String VERSION = "0.1.0-develop";

    private static Map<IComponent, RenderGameOverlayEvent.ElementType> componentMap = Maps.newHashMap();
    private static Map<IComponent, IComponentConfig> configMap = Maps.newHashMap();

    /**
     * Register a new {@link net.ilexiconn.rearrange.api.component.IComponent} without overriding an existing HUD
     * element.
     *
     * @param component the component to register.
     */
    public static void registerComponent(IComponent component) {
        registerOverrideComponent(component, null);
    }

    /**
     * Register a new {@link net.ilexiconn.rearrange.api.component.IComponent}. If the element type isn't null, that
     * specific type will be overridden by this component.
     *
     * @param component the component to register.
     * @param type the component to override.
     */
    public static void registerOverrideComponent(IComponent component, RenderGameOverlayEvent.ElementType type) {
        if (type != null) {
            for (RenderGameOverlayEvent.ElementType t : componentMap.values()) {
                if (type == t) {
                    FMLLog.warning("Duplicate type override found for type " + type + "! (" + component + ")");
                    return;
                }
            }
        }
        for (IComponent c : getComponentList()) {
            if (c.getComponentID().equals(component.getComponentID())) {
                FMLLog.warning("Duplicate component id found for component " + component.getComponentID() + "! (" + component.getClass().getName() + " and " + c.getClass().getName() + ")");
                return;
            }
        }
        componentMap.put(component, type);
    }

    /**
     * Get the {@link net.ilexiconn.rearrange.api.component.IComponent} for the specific
     * {@link net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType}. Returns null if there isn't
     * any component overriding this type.
     *
     * @param type the type.
     * @return the {@link net.ilexiconn.rearrange.api.component.IComponent} overriding this type. Null if there isn't
     * any components overriding this type.
     */
    public static IComponent getComponentForType(RenderGameOverlayEvent.ElementType type) {
        if (type == null) {
            return null;
        }
        for (Map.Entry<IComponent, RenderGameOverlayEvent.ElementType> entry : componentMap.entrySet()) {
            if (entry.getValue() == type) {
                return entry.getKey();
            }
        }
        return null;
    }

    /**
     * Get the {@link net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType} this
     * {@link net.ilexiconn.rearrange.api.component.IComponent} is overriding. Returns null if the component isn't
     * overriding any types.
     *
     * @param component the component to check the type for.
     * @return the {@link net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType} this component is
     * overriding.
     */
    public static RenderGameOverlayEvent.ElementType getTypeForComponent(IComponent component) {
        if (component == null) {
            return null;
        }
        for (Map.Entry<IComponent, RenderGameOverlayEvent.ElementType> entry : componentMap.entrySet()) {
            if (entry.getKey() == component) {
                return entry.getValue();
            }
        }
        return null;
    }

    /**
     * Get the {@link net.ilexiconn.rearrange.api.component.IComponentConfig} instance for this
     * {@link net.ilexiconn.rearrange.api.component.IComponent}. Will create a new instance if there isn't a config
     * for this component yet.
     *
     * @param component the component.
     * @return the {@link net.ilexiconn.rearrange.api.component.IComponentConfig} instance.
     */
    public static IComponentConfig getConfigForComponent(IComponent component) {
        if (configMap.containsKey(component)) {
            return configMap.get(component);
        } else {
            IComponentConfig config = component.createConfig();
            if (!config.has("enabled")) {
                config.set("enabled", true);
            }
            if (!config.has("xPos")) {
                config.set("xPos", 0);
            }
            if (!config.has("yPos")) {
                config.set("yPos", 0);
            }
            configMap.put(component, config);
            return config;
        }
    }

    /**
     * Get the {@link net.ilexiconn.rearrange.api.component.IComponent} instance for this
     * {@link net.ilexiconn.rearrange.api.component.IComponentConfig}. Will return null if it can't find one.
     *
     * @param config the config.
     * @return the {@link net.ilexiconn.rearrange.api.component.IComponentConfig} null;
     */
    public static IComponent getComponentForConfig(IComponentConfig config) {
        for (Map.Entry<IComponent, IComponentConfig> entry : configMap.entrySet()) {
            if (entry.getValue() == config) {
                return entry.getKey();
            }
        }
        return null;
    }

    /**
     * Returns a list with all registered {@link net.ilexiconn.rearrange.api.component.IComponent}s.
     *
     * @return a list with all registered {@link net.ilexiconn.rearrange.api.component.IComponent}s.
     */
    public static List<IComponent> getComponentList() {
        return Lists.newArrayList(componentMap.keySet());
    }
}
