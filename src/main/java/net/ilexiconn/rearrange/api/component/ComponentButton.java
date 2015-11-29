package net.ilexiconn.rearrange.api.component;

import net.minecraft.util.StatCollector;
import net.minecraftforge.fml.client.config.GuiButtonExt;
import net.minecraftforge.fml.client.config.HoverChecker;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ComponentButton extends GuiButtonExt {
    public int xRelative;
    public int yRelative;
    public String tooltip;
    public HoverChecker hoverChecker;

    public ComponentButton(int id, int xPos, int yPos, String displayString, String tooltip) {
        super(id, 0, 0, 10, 10, displayString);
        xRelative = xPos;
        yRelative = yPos;
        this.tooltip = StatCollector.translateToLocal(tooltip);
        hoverChecker = new HoverChecker(this, 800);
    }
}
