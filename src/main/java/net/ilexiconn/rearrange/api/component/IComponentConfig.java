package net.ilexiconn.rearrange.api.component;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public interface IComponentConfig {
    <T> void set(String id, T value);

    <T> T get(String id);

    boolean has(String id);
}
