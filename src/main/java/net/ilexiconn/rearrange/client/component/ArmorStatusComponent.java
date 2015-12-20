package net.ilexiconn.rearrange.client.component;

import net.ilexiconn.rearrange.api.RearrangeAPI;
import net.ilexiconn.rearrange.api.component.IComponent;
import net.ilexiconn.rearrange.api.component.IComponentButton;
import net.ilexiconn.rearrange.api.component.IComponentConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

@SideOnly(Side.CLIENT)
public class ArmorStatusComponent extends Gui implements IComponent {
    public Minecraft mc = Minecraft.getMinecraft();

    @Override
    public String getComponentID() {
        return "armor_status";
    }

    @Override
    public void init(List<IComponentButton> buttonList, IComponentConfig config) {
        buttonList.add(new IComponentButton() {
            @Override
            public char getDisplayChar(IComponentConfig config) {
                boolean showDamage = config.get("showDamage");
                return RearrangeAPI.getDefaultChar(showDamage);
            }

            @Override
            public String getTooltip(IComponentConfig config) {
                return "rearrange.armorStatus.tooltip.showDamage";
            }

            @Override
            public void onClick(IComponentConfig config) {
                boolean showDamage = config.get("showDamage");
                config.set("showDamage", !showDamage);
            }
        });
        buttonList.add(new IComponentButton() {
            @Override
            public char getDisplayChar(IComponentConfig config) {
                boolean showNames = config.get("showNames");
                return RearrangeAPI.getDefaultChar(showNames);
            }

            @Override
            public String getTooltip(IComponentConfig config) {
                return "rearrange.armorStatus.tooltip.showNames";
            }

            @Override
            public void onClick(IComponentConfig config) {
                boolean showNames = config.get("showNames");
                config.set("showNames", !showNames);
            }
        });
    }

    @Override
    public void render(int x, int y, IComponentConfig config) {
        boolean showDamage = config.get("showDamage");
        boolean showNames = config.get("showNames");
        for (int i = 3; i >= 0; i--) {
            ItemStack stack = mc.thePlayer.getCurrentArmor(i);
            if (stack != null) {
                RenderHelper.enableGUIStandardItemLighting();
                mc.getRenderItem().zLevel = 100f;
                mc.getRenderItem().renderItemAndEffectIntoGUI(stack, x, y);
                if (showDamage) {
                    mc.getRenderItem().renderItemOverlayIntoGUI(mc.fontRendererObj, stack, x, y, null);
                }
                if (showNames) {
                    mc.fontRendererObj.drawStringWithShadow(stack.getDisplayName(), x + 18, y + 4, 0xffffff);
                }
                mc.getRenderItem().zLevel = 0f;
                RenderHelper.disableStandardItemLighting();
            }
            y+=16;
        }
    }

    @Override
    public void update(IComponentConfig config) {

    }

    @Override
    public IComponentConfig createConfig() {
        IComponentConfig config = new DefaultComponentConfig();
        config.set("showDamage", true);
        config.set("showNames", false);
        return config;
    }

    @Override
    public int getWidth(IComponentConfig config) {
        int width = 16;
        boolean showNames = config.get("showNames");
        if (showNames) {
            for (int i = 3; i >= 0; i--) {
                ItemStack stack = mc.thePlayer.getCurrentArmor(i);
                if (stack != null) {
                    width = Math.max(width, 16 + mc.fontRendererObj.getStringWidth(stack.getDisplayName()) + 4);
                }
            }
        }
        return width;
    }

    @Override
    public int getHeight(IComponentConfig config) {
        return 64;
    }
}
