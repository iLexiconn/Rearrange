package net.ilexiconn.rearrange;

import net.ilexiconn.llibrary.common.config.ConfigHelper;
import net.ilexiconn.llibrary.common.log.LoggerHelper;
import net.ilexiconn.rearrange.server.ServerProxy;
import net.ilexiconn.rearrange.server.config.RearrangeConfig;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = "rearrange", name = "Rearrange", version = Rearrange.VERSION, dependencies = "required-after:llibrary@[0.6.0,)")
public class Rearrange {
    public static final String VERSION = "0.1.0-develop";
    @SidedProxy(serverSide = "net.ilexiconn.rearrange.server.ServerProxy", clientSide = "net.ilexiconn.rearrange.client.ClientProxy")
    public static ServerProxy proxy;
    public static LoggerHelper logger = new LoggerHelper("Rearrange");

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        ConfigHelper.registerConfigHandler("rearrange", event.getSuggestedConfigurationFile(), new RearrangeConfig());
        proxy.preInit();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit();
    }
}
