package net.ilexiconn.rearrange.client.component;

import com.google.common.collect.Maps;
import net.ilexiconn.llibrary.common.config.ConfigHelper;
import net.ilexiconn.rearrange.api.RearrangeAPI;
import net.ilexiconn.rearrange.api.component.IComponent;
import net.ilexiconn.rearrange.api.component.IComponentConfig;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;
import java.util.Map;

@SideOnly(Side.CLIENT)
public class DefaultComponentConfig implements IComponentConfig {
    private Map<String, Object> map = Maps.newHashMap();

    public <T> void set(String id, T value) {
        map.put(id, value);
    }

    public <T> T get(String id) {
        if (map.containsKey(id)) {
            return (T) map.get(id);
        } else {
            return null;
        }
    }

    public boolean has(String id) {
        return map.containsKey(id);
    }

    public void save() throws IOException {
        IComponent component = RearrangeAPI.getComponentForConfig(this);
        if (component != null) {
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                ConfigHelper.setProperty("rearrange", component.getComponentID(), entry.getKey(), String.valueOf(entry.getValue()), getTypeFromObject(entry.getValue()));
            }
        }
    }

    public void load() throws IOException {
        IComponent component = RearrangeAPI.getComponentForConfig(this);
        Configuration config = ConfigHelper.getConfigContainer("rearrange").getConfiguration();
        if (component != null && config != null) {
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                entry.setValue(saveObjectToConfig(config, component.getComponentID(), entry.getKey(), entry.getValue()));
            }
        }
    }

    public Object saveObjectToConfig(Configuration config, String id, String var, Object value) {
        if (value instanceof Boolean) {
            return config.getBoolean(var, id, (Boolean) value, "");
        } else if (value instanceof Integer) {
            return config.getInt(var, id, (Integer) value, Integer.MIN_VALUE, Integer.MAX_VALUE, "");
        } else if (value instanceof Float || value instanceof Double) {
            return config.getFloat(var, id, (Float) value, Float.MIN_VALUE, Float.MAX_VALUE, "");
        } else if (value instanceof String) {
            return config.getString(var, id, (String) value, "");
        } else {
            return null;
        }
    }

    public Property.Type getTypeFromObject(Object object) {
        if (object instanceof Boolean) {
            return Property.Type.BOOLEAN;
        } else if (object instanceof Integer) {
            return Property.Type.INTEGER;
        } else if (object instanceof Float || object instanceof Double) {
            return Property.Type.DOUBLE;
        } else if (object instanceof String) {
            return Property.Type.STRING;
        } else {
            return null;
        }
    }
}
