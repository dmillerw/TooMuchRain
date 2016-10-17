package dmillerw.rain;

import dmillerw.rain.tick.WorldTicker;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

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
