package dmillerw.rain.util;

/**
 * @author dmillerw
 */
public class NumberUtil {

	public static boolean inRange(int num1, int num2, int range) {
		return Math.abs(num2 - num1) <= range;
	}
}
