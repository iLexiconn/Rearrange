package net.ilexiconn.rearrange.api.component;

import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

/**
 * Interface for all components. You can register classes implementing this interface using
 * {@link net.ilexiconn.rearrange.api.RearrangeAPI#registerComponent(IComponent)} or
 * {@link net.ilexiconn.rearrange.api.RearrangeAPI#registerOverrideComponent(IComponent, RenderGameOverlayEvent.ElementType)}.
 *
 * @author iLexiconn
 * @since 0.1.0
 */
@SideOnly(Side.CLIENT)
public interface IComponent {
    /**
     * Return the id of this component. This value is used for the config entry.
     * You should not use capitals in the ID. Use underscores instead. Example:
     * DO:      test_component
     * DON'T:   testComponent
     *
     * @return the id of this component.
     */
    String getComponentID();

    /**
     * Called when the user opens the edit gui.
     *
     * @param buttonList the list with all the button instances.
     * @param config     the config instance.
     */
    void init(List<IComponentButton> buttonList, IComponentConfig config);

    /**
     * Called every render tick.
     *
     * @param x      the current x coordinate.
     * @param y      the current y coordinate.
     * @param config the config instance.
     */
    void render(int x, int y, IComponentConfig config);

    /**
     * Called every update tick.
     *
     * @param config the config instance.
     */
    void update(IComponentConfig config);

    /**
     * Called when a new config file is getting created. Here you can put all default values for your component.
     * You can use the default implementation ({@link net.ilexiconn.rearrange.client.component.DefaultComponentConfig})
     * or create your own one.
     *
     * @return the new config instance.
     */
    IComponentConfig createConfig();

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
