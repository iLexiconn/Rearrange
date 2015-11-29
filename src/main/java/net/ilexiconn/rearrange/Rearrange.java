package net.ilexiconn.rearrange;

import net.ilexiconn.rearrange.server.ServerProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = "rearrange", name = "Rearrange", version = Rearrange.VERSION, dependencies = "required-after:llibrary@[0.6.0,)")
public class Rearrange {
    @SidedProxy(serverSide = "net.ilexiconn.rearrange.server.ServerProxy", clientSide = "net.ilexiconn.rearrange.client.ClientProxy")
    public static ServerProxy proxy;

    public static final String VERSION = "0.1.0-develop";

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        proxy.preInit();
    }
}
