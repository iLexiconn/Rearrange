package net.ilexiconn.rearrange.client.component;

import net.ilexiconn.rearrange.api.component.IComponent;
import net.ilexiconn.rearrange.api.component.IComponentButton;
import net.ilexiconn.rearrange.api.component.IComponentConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.util.FoodStats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Random;

@SideOnly(Side.CLIENT)
public class FoodComponent extends Gui implements IComponent {
    public Minecraft mc = Minecraft.getMinecraft();
    public ResourceLocation texture = new ResourceLocation("textures/gui/icons.png");

    public Random random = new Random();

    public int updateCounter;

    @Override
    public String getComponentID() {
        return "food";
    }

    @Override
    public void init(List<IComponentButton> buttonList, IComponentConfig config) {

    }

    @Override
    public void render(int x, int y, IComponentConfig config) {
        EntityPlayer player = (EntityPlayer) mc.getRenderViewEntity();
        mc.getTextureManager().bindTexture(texture);
        GlStateManager.enableBlend();

        FoodStats stats = player.getFoodStats();
        int level = stats.getFoodLevel();

        for (int i = 0; i < 10; ++i) {
            int idx = i * 2 + 1;
            int ix = x - i * 8 + 72;
            int icon = 16;
            int background = 0;

            if (player.isPotionActive(Potion.hunger)) {
                icon += 36;
                background = 13;
            }

            if (player.getFoodStats().getSaturationLevel() <= 0f && updateCounter % (level * 3 + 1) == 0) {
                y += (random.nextInt(3) - 1);
            }

            drawTexturedModalRect(ix, y, 16 + background * 9, 27, 9, 9);

            if (idx < level) {
                drawTexturedModalRect(ix, y, icon + 36, 27, 9, 9);
            } else if (idx == level) {
                drawTexturedModalRect(ix, y, icon + 45, 27, 9, 9);
            }
        }
        GlStateManager.disableBlend();
    }

    @Override
    public void update(IComponentConfig config) {
        updateCounter++;
    }

    @Override
    public IComponentConfig createConfig() {
        return new DefaultComponentConfig();
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
