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

@SideOnly(Side.CLIENT)
public class RearrangeAPI {
    public static final String VERSION = "0.1.0";

    private static Map<IComponent, RenderGameOverlayEvent.ElementType> componentMap = Maps.newHashMap();
    private static Map<IComponent, IComponentConfig> configMap = Maps.newHashMap();

    public static void registerComponent(IComponent component) {
        registerOverrideComponent(component, null);
    }

    public static void registerOverrideComponent(IComponent component, RenderGameOverlayEvent.ElementType type) {
        if (type != null) {
            for (RenderGameOverlayEvent.ElementType t : componentMap.values()) {
                if (type ==  t) {
                    FMLLog.warning("Duplicate type override found for type " + type + "! (" + component + ")");
                    return;
                }
            }
        }
        componentMap.put(component, type);
    }

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

    public static IComponentConfig getComponentConfig(IComponent component) {
        if (configMap.containsKey(component)) {
            return configMap.get(component);
        } else {
            IComponentConfig config = component.createNewConfig();
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

    public static List<IComponent> getComponentList() {
        return Lists.newArrayList(componentMap.keySet());
    }
}
