package net.ilexiconn.rearrange.client.component;

import net.ilexiconn.rearrange.api.RearrangeAPI;
import net.ilexiconn.rearrange.api.component.IComponent;
import net.ilexiconn.rearrange.api.component.IComponentButton;
import net.ilexiconn.rearrange.api.component.IComponentConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

@SideOnly(Side.CLIENT)
public class ArmorComponent extends Gui implements IComponent {
    public Minecraft mc = Minecraft.getMinecraft();
    public ResourceLocation texture = new ResourceLocation("textures/gui/icons.png");

    @Override
    public String getComponentID() {
        return "armor";
    }

    @Override
    public void init(List<IComponentButton> buttonList, IComponentConfig config) {
        buttonList.add(new IComponentButton() {
            @Override
            public char getDisplayChar(IComponentConfig config) {
                boolean dynamicSize = config.get("dynamicSize");
                return RearrangeAPI.getDefaultChar(dynamicSize);
            }

            @Override
            public String getTooltip(IComponentConfig config) {
                return "rearrange.armor.tooltip";
            }

            @Override
            public void onClick(IComponentConfig config) {
                boolean dynamicSize = config.get("dynamicSize");
                config.set("dynamicSize", !dynamicSize);
            }
        });
    }

    @Override
    public void render(int x, int y, IComponentConfig config) {
        GlStateManager.enableBlend();
        mc.getTextureManager().bindTexture(texture);
        int level = ForgeHooks.getTotalArmorValue(mc.thePlayer);
        boolean dynamicSize = config.get("dynamicSize");
        for (int i = 1; level > 0 && i < 20; i += 2) {
            if (i < level) {
                drawTexturedModalRect(x, y, 34, 9, 9, 9);
            } else if (i == level) {
                drawTexturedModalRect(x, y, 25, 9, 9, 9);
            } else if (i > level) {
                if (dynamicSize) {
                    continue;
                }
                drawTexturedModalRect(x, y, 16, 9, 9, 9);
            }
            x += 8;
        }
        GlStateManager.disableBlend();
    }

    @Override
    public void update(IComponentConfig config) {

    }

    @Override
    public IComponentConfig createConfig() {
        IComponentConfig config = new DefaultComponentConfig();
        config.set("dynamicSize", false);
        return config;
    }

    @Override
    public int getWidth(IComponentConfig config) {
        return 81;
    }

    @Override
    public int getHeight(IComponentConfig config) {
        return 9;
    }
}
