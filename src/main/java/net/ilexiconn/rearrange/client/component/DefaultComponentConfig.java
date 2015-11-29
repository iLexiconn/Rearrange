package net.ilexiconn.rearrange.client.component;

import com.google.common.collect.Maps;
import net.ilexiconn.rearrange.api.component.IComponentConfig;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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
}
