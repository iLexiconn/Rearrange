package net.ilexiconn.rearrange.client.gui;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.ilexiconn.llibrary.common.crash.SimpleCrashReport;
import net.ilexiconn.rearrange.Rearrange;
import net.ilexiconn.rearrange.api.RearrangeAPI;
import net.ilexiconn.rearrange.api.component.ComponentButton;
import net.ilexiconn.rearrange.api.component.IComponent;
import net.ilexiconn.rearrange.api.component.IComponentConfig;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@SideOnly(Side.CLIENT)
public class GuiEditComponents extends GuiScreen {
    public Map<IComponent, List<ComponentButton>> buttonMap = Maps.newHashMap();
    public IComponent dragging;
    public int lastX;
    public int lastY;

    public void initGui() {
        buttonMap.clear();
        for (IComponent component : RearrangeAPI.getComponentList()) {
            IComponentConfig config = RearrangeAPI.getConfigForComponent(component);
            boolean enabled = config.get("enabled");
            int xPos = config.get("xPos");
            int yPos = config.get("yPos");
            List<ComponentButton> buttonList = Lists.newArrayList();
            component.init(buttonList, RearrangeAPI.getConfigForComponent(component));
            buttonList.add(new ComponentButton(-1, -1, -11, enabled ? "o" : "x", "Display this component"));
            for (ComponentButton button : buttonList) {
                button.xPosition = xPos + button.xRelative;
                button.yPosition = yPos + button.yRelative;
            }
            buttonMap.put(component, buttonList);
        }
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        for (IComponent component : RearrangeAPI.getComponentList()) {
            IComponentConfig config = RearrangeAPI.getConfigForComponent(component);
            boolean enabled = config.get("enabled");
            int xPos = config.get("xPos");
            int yPos = config.get("yPos");
            drawRect(xPos - 1, yPos - 1, xPos + component.getWidth(config) + 1, yPos + component.getHeight(config) + 1, enabled ? 0xff00ff00 : 0xffff0000);
            component.render(xPos, yPos, config);
        }
        for (List<ComponentButton> buttonList : buttonMap.values()) {
            for (ComponentButton button : buttonList) {
                GlStateManager.disableLighting();
                button.drawButton(mc, mouseX, mouseY);
                if (button.hoverChecker.checkHover(mouseX, mouseY)) {
                    drawHoveringText(mc.fontRendererObj.listFormattedStringToWidth(button.tooltip, 300), mouseX, mouseY);
                }
            }
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if (mouseButton == 0) {
            for (Map.Entry<IComponent, List<ComponentButton>> entry : buttonMap.entrySet()) {
                IComponentConfig config = RearrangeAPI.getConfigForComponent(entry.getKey());
                boolean flag = false;
                for (ComponentButton button : entry.getValue()) {
                    if (button.mousePressed(mc, mouseX, mouseY)) {
                        flag = true;
                        button.playPressSound(mc.getSoundHandler());
                        if (button.id == -1) {
                            boolean enabled = config.get("enabled");
                            config.set("enabled", !enabled);
                            button.displayString = enabled ? "x" : "o";
                        } else {
                            entry.getKey().actionPerformed(button, RearrangeAPI.getConfigForComponent(entry.getKey()));
                        }
                    }
                }
                if (flag) {
                    continue;
                }
                int xPos = config.get("xPos");
                int yPos = config.get("yPos");
                if (mouseX < xPos || mouseY < yPos || mouseX >= xPos + entry.getKey().getWidth(config) || mouseY >= yPos + entry.getKey().getHeight(config)) {
                    continue;
                }
                dragging = entry.getKey();
                lastX = mouseX;
                lastY = mouseY;
                return;
            }
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    public void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        if (dragging != null) {
            IComponentConfig config = RearrangeAPI.getConfigForComponent(dragging);
            int width = dragging.getWidth(config);
            int height = dragging.getHeight(config);
            int xPos = config.get("xPos");
            int yPos = config.get("yPos");
            int xNew = xPos + mouseX - lastX;
            int yNew = yPos + mouseY - lastY;
            if (xNew < 0) {
                xNew = 0;
            }
            if (yNew < 0) {
                yNew = 0;
            }
            if (xNew + width > this.width) {
                xNew = this.width - width;
            }
            if (yNew + height > this.height) {
                yNew = this.height - height;
            }
            config.set("xPos", xNew);
            config.set("yPos", yNew);
            lastX = mouseX;
            lastY = mouseY;
        }
    }

    public void mouseReleased(int mouseX, int mouseY, int state) {
        if (dragging != null) {
            dragging = null;
            lastX = 0;
            lastY = 0;
        }
    }

    public void updateScreen() {
        for (Map.Entry<IComponent, List<ComponentButton>> entry : buttonMap.entrySet()) {
            IComponentConfig config = RearrangeAPI.getConfigForComponent(entry.getKey());
            int xPos = config.get("xPos");
            int yPos = config.get("yPos");
            for (ComponentButton button : entry.getValue()) {
                button.xPosition = xPos + button.xRelative;
                button.yPosition = yPos + button.yRelative;
            }
        }
    }

    public void onGuiClosed() {
        for (IComponent component : RearrangeAPI.getComponentList()) {
            IComponentConfig config = RearrangeAPI.getConfigForComponent(component);
            if (config != null) {
                try {
                    config.save();
                } catch (IOException e) {
                    Rearrange.logger.error(SimpleCrashReport.makeCrashReport(e, "Unable to save config for component " + component.getComponentID()));
                }
            }
        }
    }
}