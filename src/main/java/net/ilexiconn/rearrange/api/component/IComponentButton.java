package net.ilexiconn.rearrange.api.component;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public interface IComponentButton {
    int getID();

    String getDisplayString(IComponentConfig config);

    String getTooltip(IComponentConfig config);

    void onClick(IComponentConfig config);
}
