package net.ilexiconn.rearrange.api.component;

import net.minecraft.client.gui.GuiButton;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

@SideOnly(Side.CLIENT)
public interface IComponent {
    /**
     * Called when the user opens the edit gui.
     *
     * @param buttonList the list with all the button instances.
     * @param config the config instance.
     */
    void init(List<ComponentButton> buttonList, IComponentConfig config);

    /**
     * Called every render tick.
     *
     * @param x the current x coordinate.
     * @param y the current y coordinate.
     * @param config the config instance.
     */
    void render(int x, int y, IComponentConfig config);

    /**
     * Called when the user presses on a button.
     *
     * @param button the clicked button instance.
     * @param config the config instance.
     */
    void actionPerformed(GuiButton button, IComponentConfig config);

    /**
     * Called every update tick.
     *
     * @param config the config instance.
     */
    void update(IComponentConfig config);

    /**
     * Called when a new config file is getting created. Here you can put all default values for your component.
     * <p/>
     * You can use the default implementation ({@link net.ilexiconn.rearrange.client.component.DefaultComponentConfig})
     * or create your own one.
     *
     * @return the new config instance.
     */
    IComponentConfig createNewConfig();

    /**
     * Get the current width for this component. Used to trigger the component dragging. This method gets called every
     * update tick.
     *
     * @param config the config instance.
     * @return the current width.
     */
    int getWidth(IComponentConfig config);

    /**
     * Get the current height for this component. Used to trigger the component dragging. This method gets called every
     * update tick.
     *
     * @param config the config instance.
     * @return the current height.
     */
    int getHeight(IComponentConfig config);
}
