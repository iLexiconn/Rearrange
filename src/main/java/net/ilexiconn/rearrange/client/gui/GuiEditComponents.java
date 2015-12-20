package net.ilexiconn.rearrange.client.gui;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.ilexiconn.llibrary.common.crash.SimpleCrashReport;
import net.ilexiconn.rearrange.Rearrange;
import net.ilexiconn.rearrange.api.RearrangeAPI;
import net.ilexiconn.rearrange.api.component.IComponent;
import net.ilexiconn.rearrange.api.component.IComponentButton;
import net.ilexiconn.rearrange.api.component.IComponentConfig;
import net.ilexiconn.rearrange.client.component.DefaultComponentButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@SideOnly(Side.CLIENT)
public class GuiEditComponents extends GuiScreen {
    public Map<IComponent, List<DefaultComponentButton>> buttonMap = Maps.newLinkedHashMap();
    public IComponent dragging;
    public int lastX;
    public int lastY;

    public void initGui() {
        buttonMap.clear();
        for (IComponent component : RearrangeAPI.getComponentList()) {
            IComponentConfig config = RearrangeAPI.getConfigForComponent(component);
            int xPos = config.get("xPos");
            int yPos = config.get("yPos");
            List<IComponentButton> buttonList = Lists.newArrayList();
            component.init(buttonList, RearrangeAPI.getConfigForComponent(component));
            IComponentButtonInternal[] internalButtons = initInternalButtons();
            Collections.addAll(buttonList, internalButtons);
            List<DefaultComponentButton> buttonInstanceList = Lists.newArrayList();
            for (int i = 0; i < buttonList.size(); i++) {
                IComponentButton button = buttonList.get(i);
                if (button == null) {
                    continue;
                }
                boolean flag = button instanceof IComponentButtonInternal;
                DefaultComponentButton gui = new DefaultComponentButton(flag ? (i - (buttonList.size() - internalButtons.length)) * 9 : i * 9, flag ? -11 : component.getHeight(config) + 1, button, config);
                gui.xPosition = xPos + gui.xRelative - 2;
                gui.yPosition = yPos + gui.yRelative;
                buttonInstanceList.add(gui);
            }
            buttonMap.put(component, buttonInstanceList);
        }
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        for (Map.Entry<IComponent, List<DefaultComponentButton>> entry : buttonMap.entrySet()) {
            IComponentConfig config = RearrangeAPI.getConfigForComponent(entry.getKey());
            boolean enabled = config.get("enabled");
            int xPos = config.get("xPos");
            int yPos = config.get("yPos");
            GlStateManager.disableLighting();
            drawRect(xPos - 1, yPos - 1, xPos + entry.getKey().getWidth(config) + 1, yPos + entry.getKey().getHeight(config) + 1, enabled ? 0x6000ff00 : 0x60ff0000);
            drawVerticalLine(xPos - 2, yPos - 2, yPos + entry.getKey().getHeight(config) + 1, 0xff000000);
            drawVerticalLine(xPos + entry.getKey().getWidth(config) + 1, yPos - 2, yPos + entry.getKey().getHeight(config) + 1, 0xff000000);
            drawHorizontalLine(xPos - 1, xPos + entry.getKey().getWidth(config), yPos - 2, 0xff000000);
            drawHorizontalLine(xPos - 1, xPos + entry.getKey().getWidth(config), yPos + entry.getKey().getHeight(config) + 1, 0xff000000);
            GlStateManager.color(1f, 1f, 1f, 1f);
            entry.getKey().render(xPos, yPos, config);
            for (DefaultComponentButton button : entry.getValue()) {
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
            for (Map.Entry<IComponent, List<DefaultComponentButton>> entry : buttonMap.entrySet()) {
                IComponentConfig config = RearrangeAPI.getConfigForComponent(entry.getKey());
                boolean flag = false;
                for (DefaultComponentButton button : entry.getValue()) {
                    if (button.mousePressed(mc, mouseX, mouseY)) {
                        flag = true;
                        button.playPressSound(mc.getSoundHandler());
                        button.componentButton.onClick(config);
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
        for (Map.Entry<IComponent, List<DefaultComponentButton>> entry : buttonMap.entrySet()) {
            IComponentConfig config = RearrangeAPI.getConfigForComponent(entry.getKey());
            int xPos = config.get("xPos");
            int yPos = config.get("yPos");
            for (DefaultComponentButton button : entry.getValue()) {
                button.displayString = String.valueOf(button.componentButton.getDisplayChar(config));
                button.xPosition = xPos + button.xRelative - 2;
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

    public IComponentButtonInternal[] initInternalButtons() {
        IComponentButtonInternal buttonEnable = new IComponentButtonInternal() {
            @Override
            public char getDisplayChar(IComponentConfig config) {
                boolean enabled = config.get("enabled");
                return RearrangeAPI.getDefaultChar(enabled);
            }

            @Override
            public String getTooltip(IComponentConfig config) {
                return "rearrange.enable.tooltip";
            }

            @Override
            public void onClick(IComponentConfig config) {
                boolean enabled = config.get("enabled");
                config.set("enabled", !enabled);
            }
        };

        return new IComponentButtonInternal[]{buttonEnable};
    }

    private interface IComponentButtonInternal extends IComponentButton {

    }
}