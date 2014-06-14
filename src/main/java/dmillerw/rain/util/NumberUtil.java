package dmillerw.rain.util;

/**
 * @author dmillerw
 */
public class NumberUtil {

	public static boolean inRange(int base, int check, int value) {
		return (base >= value || base <= value + check);
	}

}
