package net.ilexiconn.rearrange.client.component;

import net.ilexiconn.rearrange.api.component.ComponentButton;
import net.ilexiconn.rearrange.api.component.IComponent;
import net.ilexiconn.rearrange.api.component.IComponentConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

@SideOnly(Side.CLIENT)
public class ExperienceComponent extends Gui implements IComponent {
    public Minecraft mc = Minecraft.getMinecraft();

    public String getComponentID() {
        return "experience";
    }

    public void init(List<ComponentButton> buttonList, IComponentConfig config) {

    }

    public void render(int x, int y, IComponentConfig config) {
        GlStateManager.color(1f, 1f, 1f, 1f);
        GlStateManager.disableBlend();
        int barWidth = 182;

        if (mc.playerController.gameIsSurvivalOrAdventure()) {
            int cap = mc.thePlayer.xpBarCap();

            if (cap > 0) {
                int filled = (int) (mc.thePlayer.experience * (float) (barWidth + 1));
                drawTexturedModalRect(x, y, 0, 64, barWidth, 5);

                if (filled > 0) {
                    drawTexturedModalRect(x, y, 0, 69, filled, 5);
                }
            }

            if (mc.playerController.gameIsSurvivalOrAdventure() && mc.thePlayer.experienceLevel > 0) {
                FontRenderer fontRenderer = mc.fontRendererObj;
                boolean flag1 = false;
                int color = flag1 ? 16777215 : 8453920;
                String text = "" + mc.thePlayer.experienceLevel;
                int ix = x + (barWidth - fontRenderer.getStringWidth(text)) / 2;
                int iy = y - 2;
                fontRenderer.drawString(text, ix + 1, iy, 0);
                fontRenderer.drawString(text, ix - 1, iy, 0);
                fontRenderer.drawString(text, ix, iy + 1, 0);
                fontRenderer.drawString(text, ix, iy - 1, 0);
                fontRenderer.drawString(text, ix, iy, color);
            }
        }
        GlStateManager.enableBlend();
        GlStateManager.color(1f, 1f, 1f, 1f);
    }

    public void actionPerformed(GuiButton button, IComponentConfig config) {

    }

    public void update(IComponentConfig config) {

    }

    public IComponentConfig createConfig() {
        return new DefaultComponentConfig();
    }

    public int getWidth(IComponentConfig config) {
        return 182;
    }

    public int getHeight(IComponentConfig config) {
        return 5;
    }
}
