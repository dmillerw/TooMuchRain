package dmillerw.rain;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import dmillerw.rain.tick.WorldTicker;
import net.minecraftforge.common.config.Configuration;

import java.io.File;

/**
 * @author dmillerw
 */
@Mod(modid="TooMuchRain", name="TooMuchRain", version="%MOD_VERSION%", dependencies="required-after:Forge@[%FORGE_VERSION%,)", acceptableRemoteVersions = "*")
public class TooMuchRain {

	public static WorldTicker.ConfigWrapper settings;

	public static File config;

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		config = event.getSuggestedConfigurationFile();
	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		Configuration configuration = new Configuration(config);
		configuration.load();

		settings = new WorldTicker.ConfigWrapper().fromConfig(configuration);

		if (configuration.hasChanged()) {
			configuration.save();
		}

		FMLCommonHandler.instance().bus().register(new WorldTicker());
	}
}
