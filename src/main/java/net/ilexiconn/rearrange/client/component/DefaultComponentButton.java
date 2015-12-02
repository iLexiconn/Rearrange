package net.ilexiconn.rearrange.client.component;

import net.ilexiconn.rearrange.api.component.IComponentButton;
import net.ilexiconn.rearrange.api.component.IComponentConfig;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fml.client.config.GuiButtonExt;
import net.minecraftforge.fml.client.config.HoverChecker;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class DefaultComponentButton extends GuiButtonExt {
    public int xRelative;
    public int yRelative;
    public String tooltip;
    public HoverChecker hoverChecker;
    public IComponentButton componentButton;

    public DefaultComponentButton(int x, int y, IComponentButton button, IComponentConfig config) {
        super(0, 0, 0, 10, 10, button.getDisplayString(config));
        xRelative = x;
        yRelative = y;
        tooltip = StatCollector.translateToLocal(button.getTooltip(config));
        hoverChecker = new HoverChecker(this, 800);
        componentButton = button;
    }
}
