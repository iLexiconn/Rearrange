package net.ilexiconn.rearrange.api.component;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public interface IComponentButton {
    char getDisplayChar(IComponentConfig config);

    String getTooltip(IComponentConfig config);

    void onClick(IComponentConfig config);
}
