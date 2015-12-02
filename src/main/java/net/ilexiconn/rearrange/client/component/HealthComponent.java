package net.ilexiconn.rearrange.client.component;

import net.ilexiconn.rearrange.api.component.IComponent;
import net.ilexiconn.rearrange.api.component.IComponentButton;
import net.ilexiconn.rearrange.api.component.IComponentConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Random;

@SideOnly(Side.CLIENT)
public class HealthComponent extends Gui implements IComponent {
    public Minecraft mc = Minecraft.getMinecraft();
    public ResourceLocation texture = new ResourceLocation("textures/gui/icons.png");

    public Random random = new Random();

    public int updateCounter;
    public long healthUpdateCounter = 0L;
    public int playerHealth = 0;
    public long lastSystemTime = 0L;
    public int lastPlayerHealth = 0;

    @Override
    public String getComponentID() {
        return "health";
    }

    @Override
    public void init(List<IComponentButton> buttonList, IComponentConfig config) {

    }

    @Override
    public void render(int x, int y, IComponentConfig config) {
        mc.getTextureManager().bindTexture(texture);
        GlStateManager.enableBlend();

        EntityPlayer player = (EntityPlayer) mc.getRenderViewEntity();
        int health = MathHelper.ceiling_float_int(player.getHealth());
        boolean highlight = healthUpdateCounter > (long) updateCounter && (healthUpdateCounter - (long) updateCounter) / 3L % 2L == 1L;

        if (health < playerHealth && player.hurtResistantTime > 0) {
            lastSystemTime = Minecraft.getSystemTime();
            healthUpdateCounter = (long) (updateCounter + 20);
        } else if (health > playerHealth && player.hurtResistantTime > 0) {
            lastSystemTime = Minecraft.getSystemTime();
            healthUpdateCounter = (long) (updateCounter + 10);
        }

        if (Minecraft.getSystemTime() - lastSystemTime > 1000L) {
            playerHealth = health;
            lastPlayerHealth = health;
            lastSystemTime = Minecraft.getSystemTime();
        }

        playerHealth = health;
        int healthLast = lastPlayerHealth;

        IAttributeInstance attrMaxHealth = player.getEntityAttribute(SharedMonsterAttributes.maxHealth);
        float healthMax = (float) attrMaxHealth.getAttributeValue();
        float absorb = player.getAbsorptionAmount();

        int healthRows = MathHelper.ceiling_float_int((healthMax + absorb) / 2f / 10f);
        int rowHeight = Math.max(10 - (healthRows - 2), 3);

        random.setSeed((long) (updateCounter * 312871));

        int regen = -1;
        if (player.isPotionActive(Potion.regeneration)) {
            regen = updateCounter % 25;
        }

        final int top = 9 * (mc.theWorld.getWorldInfo().isHardcoreModeEnabled() ? 5 : 0);
        final int background = (highlight ? 25 : 16);
        int margin = 16;
        if (player.isPotionActive(Potion.poison)) {
            margin += 36;
        } else if (player.isPotionActive(Potion.wither)) {
            margin += 72;
        }
        float absorbRemaining = absorb;

        for (int i = MathHelper.ceiling_float_int((healthMax + absorb) / 2f) - 1; i >= 0; --i) {
            int row = MathHelper.ceiling_float_int((float) (i + 1) / 10f) - 1;
            int ix = x + i % 10 * 8;
            int iy = y - row * rowHeight;

            if (health <= 4) {
                iy += random.nextInt(2);
            }
            if (i == regen) {
                iy -= 2;
            }

            drawTexturedModalRect(ix, iy, background, top, 9, 9);

            if (highlight) {
                if (i * 2 + 1 < healthLast) {
                    drawTexturedModalRect(ix, iy, margin + 54, top, 9, 9);
                } else if (i * 2 + 1 == healthLast) {
                    drawTexturedModalRect(ix, iy, margin + 63, top, 9, 9);
                }
            }

            if (absorbRemaining > 0f) {
                if (absorbRemaining == absorb && absorb % 2f == 1f) {
                    drawTexturedModalRect(ix, iy, margin + 153, top, 9, 9);
                } else {
                    drawTexturedModalRect(ix, iy, margin + 144, top, 9, 9);
                }
                absorbRemaining -= 2f;
            } else {
                if (i * 2 + 1 < health) {
                    drawTexturedModalRect(ix, iy, margin + 36, top, 9, 9);
                } else if (i * 2 + 1 == health) {
                    drawTexturedModalRect(ix, iy, margin + 45, top, 9, 9);
                }
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
