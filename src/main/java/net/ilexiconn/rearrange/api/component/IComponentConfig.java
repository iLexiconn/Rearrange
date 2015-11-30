package net.ilexiconn.rearrange.api.component;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;

/**
 * Interface for component configs. You can find the default implementation at
 * {@link net.ilexiconn.rearrange.client.component.DefaultComponentConfig}.
 *
 * @author iLexiconn
 * @since 0.1.0
 */
@SideOnly(Side.CLIENT)
public interface IComponentConfig {
    /**
     * Set a variable in the config.
     *
     * @param id the string identifier for this variable.
     * @param value the value of this variable.
     * @param <T> the type of this variable.
     * @throws UnsupportedOperationException if not supported by the used implementation.
     */
    <T> void set(String id, T value);

    /**
     *Get a variable from the config.
     *
     * @param id the string identifier for this variable.
     * @param <T> the type of this variable.
     * @return the variable.
     * @throws UnsupportedOperationException if not supported by the used implementation.
     */
    <T> T get(String id);

    /**
     * Check if the config has a variable with this identifier.
     *
     * @param id the string identifier of a variable.
     * @return true if the config has a variable with this identifier.
     * @throws UnsupportedOperationException if not supported by the used implementation.
     */
    boolean has(String id);

    /**
     * Save the current config to a file.
     *
     * @throws IOException if the file can't be written to.
     */
    void save() throws IOException;

    /**
     * Read the Rearrange config file and apply the values to this config instance.
     *
     * @throws IOException if the file can't be read from.
     */
    void load() throws IOException;
}
