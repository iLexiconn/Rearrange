package net.ilexiconn.rearrange.client.component;

import net.ilexiconn.llibrary.LLibrary;
import net.ilexiconn.rearrange.api.RearrangeAPI;
import net.ilexiconn.rearrange.api.component.IComponent;
import net.ilexiconn.rearrange.api.component.IComponentButton;
import net.ilexiconn.rearrange.api.component.IComponentConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

@SideOnly(Side.CLIENT)
public class HotbarComponent extends Gui implements IComponent {
    public Minecraft mc = Minecraft.getMinecraft();
    public ResourceLocation texture = new ResourceLocation("textures/gui/widgets.png");

    @Override
    public String getComponentID() {
        return "hotbar";
    }

    @Override
    public void init(List<IComponentButton> buttonList, IComponentConfig config) {
        buttonList.add(new IComponentButton() {
            @Override
            public char getDisplayChar(IComponentConfig config) {
                boolean animated = config.get("animated");
                return RearrangeAPI.getDefaultChar(animated);
            }

            @Override
            public String getTooltip(IComponentConfig config) {
                return "rearrange.hotbar.tooltip";
            }

            @Override
            public void onClick(IComponentConfig config) {
                boolean animated = config.get("animated");
                config.set("animated", !animated);
            }
        });
    }

    @Override
    public void render(int x, int y, IComponentConfig config) {
        if (mc.getRenderViewEntity() instanceof EntityPlayer) {
            GlStateManager.color(1f, 1f, 1f, 1f);
            mc.getTextureManager().bindTexture(texture);
            GlStateManager.enableBlend();
            EntityPlayer entityplayer = (EntityPlayer) mc.getRenderViewEntity();
            drawTexturedModalRect(x, y, 0, 0, 182, 22);
            drawTexturedModalRect(x - 1 + entityplayer.inventory.currentItem * 20, y - 1, 0, 22, 24, 22);
            GlStateManager.enableRescaleNormal();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            RenderHelper.enableGUIStandardItemLighting();
            boolean animated = config.get("animated");
            for (int j = 0; j < 9; ++j) {
                int k = x + j * 20 + 3;
                int l = y + 3;
                ItemStack itemstack = entityplayer.inventory.mainInventory[j];
                if (itemstack != null) {
                    float f = animated ? itemstack.animationsToGo - LLibrary.proxy.getPartialTicks() : 0f;
                    if (f > 0f) {
                        GlStateManager.pushMatrix();
                        float f1 = 1f + f / 5f;
                        GlStateManager.translate(k + 8, l + 12, 0f);
                        GlStateManager.scale(1.f / f1, (f1 + 1f) / 2f, 1f);
                        GlStateManager.translate(-(k + 8), -(l + 12), 0f);
                    }
                    mc.getRenderItem().renderItemAndEffectIntoGUI(itemstack, k, l);
                    if (f > 0f) {
                        GlStateManager.popMatrix();
                    }
                    mc.getRenderItem().renderItemOverlays(mc.fontRendererObj, itemstack, k, l);
                }
            }
            RenderHelper.disableStandardItemLighting();
            GlStateManager.disableRescaleNormal();
            GlStateManager.disableBlend();
        }
    }

    @Override
    public void update(IComponentConfig config) {

    }

    @Override
    public IComponentConfig createConfig() {
        IComponentConfig config = new DefaultComponentConfig();
        config.set("animated", true);
        return config;
    }

    @Override
    public int getWidth(IComponentConfig config) {
        return 182;
    }

    @Override
    public int getHeight(IComponentConfig config) {
        return 22;
    }
}
