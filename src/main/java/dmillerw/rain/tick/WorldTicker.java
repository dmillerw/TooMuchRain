package dmillerw.rain.tick;

import dmillerw.rain.TooMuchRain;
import dmillerw.rain.util.NumberUtil;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.Random;

/**
 * @author dmillerw
 */
public class WorldTicker {

	public static final int RAIN_DURATION_CONSTANT = 12000;
	public static final int RAIN_DURATION_MODULATOR = 12000;
	public static final int RAIN_BREAK_CONSTANT = 12000;
	public static final int RAIN_BREAK_MODULATOR = 168000;

	public static final int THUNDER_DURATION_CONSTANT = 3600;
	public static final int THUNDER_DURATION_MODULATOR = 12000;
	public static final int THUNDER_BREAK_CONSTANT = 12000;
	public static final int THUNDER_BREAK_MODULATOR = 168000;

	public static final Random random = new Random();

	public static class ConfigWrapper {
		public int rainDurationConstant = 0;
		public int rainDurationModulator = 0;
		public int rainBreakConstant = 0;
		public int rainBreakModulator = 0;

		public int thunderDurationConstant = 0;
		public int thunderDurationModulator = 0;
		public int thunderBreakConstant = 0;
		public int thunderBreakModulator = 0;
		
		public ConfigWrapper fromConfig(Configuration configuration) {
			rainDurationConstant = configuration.get("rain", "rainDurationConstant", WorldTicker.RAIN_DURATION_CONSTANT).getInt();
			rainDurationModulator = configuration.get("rain", "rainDurationModulator", WorldTicker.RAIN_DURATION_MODULATOR).getInt();
			rainBreakConstant = configuration.get("rain", "rainBreakConstant", WorldTicker.RAIN_BREAK_CONSTANT).getInt();
			rainBreakModulator = configuration.get("rain", "rainBreakModulator", WorldTicker.RAIN_BREAK_MODULATOR).getInt();

			thunderDurationConstant = configuration.get("thunder", "thunderDurationConstant", WorldTicker.THUNDER_DURATION_CONSTANT).getInt();
			thunderDurationModulator = configuration.get("thunder", "thunderDurationModulator", WorldTicker.THUNDER_DURATION_MODULATOR).getInt();
			thunderBreakConstant = configuration.get("thunder", "thunderBreakConstant", WorldTicker.THUNDER_BREAK_CONSTANT).getInt();
			thunderBreakModulator = configuration.get("thunder", "thunderBreakModulator", WorldTicker.THUNDER_BREAK_MODULATOR).getInt();
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

	/**
	 * Stores the tick value for the last world tick, so we can see if a command was used
	 */
	private int lastThunderTick = 0;

	/**
	 * Stores the rain enabled value for the last tick, so we can see if something changed
	 */
	private boolean lastThunderState = false;

	@SubscribeEvent
	public void onWorldTick(TickEvent.WorldTickEvent event) {
		if (event.side.isServer() && event.phase == TickEvent.Phase.END) {
			WorldServer world = (WorldServer) event.world;
			WorldInfo info = world.getWorldInfo();

			if (!world.provider.getHasNoSky()) {
				tickRain(info, TooMuchRain.settings);
				tickThunder(info, TooMuchRain.settings);
			}
		}
	}

	private void tickRain(WorldInfo info, ConfigWrapper config) {
		int rainTick = info.getRainTime();
		boolean rainState = info.isRaining();

		if (lastRainTick != 0 && !NumberUtil.inRange(lastRainTick, rainTick, 10)) {
			// Someone forcibly changed the tick timing. Recalculate
			recalculateRainTicks(info, config);
		}

		lastRainTick = info.getRainTime();

		if (rainState != lastRainState) {
			// Ticks have expired, so recalculate
			recalculateRainTicks(info, config);
			lastRainState = rainState;
		}
	}

	private void tickThunder(WorldInfo info, ConfigWrapper config) {
		int thunderTick = info.getThunderTime();
		boolean thunderState = info.isThundering();

		if (lastThunderTick != 0 && !NumberUtil.inRange(lastThunderTick, thunderTick, 10)) {
			// Someone forcibly changed the tick timing. Recalculate
			recalculateThunderTicks(info, config);
		}

		lastThunderTick = info.getThunderTime();

		if (thunderState != lastThunderState) {
			// Ticks have expired, so recalculate
			recalculateThunderTicks(info, config);
			lastThunderState = thunderState;
		}
	}

	private void recalculateRainTicks(WorldInfo info, ConfigWrapper config) {
		if (!info.isRaining()) {
			info.setRainTime(random.nextInt(config.rainBreakModulator + 1) + config.rainBreakConstant);
		} else {
			info.setRainTime(random.nextInt(config.rainDurationModulator + 1) + config.rainDurationConstant);
		}
	}

	private void recalculateThunderTicks(WorldInfo info, ConfigWrapper config) {
		if (!info.isThundering()) {
			info.setThunderTime(random.nextInt(config.thunderBreakModulator + 1) + config.thunderBreakConstant);
		} else {
			info.setThunderTime(random.nextInt(config.thunderDurationModulator + 1) + config.thunderDurationModulator);
		}
	}
}
