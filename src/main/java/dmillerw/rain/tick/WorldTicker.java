package dmillerw.rain.tick;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import dmillerw.rain.TooMuchRain;
import dmillerw.rain.util.NumberUtil;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.common.config.Configuration;

import java.util.Random;

/**
 * @author dmillerw
 */
public class WorldTicker {

	public static final int RAIN_DURATION_CONSTANT = 12000;
	public static final int RAIN_DURATION_MODULATOR = 12000;
	public static final int RAIN_BREAK_CONSTANT = 12000;
	public static final int RAIN_BREAK_MODULATOR = 168000;

	public static final Random random = new Random();

	public static class ConfigWrapper {
		public int rainDurationConstant = 0;
		public int rainDurationModulator = 0;
		public int rainBreakConstant = 0;
		public int rainBreakModulator = 0;

		public ConfigWrapper fromConfig(Configuration configuration, int dimension) {
//			String name = DimensionManager.getProvider(dimension).getDimensionName();
			rainDurationConstant = configuration.get(Configuration.CATEGORY_GENERAL, "rainDurationConstant", WorldTicker.RAIN_DURATION_CONSTANT).getInt();
			rainDurationModulator = configuration.get(Configuration.CATEGORY_GENERAL, "rainDurationModulator", WorldTicker.RAIN_DURATION_MODULATOR).getInt();
			rainBreakConstant = configuration.get(Configuration.CATEGORY_GENERAL, "rainBreakConstant", WorldTicker.RAIN_BREAK_CONSTANT).getInt();
			rainBreakModulator = configuration.get(Configuration.CATEGORY_GENERAL, "rainBreakModulator", WorldTicker.RAIN_BREAK_MODULATOR).getInt();
			return this;
		}
	}

	/*

	Brief overview of how the weather system works:
	For both thunder and rain, there are three values. time, strength, and a boolean flag

	Time is an int value with how many ticks until the next update. Unless forced, if the rain time set to 16000 ticks
	and rain is enabled, it's gonna keep raining for the next 16000 ticks. Likewise, if it's not raining, you'll have some
	peace and quiet for those ticks

	The boolean flag is only taken into account when the tick value is <= 0, in which case things are recalculated

	 */

	/**
	 * Stores the tick value for the last world tick, so we can see if a command was used
	 */
	private int lastRainTick = 0;

	/**
	 * Stores the rain enabled value for the last tick, so we can see if something changed
	 */
	private boolean lastRainState = false;

	@SubscribeEvent
	public void onWorldTick(TickEvent.WorldTickEvent event) {
		if (event.side.isServer() && event.phase == TickEvent.Phase.END) {
			WorldServer world = (WorldServer) event.world;
			WorldInfo info = world.getWorldInfo();

			if (!world.provider.hasNoSky) {
				ConfigWrapper config = TooMuchRain.settingMap.get(world.provider.dimensionId);
				int rainTick = info.getRainTime();
				boolean rainState = info.isRaining();

				/* DO CALC */

				System.out.println(info.getRainTime() + " " + lastRainTick + " = " + NumberUtil.inRange(lastRainTick, 10, rainTick));

				lastRainTick = rainTick;

				if (rainState != lastRainState) {
					// Ticks have expired, so recalculate
					if (!info.isRaining()) {
						info.setRainTime(random.nextInt(config.rainBreakModulator) + config.rainBreakConstant);
					} else {
						info.setRainTime(random.nextInt(config.rainDurationModulator) + config.rainDurationConstant);
					}

					lastRainState = rainState;
				}
			}
		}
	}

}
