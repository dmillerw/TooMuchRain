package dmillerw.rain;

import com.google.common.collect.Maps;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import dmillerw.rain.tick.WorldTicker;
import net.minecraftforge.common.config.Configuration;

import java.io.File;
import java.util.Map;

/**
 * @author dmillerw
 */
@Mod(modid="omg-rain", name="TooMuchRain", version="1.0.0")
public class TooMuchRain {

	public static Map<Integer, WorldTicker.ConfigWrapper> settingMap = Maps.newHashMap();

	public static File config;

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		config = event.getSuggestedConfigurationFile();
	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		Configuration configuration = new Configuration(config);
		configuration.load();

//		for (int dimension : DimensionManager.getIDs()) {
//			WorldServer world = DimensionManager.getWorld(dimension);
//
//			if (!world.provider.hasNoSky) {
				settingMap.put(0, new WorldTicker.ConfigWrapper().fromConfig(configuration, 0));
//			}
//		}

		if (configuration.hasChanged()) {
			configuration.save();
		}

		FMLCommonHandler.instance().bus().register(new WorldTicker());
	}

}
